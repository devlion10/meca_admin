package kr.or.kpf.lms.biz.user.instructor.service;

import kr.or.kpf.lms.biz.user.history.vo.InstructorLctrViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.history.service.InstructorHistoryService;
import kr.or.kpf.lms.biz.user.instructor.history.vo.request.InstructorHistoryApiRequestVO;
import kr.or.kpf.lms.biz.user.instructor.qualification.service.InstructorQualificationService;
import kr.or.kpf.lms.biz.user.instructor.qualification.vo.request.InstructorQualificationApiRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.*;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorApiResponseVO;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.user.InstructorHist;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.entity.user.InstructorQlfc;
import kr.or.kpf.lms.repository.user.InstructorHistRepository;
import kr.or.kpf.lms.repository.user.InstructorInfoRepository;
import kr.or.kpf.lms.repository.user.InstructorQlfcRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorService extends CSServiceSupport {

    private static final String PICTURE_IMG_TAG = "_PICTURE";
    private static final String SIGN_IMG_TAG = "_SIGN";

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 강사 */
    private final InstructorInfoRepository instructorInfoRepository;
    private final InstructorHistRepository instructorHistRepository;
    private final InstructorQlfcRepository instructorQlfcRepository;
    private final InstructorHistoryService instructorHistoryService;
    private final InstructorQualificationService instructorQualificationService;

    /**
     * 강사 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(InstructorViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강사 정보 생성
     */
    public InstructorApiResponseVO createInfo(InstructorApiRequestVO requestObject) {
        if (requestObject.getUserId() != null && requestObject.getInstrCtgr() != "READER" && instructorInfoRepository.findOne(Example.of(InstructorInfo.builder()
                .userId(requestObject.getUserId())
                .build())).isPresent()) {

            InstructorInfo instructorInfo = instructorInfoRepository.findOne(Example.of(InstructorInfo.builder()
                            .userId(requestObject.getUserId())
                            .build()))
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8104, "존재하지 않는 강사 강사 정보"));

            instructorHistRepository.deleteAll(instructorHistRepository.findAll(Example.of(InstructorHist.builder()
                    .instrSerialNo(instructorInfo.getInstrSerialNo())
                    .build())));

            instructorQlfcRepository.deleteAll(instructorQlfcRepository.findAll(Example.of(InstructorQlfc.builder()
                    .instrSerialNo(instructorInfo.getInstrSerialNo())
                    .build())));

            if (requestObject.getInstructorHistoryApiRequestVOs() != null && requestObject.getInstructorHistoryApiRequestVOs().size() > 0){
                for (InstructorHistoryApiRequestVO item : requestObject.getInstructorHistoryApiRequestVOs()){
                    item.setInstrSerialNo(instructorInfo.getInstrSerialNo());
                    instructorHistoryService.createInfo(item);
                }
            }

            if (requestObject.getInstructorQualificationApiRequestVOs() != null && requestObject.getInstructorQualificationApiRequestVOs().size() > 0){
                for (InstructorQualificationApiRequestVO item : requestObject.getInstructorQualificationApiRequestVOs()){
                    item.setInstrSerialNo(instructorInfo.getInstrSerialNo());
                    instructorQualificationService.createInfo(item);
                }
            }
            InstructorApiResponseVO result = InstructorApiResponseVO.builder().build();
            requestObject.setInstrSerialNo(instructorInfo.getInstrSerialNo());
            copyNonNullObject(requestObject,instructorInfo);
            BeanUtils.copyProperties(instructorInfoRepository.saveAndFlush(instructorInfo), result);
            return result;
        } else {
            InstructorInfo entity = InstructorInfo.builder().build();
            BeanUtils.copyProperties(requestObject, entity);
            InstructorApiResponseVO result = InstructorApiResponseVO.builder().build();
            BeanUtils.copyProperties(instructorInfoRepository.saveAndFlush(entity), result);

            if (result != null && requestObject.getInstructorHistoryApiRequestVOs() != null && requestObject.getInstructorHistoryApiRequestVOs().size() > 0){
                for (InstructorHistoryApiRequestVO item : requestObject.getInstructorHistoryApiRequestVOs()){
                    item.setInstrSerialNo(result.getInstrSerialNo());
                    instructorHistoryService.createInfo(item);
                }
            }

            if (result != null && requestObject.getInstructorQualificationApiRequestVOs() != null && requestObject.getInstructorQualificationApiRequestVOs().size() > 0){
                for (InstructorQualificationApiRequestVO item : requestObject.getInstructorQualificationApiRequestVOs()){
                    item.setInstrSerialNo(result.getInstrSerialNo());
                    instructorQualificationService.createInfo(item);
                }
            }
            return result;
        }
    }

    /**
     * 강사 정보 업데이트
     *
     * @param requestObject
     * @return
     */
    public InstructorApiResponseVO updateInfo(InstructorApiRequestVO requestObject) {
        return instructorInfoRepository.findOne(Example.of(InstructorInfo.builder()
                        .instrSerialNo(requestObject.getInstrSerialNo())
                        .build()))
                .map(InstructorInfo -> {
                    InstructorApiResponseVO result = InstructorApiResponseVO.builder().build();
                    copyNonNullObject(requestObject, InstructorInfo);
                    BeanUtils.copyProperties(instructorInfoRepository.saveAndFlush(InstructorInfo), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 강사 정보 미존재"));
    }

    /**
     * 강사 정보 관련 파일 업로드
     *
     * @param instrSerialNo
     * @param pictureFile
     * @param signFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(Long instrSerialNo, MultipartFile pictureFile, MultipartFile signFile) {
        /** 강사 정보 신청 확인 */
        InstructorInfo instructorInfo = instructorInfoRepository.findOne(Example.of(InstructorInfo.builder()
                        .instrSerialNo(instrSerialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1061, "수정 가능한 강사 정보 미존재"));

        String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();
        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getInstrFolder()).toString());

        try {
            /** 파일 저장 및 파일 경로 셋팅 */
            Files.createDirectories(directoryPath);
            Optional.ofNullable(pictureFile)
                    .ifPresent(file -> {
                        String pictureFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                .append(appConfig.getUploadFile().getInstrFolder())
                                .append("/")
                                .append(instrSerialNo + PICTURE_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                        try {
                            file.transferTo(new File(pictureFilePath));
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR1061, "파일 업로드 실패");
                        }
                        instructorInfo.setInstrPctr(pictureFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                        instructorInfo.setInstrPctrSize(file.getSize());
                    });

            Optional.ofNullable(signFile)
                    .ifPresent(file -> {
                        String signFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                .append(appConfig.getUploadFile().getInstrFolder())
                                .append("/")
                                .append(instrSerialNo + SIGN_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                        try {
                            file.transferTo(new File(signFilePath));
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR1061, "파일 업로드 실패");
                        }
                        instructorInfo.setInstrSgntr(signFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                        instructorInfo.setInstrSgntrSize(file.getSize());
                    });
        } catch (IOException e1) {
            throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
        }
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     강사정보 엑셀 다운로드
     */
    public <T> List<InstructorExcelVO> getExcel(InstructorCustomViewRequestVO requestObject) {
        List<InstructorExcelVO> instructorExcelVOList = (List<InstructorExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return instructorExcelVOList;
    }

    /**
     * 강의이력
     */
    public <T> Page<T> getCareer(InstructorCareerRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));

    }

    /**
     * 언론인교육센터 목록
     */
    public <T> Page<T> getJournaliLctrList(InstructorLctrApiRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));

    }

    public <T> Page<T> getJournaliLctrList(InstructorLctrViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));

    }

}
