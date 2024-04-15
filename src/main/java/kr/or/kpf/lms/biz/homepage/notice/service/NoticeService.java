package kr.or.kpf.lms.biz.homepage.notice.service;

import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancApiRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeApiRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeViewRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.response.NoticeApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import kr.or.kpf.lms.repository.entity.homepage.Notice;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.homepage.NoticeRepository;
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
 * 홈페이지 관리 > 공지사항 관련 Service
 */
@Service
@RequiredArgsConstructor
public class NoticeService extends CSServiceSupport {

    private static final String NOTICE_IMG_TAG = "_NOTICE";
    private static final String PREFIX_NOTICE = "NOTI";

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 공지사항 Repository */
    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(NoticeViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 공지사항 생성
     *
     * @param noticeApiRequestVO
     * @return
     */
    public NoticeApiResponseVO createNotice(NoticeApiRequestVO noticeApiRequestVO) {
        Notice entity = Notice.builder().build();
        copyNonNullObject(noticeApiRequestVO, entity);

        /** 공지사항 일련 번호 획득 */
        String noticeSerialNo = commonHomepageRepository.generateCode(PREFIX_NOTICE);
        entity.setNoticeSerialNo(noticeSerialNo);
        /** 조회수 '0' 셋팅 */
        entity.setViewCount(BigInteger.ZERO);

        NoticeApiResponseVO result = NoticeApiResponseVO.builder().build();

        BeanUtils.copyProperties(noticeRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 공지사항 업데이트
     *
     * @param noticeApiRequestVO
     * @return
     */
    public NoticeApiResponseVO updateNotice(NoticeApiRequestVO noticeApiRequestVO) {
        return noticeRepository.findOne(Example.of(Notice.builder().noticeSerialNo(noticeApiRequestVO.getNoticeSerialNo()).build()))
                .map(notice -> {

                    copyNonNullObject(noticeApiRequestVO, notice);

                    NoticeApiResponseVO result = NoticeApiResponseVO.builder().build();
                    BeanUtils.copyProperties(noticeRepository.saveAndFlush(notice), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7041, "해당 공지사항 미존재"));
    }

    /**
     * 공지사항 첨부파일 업로드
     *
     * @param noticeSerialNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String noticeSerialNo, MultipartFile attachFile) {
        /** 공지사항 이력 확인 */
        Notice noticeMaster = noticeRepository.findOne(Example.of(Notice.builder()
                        .noticeSerialNo(noticeSerialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7041, "대상 공지사항 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getNoticeFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getNoticeFolder())
                                    .append("/")
                                    .append(authenticationInfo().getUserId() + NOTICE_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            noticeMaster.setAttachFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            noticeMaster.setAttachFileSize(file.getSize());
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     공지사항 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(NoticeApiRequestVO requestObject) {
        noticeRepository.delete(noticeRepository.findOne(Example.of(Notice.builder()
                        .noticeSerialNo(requestObject.getNoticeSerialNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3507, "삭제된 공지사항 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

}
