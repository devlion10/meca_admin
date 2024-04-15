package kr.or.kpf.lms.biz.business.apply.service;

import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyExcelVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyFreeExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizAplyDtlRepository;
import kr.or.kpf.lms.repository.BizAplyDtlFileRepository;
import kr.or.kpf.lms.repository.BizAplyRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizAply;
import kr.or.kpf.lms.repository.entity.BizAplyDtl;
import kr.or.kpf.lms.repository.entity.BizAplyDtlFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizAplyService extends CSServiceSupport {
    private static final String BIZ_APLY_FILE_TAG = "_BIZ_APLY";
    private final CommonBusinessRepository commonBusinessRepository;
    private final BizAplyRepository bizAplyRepository;

    private final BizAplyDtlRepository bizAplyDtlRepository;
    private final BizAplyDtlFileRepository bizAplyDtlFileRepository;

    /**
     사업 공고 신청 - 언론인/기본형/자유형 정보 삭제
     */
    public CSResponseVOSupport deleteBizAply(BizAplyApiRequestVO requestObject) {
        bizAplyRepository.delete(bizAplyRepository.findOne(Example.of(BizAply.builder()
                .sequenceNo(requestObject.getSequenceNo())
                .build()))
            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3547, "삭제된 사업 공고 신청 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     사업 공고 신청 - 언론인/기본형/자유형 리스트
     */
    public <T> Page<T> getBizAplyList(BizAplyViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 사업 공고 신청 - 언론인/기본형/자유형 결과보고서 업로드
     *
     * @param sequenceNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(BigInteger sequenceNo, MultipartFile attachFile) {
        /** 사업 공고 신청 - 언론인/기본형 이력 확인 */
        BizAply bizAply = bizAplyRepository.findOne(Example.of(BizAply.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3545, "대상 사업 공고 신청 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getBizAplyFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getBizAplyFolder())
                                    .append("/")
                                    .append(authenticationInfo().getUserId() + BIZ_APLY_FILE_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            bizAply.setBizAplyFile(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            bizAply.setBizAplyFileSize(file.getSize());
                            bizAply.setBizAplyFileOrigin(file.getOriginalFilename());
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
     엑셀 다운로드
     */
    public <T> List<BizAplyExcelVO> getExcel(BizAplyViewRequestVO requestObject) {
        List<BizAplyExcelVO> bizAplyExcelVOList = (List<BizAplyExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizAplyExcelVOList;
    }
    public <T> List<BizAplyFreeExcelVO> getFreeExcel(BizAplyViewRequestVO requestObject) {
        List<BizAplyFreeExcelVO> bizAplyExcelVOList = (List<BizAplyFreeExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizAplyExcelVOList;
    }


    public CSResponseVOSupport updateStts(BizAplyApiRequestVO requestObject) {
        if(requestObject.getBizAplyStts()!=null && requestObject.getSequenceNoList() != null){
            for(BigInteger seqNo : requestObject.getSequenceNoList()){
                Optional<BizAply> bizAply =bizAplyRepository.findById(seqNo);
                if(bizAply.isPresent()){
                    BizAply aply = bizAply.get();
                    aply.setBizAplyStts(requestObject.getBizAplyStts());
                    bizAplyRepository.saveAndFlush(aply);
                }
            }
        }
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    public CSResponseVOSupport deleteAll(BizAplyApiRequestVO requestObject) {
        if(requestObject.getSequenceNoList() != null){
            for(BigInteger seqNo : requestObject.getSequenceNoList()){
                Optional<BizAply> bizAply = bizAplyRepository.findById(seqNo);
                if(bizAply.isPresent()){
                    BizAply aply = bizAply.get();
                    List<BizAplyDtl> dtls = bizAplyDtlRepository.findAll(Example.of(BizAplyDtl.builder().sequenceNo(aply.getSequenceNo()).build()));
                    if(dtls!=null && dtls.size()>0){
                        for(BizAplyDtl dtl : dtls){
                            bizAplyDtlRepository.delete(dtl);
                        }
                    }
                    bizAplyDtlFileRepository.deleteAll(bizAplyDtlFileRepository.findAll(Example.of(BizAplyDtlFile.builder()
                            .fileSn(aply.getSequenceNo().longValue())
                            .build())));
                    bizAplyRepository.delete(aply);
                }
            }
        }
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}