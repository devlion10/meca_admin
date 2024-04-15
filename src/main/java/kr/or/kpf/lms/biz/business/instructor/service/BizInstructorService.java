package kr.or.kpf.lms.biz.business.instructor.service;

import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorPbancApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorPbancApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorTestExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.*;
import kr.or.kpf.lms.repository.entity.BizInstructor;
import kr.or.kpf.lms.repository.entity.BizInstructorAply;
import kr.or.kpf.lms.repository.entity.BizInstructorPbanc;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizInstructorService extends CSServiceSupport {
    private static final String INSTR_FILE_TAG = "INSTR";
    private static final String PREFIX_INSTR = "ISR";

    private final CommonBusinessRepository commonBusinessRepository;

    private final BizInstructorRepository bizInstructorRepository;

    private final BizInstructorPbancRepository bizInstructorPbancRepository;
    private final BizInstructorAplyRepository bizInstructorAplyRepository;

    /**
     강사 모집 정보 생성
     */
    @Transactional(rollbackOn = {Exception.class})
    public BizInstructorApiResponseVO createBizInstructor(BizInstructorApiRequestVO requestObject) {
        BizInstructor entity = BizInstructor.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizInstrNo(commonBusinessRepository.generateCode(PREFIX_INSTR));
        BizInstructorApiResponseVO result = BizInstructorApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizInstructorRepository.saveAndFlush(entity), result);

        /** 강사 모집 공고가 등록되었다면, 해당하는 강사 모집 공고에 등록된 사업 공고 리스트, 관련 정보 적재 */
        if (result != null) {
            if (requestObject.getBizInstrPbancs() != null && requestObject.getBizInstrPbancs().size() > 0) {
                List<BizInstructorPbanc> bizInstructorPbancs = new ArrayList<>();
                int i = 1;
                for(BizInstructorPbancApiRequestVO requestVO : requestObject.getBizInstrPbancs()){
                    if (bizInstructorPbancRepository.findAll(Example.of(BizInstructorPbanc.builder()
                            .bizPbancNo(requestVO.getBizPbancNo())
                            .build()))
                            .isEmpty()) {
                        bizInstructorPbancs.add(
                                BizInstructorPbanc.builder()
                                        .sequenceNo(commonBusinessRepository.generateInstrPbancAutoIncrease() + i)
                                        .bizInstrNo(result.getBizInstrNo())
                                        .bizPbancNo(requestVO.getBizPbancNo())
                                        .bizInstrRcptBgng(requestVO.getBizInstrRcptBgng())
                                        .bizInstrRcptEnd(requestVO.getBizInstrRcptEnd())
                                        .bizInstrPbancStts(requestVO.getBizInstrPbancStts())
                                        .build()
                        );
                        i++;
                    }else{
                        result.setBizPbacnCode("3602");
                    }
                }
                bizInstructorPbancRepository.saveAllAndFlush(bizInstructorPbancs);
            }
        }
        return result;
    }

    /**
     강사 모집 정보 업데이트
     */
    @Transactional(rollbackOn = {Exception.class})
    public BizInstructorApiResponseVO updateBizInstructor(BizInstructorApiRequestVO requestObject) {
        return bizInstructorRepository.findById(requestObject.getBizInstrNo())
                .map(bizInstructor -> {
                    copyNonNullObject(requestObject, bizInstructor);

                    BizInstructorApiResponseVO result = BizInstructorApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizInstructorRepository.saveAndFlush(bizInstructor), result);

                    if (requestObject.getBizInstrPbancs() != null && requestObject.getBizInstrPbancs().size() > 0) {
                        for (BizInstructorPbancApiRequestVO entity : requestObject.getBizInstrPbancs()) {
                            if (entity.getSequenceNo() != null &&  entity.getSequenceNo() > 0) {
                                bizInstructorPbancRepository.findBizInstructorPbancBySequenceNo(entity.getSequenceNo())
                                        .map(bizInstructorPbanc -> {
                                            copyNonNullObject(entity, bizInstructorPbanc);

                                            BizInstructorPbancApiResponseVO vo = BizInstructorPbancApiResponseVO.builder().build();
                                            BeanUtils.copyProperties(bizInstructorPbancRepository.saveAndFlush(bizInstructorPbanc), vo);

                                            return vo;
                                        }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3605, "해당 강사 모집 사업 공고 미존재"));
                            } else {
                                if (bizInstructorPbancRepository.findAll(Example.of(BizInstructorPbanc.builder()
                                                .bizPbancNo(entity.getBizPbancNo())
                                                .build()))
                                        .isEmpty()) {
                                    bizInstructorPbancRepository.saveAndFlush(BizInstructorPbanc.builder()
                                            .sequenceNo(commonBusinessRepository.generateInstrPbancAutoIncrease())
                                            .bizInstrNo(bizInstructor.getBizInstrNo())
                                            .bizPbancNo(entity.getBizPbancNo())
                                            .bizInstrRcptBgng(entity.getBizInstrRcptBgng())
                                            .bizInstrRcptEnd(entity.getBizInstrRcptEnd())
                                            .bizInstrPbancStts(entity.getBizInstrPbancStts())
                                            .build());
                                }else{
                                    result.setBizPbacnCode("3602");
                                }

                            }
                        }
                    }

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3605, "해당 강사 모집 미존재"));
    }

    /**
     강사 모집 정보 삭제
     */
    public CSResponseVOSupport deleteBizInstructor(BizInstructorApiRequestVO requestObject) {
        if (!bizInstructorAplyRepository.findAll(Example.of(BizInstructorAply.builder()
                .bizInstrNo(requestObject.getBizInstrNo())
                .build())).isEmpty()) {
            return CSResponseVOSupport.of(KPF_RESULT.ERROR3608);
        } else {
            bizInstructorRepository.delete(bizInstructorRepository.findOne(Example.of(BizInstructor.builder()
                            .bizInstrNo(requestObject.getBizInstrNo())
                            .build()))
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3607, "삭제된 강사 모집 입니다.")));

            bizInstructorPbancRepository.deleteAll(bizInstructorPbancRepository.findAll(Example.of(BizInstructorPbanc.builder()
                    .bizInstrNo(requestObject.getBizInstrNo())
                    .build())));

            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        }
    }


    /**
     강사 모집 사업 삭제
     */
    public CSResponseVOSupport deleteBizPbanc(BizInstructorApiRequestVO requestObject, String bizPbancNo) {
        if (!bizInstructorAplyRepository.findAll(Example.of(BizInstructorAply.builder()
                .bizInstrNo(requestObject.getBizInstrNo())
                .build()))
                .isEmpty()) {
            return CSResponseVOSupport.of(KPF_RESULT.ERROR3608);
        } else {
            bizInstructorPbancRepository.deleteAll(bizInstructorPbancRepository.findAll(Example.of(BizInstructorPbanc.builder()
                    .bizInstrNo(requestObject.getBizInstrNo())
                    .bizPbancNo(bizPbancNo)
                    .build())));

            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        }
    }

    /**
     강사 모집 리스트
     */
    public <T> Page<T> getBizInstructorList(BizInstructorViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강사 모집 상세
     */
    public <T> T getBizInstructor(BizInstructorViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    /**
     * 강사공고 결과 업로드
     *
     * @param bizInstrNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String bizInstrNo, MultipartFile attachFile) {
        /** 공지사항 이력 확인 */
        BizInstructor bizInstructor = bizInstructorRepository.findOne(Example.of(BizInstructor.builder()
                        .bizInstrNo(bizInstrNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3545, "대상 사업 공고 신청 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getBizInstrFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getBizInstrFolder())
                                    .append("/")
                                    .append(INSTR_FILE_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            bizInstructor.setBizInstrFile(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            bizInstructor.setBizInstrFileSize(file.getSize());
                            bizInstructor.setBizInstrFileOrigin(file.getOriginalFilename());
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
    public <T> List<BizInstructorTestExcelVO> getExcel(BizInstructorViewRequestVO requestObject) {
        List<BizInstructorTestExcelVO> bizInstructorExcelVOList = (List<BizInstructorTestExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizInstructorExcelVOList;
    }

    /**
     강사 모집 정보 자동 업데이트
     */
    public void updateAutoBizInstructor() {
        LocalDate now = LocalDate.now();
        List<BizInstructorPbanc> bizInstructorPbancs = bizInstructorPbancRepository.findByBizInstrRcptEndBeforeAndBizInstrPbancStts(now, 1);
        for (BizInstructorPbanc bizInstructorPbanc : bizInstructorPbancs) {
            bizInstructorPbanc.setBizInstrPbancStts(0);
            bizInstructorPbancRepository.save(bizInstructorPbanc);
        }
    }

}
