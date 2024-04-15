package kr.or.kpf.lms.biz.homepage.myqna.service;

import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaApiRequestVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaViewRequestVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.response.MyQnaApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.homepage.MyQnaRepository;
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
 * 홈페이지 관리 > 1:1 문의 관련 Service
 */
@Service
@RequiredArgsConstructor
public class MyQnaService extends CSServiceSupport {

    private static final String MY_QNA_IMG_TAG = "_MY_QNA";

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 1:1 문의 Repository */
    private final MyQnaRepository myQnaRepository;

    /**
     * 1:1 문의 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(MyQnaViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 1:1 문의 업데이트
     *
     * @param myQnaApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public MyQnaApiResponseVO updateMyQna(MyQnaApiRequestVO myQnaApiRequestVO) {
        return myQnaRepository.findOne(Example.of(MyQna.builder().sequenceNo(myQnaApiRequestVO.getSequenceNo()).build()))
                .map(myQna -> {

                    copyNonNullObject(myQnaApiRequestVO, myQna);

                    MyQnaApiResponseVO result = MyQnaApiResponseVO.builder().build();
                    BeanUtils.copyProperties(myQnaRepository.saveAndFlush(myQna), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7022, "해당 1:1문의 미존재"));
    }

    /**
     * 1:1 문의 첨부파일 등록
     *
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(BigInteger serialNo, MultipartFile attachFile) {
        /** 1:1 문의 이력 확인 */
        MyQna myQna = myQnaRepository.findOne(Example.of(MyQna.builder()
                        .sequenceNo(serialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7022, "대상 1:1 문의 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getMyQnaFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getMyQnaFolder())
                                    .append("/")
                                    .append(myQna.getRegistUserId() + MY_QNA_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            myQna.setResAttachFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            myQna.setResFileSize(file.getSize());
                            myQna.setResFileOrigin(file.getOriginalFilename());
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
