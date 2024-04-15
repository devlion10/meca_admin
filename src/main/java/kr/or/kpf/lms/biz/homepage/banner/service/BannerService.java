package kr.or.kpf.lms.biz.homepage.banner.service;

import kr.or.kpf.lms.biz.homepage.banner.vo.request.BannerApiRequestVO;
import kr.or.kpf.lms.biz.homepage.banner.vo.request.BannerViewRequestVO;
import kr.or.kpf.lms.biz.homepage.banner.vo.response.BannerApiResponseVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventApiRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.response.EventApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.EventInfo;
import kr.or.kpf.lms.repository.entity.homepage.HomeBanner;
import kr.or.kpf.lms.repository.homepage.EventRepository;
import kr.or.kpf.lms.repository.homepage.HomeBannerRepository;
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
 * 배너 관련 Service
 */
@Service
@RequiredArgsConstructor
public class BannerService extends CSServiceSupport {

    private static final String BANNER_IMG_TAG = "_BANNER";
    private static final String PREFIX_HOMEPAGE_BANNER = "HBN";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 배너 Repository */
    private final HomeBannerRepository bannerRepository;

    /**
     * 배너 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(BannerViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 배너 생성
     *
     * @param requestVO
     * @return
     */
    public BannerApiResponseVO createInfo(BannerApiRequestVO requestVO) {
        HomeBanner entity = HomeBanner.builder().build();
        copyNonNullObject(requestVO, entity);

        /** 배너 일련 번호 획득 */
        String bannerSerialNo = commonHomepageRepository.generateCode(PREFIX_HOMEPAGE_BANNER);
        entity.setBannerSn(bannerSerialNo);

        BannerApiResponseVO result = BannerApiResponseVO.builder().build();
        BeanUtils.copyProperties(bannerRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 배너 업데이트
     *
     * @param requestVO
     * @return
     */
    public BannerApiResponseVO updateInfo(BannerApiRequestVO requestVO) {
        return bannerRepository.findById(requestVO.getBannerSn())
                .map(banner -> {

                    copyNonNullObject(requestVO, banner);

                    BannerApiResponseVO result = BannerApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bannerRepository.saveAndFlush(banner), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "해당 배너 미존재"));
    }

    /**
     배너 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(BannerApiRequestVO requestObject) {
        bannerRepository.delete(bannerRepository.findById(requestObject.getBannerSn())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "이미 삭제된 배너 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 배너 첨부파일 등록
     *
     * @param bannerSn
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String bannerSn, MultipartFile attachFile) {
        /** 배너 이력 확인 */
        HomeBanner banner = bannerRepository.findById(bannerSn)
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "배너 이력 없음."));

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
                                    .append(banner.getRegistUserId() + BANNER_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            banner.setBannerImagePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
