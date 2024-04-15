package kr.or.kpf.lms.biz.homepage.eduplace.service;

import kr.or.kpf.lms.biz.homepage.eduplace.vo.request.EduPlaceApiRequestVO;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.request.EduPlaceViewRequestVO;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.response.EduPlaceApiResponseVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.EduPlaceAply;
import kr.or.kpf.lms.repository.homepage.EduPlaceAplyRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * 교육장 장소 신청 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EduPlaceService extends CSServiceSupport {

    private static final String EDU_PLACE_IMG_TAG = "_EDU_PLACE";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 교육장 장소 신청 Repository */
    private final EduPlaceAplyRepository eduPlaceAplyRepository;

    /**
     * 교육장 장소 신청 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(EduPlaceViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 교육장 장소 신청 생성
     *
     * @param requestVO
     * @return
     */
    public EduPlaceApiResponseVO createInfo(EduPlaceApiRequestVO requestVO) {
        EduPlaceAply entity = EduPlaceAply.builder().build();
        copyNonNullObject(requestVO, entity);

        EduPlaceApiResponseVO result = EduPlaceApiResponseVO.builder().build();
        BeanUtils.copyProperties(eduPlaceAplyRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 교육장 장소 신청 업데이트
     *
     * @param requestVO
     * @return
     */
    public EduPlaceApiResponseVO updateInfo(EduPlaceApiRequestVO requestVO) {
        return eduPlaceAplyRepository.findById(requestVO.getSequenceNo())
                .map(eventInfo -> {

                    copyNonNullObject(requestVO, eventInfo);

                    EduPlaceApiResponseVO result = EduPlaceApiResponseVO.builder().build();
                    BeanUtils.copyProperties(eduPlaceAplyRepository.saveAndFlush(eventInfo), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7125, "해당 교육장 장소 신청 미존재"));
    }

    /**
     교육장 장소 신청 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(EduPlaceApiRequestVO requestObject) {
        eduPlaceAplyRepository.delete(eduPlaceAplyRepository.findById(requestObject.getSequenceNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 교육장 장소 신청 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 교육장 장소 신청 첨부파일 등록
     *
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(Long serialNo, MultipartFile attachFile) {
        /** 교육장 장소 신청 확인 */
        EduPlaceAply eduPlaceAply = eduPlaceAplyRepository.findOne(Example.of(EduPlaceAply.builder()
                        .sequenceNo(serialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7022, "교육장 장소 신청 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getEduPlaceFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getEventFolder())
                                    .append("/")
                                    .append(eduPlaceAply.getRegistUserId() + EDU_PLACE_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            eduPlaceAply.setAtchFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
