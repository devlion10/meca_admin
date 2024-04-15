package kr.or.kpf.lms.biz.homepage.event.service;

import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideApiResponseVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventApiRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.response.EventApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.ClassGuide;
import kr.or.kpf.lms.repository.entity.homepage.EventInfo;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import kr.or.kpf.lms.repository.homepage.EventRepository;
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
 * 이벤트/설문 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EventService extends CSServiceSupport {

    private static final String EVENT_IMG_TAG = "_EVENT";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 이벤트/설문 Repository */
    private final EventRepository eventRepository;

    /**
     * 이벤트/설문 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(EventViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 이벤트/설문 생성
     *
     * @param requestVO
     * @return
     */
    public EventApiResponseVO createInfo(EventApiRequestVO requestVO) {
        EventInfo entity = EventInfo.builder().build();
        copyNonNullObject(requestVO, entity);

        EventApiResponseVO result = EventApiResponseVO.builder().build();
        BeanUtils.copyProperties(eventRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 이벤트/설문 업데이트
     *
     * @param requestVO
     * @return
     */
    public EventApiResponseVO updateInfo(EventApiRequestVO requestVO) {
        return eventRepository.findById(requestVO.getSequenceNo())
                .map(eventInfo -> {

                    copyNonNullObject(requestVO, eventInfo);

                    EventApiResponseVO result = EventApiResponseVO.builder().build();
                    BeanUtils.copyProperties(eventRepository.saveAndFlush(eventInfo), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7125, "해당 이벤트/설문 미존재"));
    }

    /**
     이벤트/설문 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(EventApiRequestVO requestObject) {
        eventRepository.delete(eventRepository.findById(requestObject.getSequenceNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 이벤트/설문 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 이벤트/설문 첨부파일 등록
     *
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(Long serialNo, MultipartFile attachFile) {
        /** 이벤트 이력 확인 */
        EventInfo eventInfo = eventRepository.findById(serialNo)
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7022, "이벤트 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getEventFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getEventFolder())
                                    .append("/")
                                    .append(eventInfo.getRegistUserId() + EVENT_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            eventInfo.setThumbFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
