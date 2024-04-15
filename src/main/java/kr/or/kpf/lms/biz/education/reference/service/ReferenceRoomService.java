package kr.or.kpf.lms.biz.education.reference.service;

import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationExcelVO;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionApiRequestVOS;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomApiRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomViewRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomApiResponseVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.education.CurriculumMasterRepository;
import kr.or.kpf.lms.repository.education.CurriculumReferenceRoomRepository;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumReferenceRoom;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionMaster;
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
import java.util.List;
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ReferenceRoomService extends CSServiceSupport {

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    /** 교육과정 관리 */
    private final CurriculumMasterRepository curriculumMasterRepository;
    /** 자료실 관리 */
    private final CurriculumReferenceRoomRepository curriculumReferenceRoomRepository;

    /**
     * 자료실 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ReferenceRoomViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 특정 교육 과정 조회
     *
     * @param curriculumCode
     * @return
     */
    public CurriculumMaster getCurriculumInfo(String curriculumCode) {
        return curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(curriculumCode).build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "존재하지 않는 교육과정"));
    }

    /**
     * 자료실 생성
     *
     * @param referenceRoomApiRequestVO
     * @param referenceRoomFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ReferenceRoomApiResponseVO createReferenceRoom(ReferenceRoomApiRequestVO referenceRoomApiRequestVO, MultipartFile referenceRoomFile) {
        CurriculumReferenceRoom entity = CurriculumReferenceRoom.builder().build();
        copyNonNullObject(referenceRoomApiRequestVO, entity);

        ReferenceRoomApiResponseVO result = ReferenceRoomApiResponseVO.builder().build();
        BeanUtils.copyProperties(uploadFile(entity, referenceRoomFile), result);

        return result;
    }

    /**
     * 자료실 업데이트
     *
     * @param referenceRoomApiRequestVO
     * @param referenceRoomFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ReferenceRoomApiResponseVO updateReferenceRoom(ReferenceRoomApiRequestVO referenceRoomApiRequestVO, MultipartFile referenceRoomFile) {
        CurriculumReferenceRoom curriculumReferenceRoom = curriculumReferenceRoomRepository.findOne(Example.of(CurriculumReferenceRoom.builder()
                .sequenceNo(referenceRoomApiRequestVO.getSequenceNo())
                .build())).orElseThrow(() -> new RuntimeException("수정 대상 자료실 정보가 존재하지 않습니다."));

        copyNonNullObject(referenceRoomApiRequestVO, curriculumReferenceRoom);

        if(referenceRoomApiRequestVO.getIsDeleteFile()) {
            curriculumReferenceRoom.setFileSize(null);
            curriculumReferenceRoom.setFilePath(null);
        }

        ReferenceRoomApiResponseVO result = ReferenceRoomApiResponseVO.builder().build();
        BeanUtils.copyProperties(uploadFile(curriculumReferenceRoom, referenceRoomFile), result);
        return result;
    }

    /**
     * 자료실 정보 삭제
     *
     * @param referenceRoomApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteInfo(ReferenceRoomApiRequestVO referenceRoomApiRequestVO) {
        /** 자료실 정보 삭제 */
        curriculumReferenceRoomRepository.delete(curriculumReferenceRoomRepository.findOne(Example.of(CurriculumReferenceRoom.builder()
                        .sequenceNo(referenceRoomApiRequestVO.getSequenceNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3022, "삭제된 자료 정보 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }


    /**
     엑셀 다운로드
     */
    public <T> List<ReferenceRoomExcelVO> getExcel(ReferenceRoomViewRequestVO requestObject) {
        List<ReferenceRoomExcelVO> referenceRoomExcelVOList = (List<ReferenceRoomExcelVO>) commonEducationRepository.excelDownload(requestObject);
        return referenceRoomExcelVOList;
    }

    /**
     * 자료실 첨부 파일 업로드 및 데이터 처리
     *
     * @param curriculumReferenceRoom
     * @param referenceRoomFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CurriculumReferenceRoom uploadFile(CurriculumReferenceRoom curriculumReferenceRoom, MultipartFile referenceRoomFile) {
        /** 파일 저장 및 파일 경로 셋팅 */
        return Optional.ofNullable(referenceRoomFile)
                .map(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                            .append(appConfig.getUploadFile().getReferenceFolder())
                            .append("/")
                            .append(curriculumReferenceRoom.getCurriculumCode()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String fileSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String filePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getReferenceFolder())
                                    .append("/")
                                    .append(curriculumReferenceRoom.getCurriculumCode())
                                    .append("/")
                                    .append("자료실 첨부파일" + "_" + fileSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(filePath));
                            curriculumReferenceRoom.setFilePath(filePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            curriculumReferenceRoom.setFileSize(file.getSize());
                            curriculumReferenceRoom.setFileOriginName(file.getOriginalFilename());
                            return curriculumReferenceRoomRepository.saveAndFlush(curriculumReferenceRoom);
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9004, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "파일 경로 확인 또는, 생성 실패");
                    }
                }).orElseGet(() -> curriculumReferenceRoomRepository.saveAndFlush(curriculumReferenceRoom));
    }
}
