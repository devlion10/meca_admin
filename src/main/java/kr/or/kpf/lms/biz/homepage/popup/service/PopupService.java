package kr.or.kpf.lms.biz.homepage.popup.service;

import kr.or.kpf.lms.biz.homepage.popup.vo.request.PopupApiRequestVO;
import kr.or.kpf.lms.biz.homepage.popup.vo.request.PopupViewRequestVO;
import kr.or.kpf.lms.biz.homepage.popup.vo.response.PopupApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.HomePopup;
import kr.or.kpf.lms.repository.homepage.HomePopupRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * 팝업 관련 Service
 */
@Service
@RequiredArgsConstructor
public class PopupService extends CSServiceSupport {

    private static final String POPUP_IMG_TAG = "_POPUP";
    private static final String PREFIX_HOMEPAGE_POPUP = "HPU";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 팝업 Repository */
    private final HomePopupRepository popupRepository;

    /**
     * 팝업 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(PopupViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 팝업 생성
     *
     * @param requestVO
     * @return
     */
    public PopupApiResponseVO createInfo(PopupApiRequestVO requestVO) {
        HomePopup entity = HomePopup.builder().build();
        copyNonNullObject(requestVO, entity);

        /** 팝업 일련 번호 획득 */
        String popupSerialNo = commonHomepageRepository.generateCode(PREFIX_HOMEPAGE_POPUP);
        entity.setPopupSn(popupSerialNo);

        PopupApiResponseVO result = PopupApiResponseVO.builder().build();
        BeanUtils.copyProperties(popupRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 팝업 업데이트
     *
     * @param requestVO
     * @return
     */
    public PopupApiResponseVO updateInfo(PopupApiRequestVO requestVO) {
        return popupRepository.findById(requestVO.getPopupSn())
                .map(popup -> {

                    copyNonNullObject(requestVO, popup);

                    PopupApiResponseVO result = PopupApiResponseVO.builder().build();
                    BeanUtils.copyProperties(popupRepository.saveAndFlush(popup), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7125, "해당 팝업 미존재"));
    }

    /**
     팝업 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(PopupApiRequestVO requestObject) {
        popupRepository.delete(popupRepository.findById(requestObject.getPopupSn())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 팝업 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 팝업 첨부파일 등록
     *
     * @param popupSn
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String popupSn, MultipartFile attachFile) {
        /** 팝업 이력 확인 */
        HomePopup popup = popupRepository.findById(popupSn)
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "팝업 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getBannerFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getBannerFolder())
                                    .append("/")
                                    .append(popup.getRegistUserId() + POPUP_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            popup.setPopupImagePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
