package kr.or.kpf.lms.biz.business.instructor.identify.service;

import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.*;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.user.history.vo.FormeBizlecinfoViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizInstructorIdentifyDtlRepository;
import kr.or.kpf.lms.repository.BizInstructorIdentifyRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.*;
import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BizInstructorIdentifyService extends CSServiceSupport {
    private static final String PREFIX_INSTR_IDENTIFY = "BII";

    private static final String ATCH_TAG = "_ATCH";
    private static final String LSN_TAG = "_LSN";

    private final CommonBusinessRepository commonBusinessRepository;

    private final BizInstructorIdentifyRepository bizInstructorIdentifyRepository;
    private final BizInstructorIdentifyDtlRepository bizInstructorIdentifyDtlRepository;

    /**
     강의확인서 정보 생성
     */
    public BizInstructorIdentifyApiResponseVO createBizInstructorIdentify(BizInstructorIdentifyApiRequestVO requestObject) {
        BizInstructorIdentify entity = BizInstructorIdentify.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizInstrIdntyNo(commonBusinessRepository.generateCode(PREFIX_INSTR_IDENTIFY));
        BizInstructorIdentifyApiResponseVO result = BizInstructorIdentifyApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizInstructorIdentifyRepository.saveAndFlush(entity), result);

        return result;
    }

    /**
     강의확인서 정보 업데이트
     */
    public BizInstructorIdentifyApiResponseVO updateBizInstructorIdentify(BizInstructorIdentifyApiRequestVO requestObject) {
        return bizInstructorIdentifyRepository.findById(requestObject.getBizInstrIdntyNo())
                .map(bizInstructorIdentify -> {
                    copyNonNullObject(requestObject, bizInstructorIdentify);

                    if (requestObject.getBizInstrIdntyStts() == 2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                        bizInstructorIdentify.setBizInstrIdntyAprvDt(sdf.format(date));
                    }

                    BizInstructorIdentifyApiResponseVO result = BizInstructorIdentifyApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizInstructorIdentifyRepository.saveAndFlush(bizInstructorIdentify), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3635, "해당 강의확인서 미존재"));
    }

    /**
     강의확인서 정보 초기화
     */
    public BizInstructorIdentifyApiResponseVO removeBizInstructorIdentify(BizInstructorIdentifyApiRequestVO requestObject) {
        if(requestObject.getIdntyNo().equals(null) || requestObject.getIdntyNo().size()==0 ){
            List<BizInstructorIdentify> identifyList = bizInstructorIdentifyRepository.findAllByBizInstrIdntyPayYmAndBizInstrIdntyStts(requestObject.getBizInstrIdntyYm(), 4);
            BizInstructorIdentifyApiResponseVO result = BizInstructorIdentifyApiResponseVO.builder().build();
            for(BizInstructorIdentify identify:identifyList){
                identify.setBizInstrIdntyStts(3);
                identify.setBizInstrIdntyPayYm(null);
            }
            BeanUtils.copyProperties(bizInstructorIdentifyRepository.saveAll(identifyList), result);
            return result;
        }else if(!requestObject.getIdntyNo().equals(null) && requestObject.getIdntyNo().size()>0){
            BizInstructorIdentifyApiResponseVO result = BizInstructorIdentifyApiResponseVO.builder().build();
            for(String identifyNo:requestObject.getIdntyNo()){
                BizInstructorIdentify identify = bizInstructorIdentifyRepository.findOne(Example.of(BizInstructorIdentify.builder()
                        .bizInstrIdntyNo(identifyNo)
                        .build()))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3635, "강의확인서 정보 미존재"));
                if(!identify.equals(null)){
                    identify.setBizInstrIdntyStts(3);
                    identify.setBizInstrIdntyPayYm(null);
                    BeanUtils.copyProperties(bizInstructorIdentifyRepository.saveAndFlush(identify),result);
                }
            }
            return result;
        }
        return null;
    }

    /**
     강의확인서 정보 삭제
     */
    public CSResponseVOSupport deleteBizInstructorIdentify(BizInstructorIdentifyApiRequestVO requestObject) {
        if (requestObject.getBizInstrIdntyStts() == 1) {
            bizInstructorIdentifyDtlRepository.deleteAll(bizInstructorIdentifyDtlRepository.findAll(Example.of(BizInstructorIdentifyDtl.builder()
                    .bizInstrIdntyNo(requestObject.getBizInstrIdntyNo())
                    .build())));

            bizInstructorIdentifyRepository.delete(bizInstructorIdentifyRepository.findById(requestObject.getBizInstrIdntyNo())
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3607, "삭제된 강의확인서 입니다.")));

            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        } else {
            return CSResponseVOSupport.of(KPF_RESULT.ERROR3634);
        }
    }

    /**
     강의확인서 리스트
     */
    public <T> Page<T> getBizInstructorIdentifyList(BizInstructorIdentifyViewRequestVO requestObject) {
         return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    public <T> Page<T> getBizInstructorIdentifyList(FormeBizlecinfoApiRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    public <T> Page<T> getBizInstructorIdentifyList(FormeBizlecinfoViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    public  <T> Page<T>  getBizInstructorIdentify(String blciId) {
        FormeBizlecinfoDetailApiRequestVO requestObject = FormeBizlecinfoDetailApiRequestVO.builder().blciId(Long.valueOf(blciId)).build();
        requestObject.setPageable(Pageable.ofSize(1));
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    public <T> Page<T> getBizInstructorIdentifyCalculateSortList(BizInstructorIdentifyCalculateViewRequestVO requestObject) {
        requestObject.setBizInstrAplyStts(2);
        List<BizInstructorAply> bizInstructorAplies = new ArrayList<>((List<BizInstructorAply>) commonBusinessRepository.findEntityList(requestObject).getContent());
        bizInstructorAplies.sort(Comparator.comparing(BizInstructorAply::getBizInstrAplyInstrId));
        return (Page<T>) Optional.ofNullable(CSPageImpl.of(bizInstructorAplies, commonBusinessRepository.findEntityList(requestObject).getPageable(), commonBusinessRepository.findEntityList(requestObject).getTotalElements()))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    public <T> Page<T> getBizInstructorIdentifyCalculateList(BizInstructorIdentifyCalculateViewRequestVO requestObject) {
        requestObject.setBizInstrAplyStts(2);
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    public <T> Page<T> getBizInstructorIdentifyManageList(BizInstructorIdentifyManageViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강의확인서 상세
     */
    public <T> T getBizInstructorIdentify(BizInstructorIdentifyViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    /**
     * 강의확인서 관련 파일 업로드
     *
     * @param bizInstrIdntyNo
     * @param attachFile
     * @param fileType
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String bizInstrIdntyNo, MultipartFile attachFile, String fileType) {
        BizInstructorIdentify bizInstructorIdentify = bizInstructorIdentifyRepository.findOne(Example.of(BizInstructorIdentify.builder()
                        .bizInstrIdntyNo(bizInstrIdntyNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1061, "강의확인서 정보 미존재"));

        String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();
        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getInstrFolder()).toString());

        try {
            /** 파일 저장 및 파일 경로 셋팅 */

            Files.createDirectories(directoryPath);
            if(fileType.equals("atch")){
                Optional.ofNullable(attachFile)
                        .ifPresent(file -> {
                            String idntyLsnFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getInstrFolder())
                                    .append("/")
                                    .append(bizInstrIdntyNo + ATCH_TAG + imageSequence + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".")).toString();
                            try {
                                file.transferTo(new File(idntyLsnFilePath));
                            } catch (IOException e) {
                                throw new KPFException(KPF_RESULT.ERROR1061, "파일 업로드 실패");
                            }
                            bizInstructorIdentify.setBizInstrIdntyAtchFile(idntyLsnFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            bizInstructorIdentify.setBizInstrIdntyAtchFileSize(file.getSize());
                            bizInstructorIdentify.setBizInstrIdntyAtchFileOrgn(file.getOriginalFilename());
                        });
            }else if(fileType.equals("lsn")){
                Optional.ofNullable(attachFile)
                        .ifPresent(file -> {
                            String idntyAtchFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getInstrFolder())
                                    .append("/")
                                    .append(bizInstrIdntyNo + LSN_TAG + imageSequence + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".")).toString();
                            try {
                                file.transferTo(new File(idntyAtchFilePath));
                            } catch (IOException e) {
                                throw new KPFException(KPF_RESULT.ERROR1061, "파일 업로드 실패");
                            }
                            bizInstructorIdentify.setBizInstrIdntyLsnFile(idntyAtchFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            bizInstructorIdentify.setBizInstrIdntyLsnFileSize(file.getSize());
                            bizInstructorIdentify.setBizInstrIdntyLsnFileOrgn(file.getOriginalFilename());
                        });
            }

        } catch (IOException e1) {
            throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
        }
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     엑셀 다운로드
     */
    public <T> List<BizInstructorIdentifyExcelVO> getExcel(BizInstructorIdentifyViewRequestVO requestObject) {
        List<BizInstructorIdentifyExcelVO> bizInstructorIdentifyExcelVOList = (List<BizInstructorIdentifyExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizInstructorIdentifyExcelVOList;
    }
    /**
     엑셀 다운로드
     */
    public <T> List<BizInstructorIdentifyExcelVO> getListCalculateExcel(BizInstructorIdentifyViewRequestVO requestObject) {
        List<BizInstructorIdentifyExcelVO> bizInstructorIdentifyExcelVOList = (List<BizInstructorIdentifyExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizInstructorIdentifyExcelVOList;
    }

    public <T> Page<T> getInstructorStateList(MyInstructorStateViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
}
