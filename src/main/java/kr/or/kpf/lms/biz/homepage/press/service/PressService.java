package kr.or.kpf.lms.biz.homepage.press.service;

import kr.or.kpf.lms.biz.homepage.event.vo.request.EventApiRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
import kr.or.kpf.lms.biz.homepage.press.vo.request.PressApiRequestVO;
import kr.or.kpf.lms.biz.homepage.press.vo.request.PressViewRequestVO;
import kr.or.kpf.lms.biz.homepage.press.vo.response.PressApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.PressRelease;
import kr.or.kpf.lms.repository.homepage.PressReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * 행사소개 관련 Service
 */
@Service
@RequiredArgsConstructor
public class PressService extends CSServiceSupport {

    private static final String PRESS_IMG_TAG = "_PRESS";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 행사소개 Repository */
    private final PressReleaseRepository pressReleaseRepository;

    /**
     * 행사소개 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(PressViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 행사소개 생성
     *
     * @param requestVO
     * @return
     */
    public PressApiResponseVO createInfo(PressApiRequestVO requestVO) {
        PressRelease entity = PressRelease.builder().build();
        /** 조회수 '0' 셋팅 */
        entity.setViewCount(0L);
        copyNonNullObject(requestVO, entity);

        PressApiResponseVO result = PressApiResponseVO.builder().build();
        BeanUtils.copyProperties(pressReleaseRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 행사소개 업데이트
     *
     * @param requestVO
     * @return
     */
    public PressApiResponseVO updateInfo(PressApiRequestVO requestVO) {
        return pressReleaseRepository.findById(requestVO.getSequenceNo())
                .map(pressRelease -> {

                    copyNonNullObject(requestVO, pressRelease);

                    PressApiResponseVO result = PressApiResponseVO.builder().build();
                    BeanUtils.copyProperties(pressReleaseRepository.saveAndFlush(pressRelease), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7125, "해당 행사소개 미존재"));
    }

    /**
     행사소개 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(PressApiRequestVO requestObject) {
        pressReleaseRepository.delete(pressReleaseRepository.findById(requestObject.getSequenceNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 행사소개 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 행사소개 첨부파일 등록
     *
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(Long serialNo, MultipartFile attachFile) {
        /** 이벤트 이력 확인 */
        PressRelease pressRelease = pressReleaseRepository.findOne(Example.of(PressRelease.builder()
                        .sequenceNo(serialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7022, "행사소개 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getPressFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getPressFolder())
                                    .append("/")
                                    .append(pressRelease.getRegistUserId() + PRESS_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            pressRelease.setAtchFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            pressRelease.setAtchFileSize(file.getSize());
                            pressRelease.setAtchFileOrigin(file.getOriginalFilename());
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
