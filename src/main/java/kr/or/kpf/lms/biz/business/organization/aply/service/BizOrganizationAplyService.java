package kr.or.kpf.lms.biz.business.organization.aply.service;

import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyDistExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.*;
import kr.or.kpf.lms.repository.entity.*;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class BizOrganizationAplyService extends CSServiceSupport {

    private static final String ORGANIZATION_APLY_FILE_TAG = "ORGANIZATION_APLY";

    private static final String PREFIX_ORGAPLY = "BOA";

    private final CommonBusinessRepository commonBusinessRepository;
    private final BizPbancMasterRepository bizPbancMasterRepository;
    private final BizOrganizationAplyRepository bizOrganizationAplyRepository;
    private final BizOrganizationAplyDtlRepository bizOrganizationAplyDtlRepository;
    private final BizEditHistRepository bizEditHistRepository;

    /** 사업 신청 개수 제한 */
    public boolean vailedBizOrganizationAply(BizOrganizationAplyApiRequestVO requestObject){
        if (requestObject.getBizOrgAplyStts() == 2) {
            BizPbancMaster bizPbancMaster = bizPbancMasterRepository.findById(requestObject.getBizPbancNo())
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3505, "존재하지 않는 사업 공고 입니다."));

            Long aplyCnt = commonBusinessRepository.countEntity(requestObject);
            if (aplyCnt + 1 < bizPbancMaster.getBizPbancMaxInst()){
                return true;
            }
            return false;
        } else return true;
    }

    /**
     사업 공고 신청 정보 생성
     */
    public BizOrganizationAplyApiResponseVO createBizOrganizationAply(BizOrganizationAplyApiRequestVO requestObject) {
        BizOrganizationAply entity = BizOrganizationAply.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizOrgAplyNo(commonBusinessRepository.generateCode(PREFIX_ORGAPLY));
        BizOrganizationAplyApiResponseVO result = BizOrganizationAplyApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizOrganizationAplyRepository.saveAndFlush(entity), result);

        return result;
    }

    /**
     사업 공고 신청 정보 업데이트
     */
    public BizOrganizationAplyApiResponseVO updateBizOrganizationAply(BizOrganizationAplyApiRequestVO requestObject) {
        return bizOrganizationAplyRepository.findById(requestObject.getBizOrgAplyNo())
                .map(bizOrganizationAply -> {
                    copyNonNullObject(requestObject, bizOrganizationAply);
                    BizOrganizationAplyApiResponseVO result = BizOrganizationAplyApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizOrganizationAplyRepository.saveAndFlush(bizOrganizationAply), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3545, "해당 사업 공고 신청 미존재"));
    }

    /**
     사업 공고 신청 정보 삭제
     */
    public CSResponseVOSupport deleteBizOrganizationAply(BizOrganizationAplyApiRequestVO requestObject) {
        bizOrganizationAplyRepository.delete(bizOrganizationAplyRepository.findById(requestObject.getBizOrgAplyNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3547, "삭제된 사업 공고 신청 입니다.")));

        bizOrganizationAplyDtlRepository.deleteAll(bizOrganizationAplyDtlRepository.findAll(Example.of(BizOrganizationAplyDtl.builder()
                .bizOrgAplyNo(requestObject.getBizOrgAplyNo())
                .build())));

        if (bizEditHistRepository.findAll(Example.of(BizEditHist.builder()
                .bizEditHistTrgtNo(requestObject.getBizOrgAplyNo())
                .build())).size() > 0) {
            bizEditHistRepository.deleteAll(bizEditHistRepository.findAll(Example.of(BizEditHist.builder()
                    .bizEditHistTrgtNo(requestObject.getBizOrgAplyNo())
                    .build())));
        }

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     사업 공고 신청 리스트(page)
     */
    public <T> Page<T> getBizOrganizationAplyList(BizOrganizationAplyViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     사업 공고 신청 강사 리스트
     */
    public <T> Page<T> getBizOrganizationAplyCustomList(BizOrganizationAplyCustomViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     사업 공고 신청 리스트(List)
     */
    public List<BizOrganizationAply> getBizOrganizationAplies(BizOrganizationAplyViewRequestVO requestObject) {
        return (List<BizOrganizationAply>) commonBusinessRepository.findEntityList(requestObject).getContent();
    }

    /**
     사업 공고 신청 상세
     */
    public <T> T getBizOrganizationAply(BizOrganizationAplyViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    /**
     * 사업 공고 신청 신청서 업로드
     *
     * @param bizOrgAplyNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String bizOrgAplyNo, MultipartFile attachFile) {
        /** 공지사항 이력 확인 */
        BizOrganizationAply bizOrganizationAply = bizOrganizationAplyRepository.findOne(Example.of(BizOrganizationAply.builder()
                        .bizOrgAplyNo(bizOrgAplyNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3545, "대상 사업 공고 신청 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getBizOrganizationAplyFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getBizOrganizationAplyFolder())
                                    .append("/")
                                    .append(ORGANIZATION_APLY_FILE_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            bizOrganizationAply.setBizOrgAplyFile(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            bizOrganizationAply.setBizOrgAplyFileSize(file.getSize());
                            bizOrganizationAply.setBizOrgAplyFileOrigin(file.getOriginalFilename());
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
     사업 공고 신청 엑셀 다운로드
     */
    public <T> List<BizOrganizationAplyExcelVO> getExcel(BizOrganizationAplyViewRequestVO requestObject) {
        requestObject.setIsDist(0);
        List<BizOrganizationAplyExcelVO> bizOrganizationAplyExcelVOList = (List<BizOrganizationAplyExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizOrganizationAplyExcelVOList;
    }

    public <T> List<BizOrganizationAplyDistExcelVO> getDistExcel(BizOrganizationAplyViewRequestVO requestObject) {
        requestObject.setIsDist(1);
        List<BizOrganizationAplyDistExcelVO> bizOrganizationAplyExcelVOList = (List<BizOrganizationAplyDistExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizOrganizationAplyExcelVOList;
    }
}