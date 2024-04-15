package kr.or.kpf.lms.biz.business.organization.aply.dtl.service;

import kr.or.kpf.lms.biz.business.organization.aply.dtl.vo.request.BizOrganizationAplyDtlApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.dtl.vo.request.BizOrganizationAplyDtlViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.dtl.vo.response.BizOrganizationAplyDtlApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.service.BizEditHistService;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.vo.request.BizEditHistViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.BizInstructorIdentifyDtlRepository;
import kr.or.kpf.lms.repository.BizOrganizationAplyDtlRepository;
import kr.or.kpf.lms.repository.BizOrganizationAplyRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentifyDtl;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import kr.or.kpf.lms.repository.entity.BizOrganizationAplyDtl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BizOrganizationAplyDtlService extends CSServiceSupport {

    private static final String PREFIX_ORGAPLY_DTL = "BOAD";

    private final CommonBusinessRepository commonBusinessRepository;
    private final BizOrganizationAplyRepository bizOrganizationAplyRepository;
    private final BizOrganizationAplyDtlRepository bizOrganizationAplyDtlRepository;
    private final BizInstructorIdentifyDtlRepository bizInstructorIdentifyDtlRepository;
    private final BizEditHistService bizEditHistService;

    /**
     강사 모집 신청 상세
     */
    public <T> T getBizInstructorAplyDtl(BizOrganizationAplyDtlViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    public BizOrganizationAplyDtlApiResponseVO createBizOrganizationAplyDtl(BizOrganizationAplyDtlApiRequestVO requestObject) {
        BizOrganizationAplyDtl entity = BizOrganizationAplyDtl.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizOrgAplyDtlNo(commonBusinessRepository.generateCode(PREFIX_ORGAPLY_DTL));
        BizOrganizationAplyDtlApiResponseVO result = BizOrganizationAplyDtlApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizOrganizationAplyDtlRepository.saveAndFlush(entity), result);

        if (result != null){
            BizOrganizationAply bizOrganizationAply = bizOrganizationAplyRepository.findByBizOrgAplyNo(entity.getBizOrgAplyNo());
            if (bizOrganizationAply != null && (bizOrganizationAply.getBizOrgAplyStts() == 2 || bizOrganizationAply.getBizOrgAplyStts() == 9)) {
                String after = entity.getBizOrgAplyLsnDtlYmd() + " " +
                        entity.getBizOrgAplyLsnDtlBgngTm().substring(0,entity.getBizOrgAplyLsnDtlYmd().length()-5) + " ~ " +
                        entity.getBizOrgAplyLsnDtlEndTm().substring(0,entity.getBizOrgAplyLsnDtlYmd().length()-5) + " (" + entity.getBizOrgAplyLsnDtlHr() + ") ";

                bizEditHistService.addBizEditHist(entity.getBizOrgAplyNo(), 1, "", after, 0);
            }
        }

        return result;
    }

    /**
     사업 공고 신청 수업계획서(복수) 정보 생성
     */
    public BizOrganizationAplyDtlApiResponseVO createBizOrganizationAplyDtls(List<BizOrganizationAplyDtlApiRequestVO> requestObjects) {

        BizOrganizationAplyDtlApiResponseVO result = BizOrganizationAplyDtlApiResponseVO.builder().build();
        for (BizOrganizationAplyDtlApiRequestVO requestObject : requestObjects){
            BizOrganizationAplyDtl entity = BizOrganizationAplyDtl.builder().build();
            BeanUtils.copyProperties(requestObject, entity);
            entity.setBizOrgAplyDtlNo(commonBusinessRepository.generateCode(PREFIX_ORGAPLY_DTL));
            BeanUtils.copyProperties(bizOrganizationAplyDtlRepository.saveAndFlush(entity), result);
        }
        return result;
    }

    /**
     사업 공고 신청 수업계획서 정보 업데이트
     */
    public BizOrganizationAplyDtlApiResponseVO updateBizOrganizationAplyDtl(BizOrganizationAplyDtlApiRequestVO requestObject) {
        BizOrganizationAply bizOrganizationAply = bizOrganizationAplyRepository.findByBizOrgAplyNo(requestObject.getBizOrgAplyNo());
        BizOrganizationAplyDtlApiResponseVO result = BizOrganizationAplyDtlApiResponseVO.builder().build();

        if (bizInstructorIdentifyDtlRepository.findAll(Example.of(BizInstructorIdentifyDtl.builder().bizOrgAplyDtlNo(requestObject.getBizOrgAplyDtlNo()).build())).size() == 0) {
            if (requestObject != null) {
                if (bizOrganizationAply.getBizOrgAplyStts() == 2 || bizOrganizationAply.getBizOrgAplyStts() == 9) {
                    return bizOrganizationAplyDtlRepository.findBizOrganizationAplyDtlByBizOrgAplyDtlNo(requestObject.getBizOrgAplyDtlNo())
                            .map(bizOrganizationAplyDtl -> {
                                String oldBizOrgAplyLsnDtlYmd = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd();
                                String oldBizOrgAplyLsnDtlBgngTm = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlBgngTm().substring(0,bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd().length()-5);
                                String oldBizOrgAplyLsnDtlEndTm = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlEndTm().substring(0,bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd().length()-5);
                                Integer oldBizOrgAplyLsnDtlHr = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlHr();

                                copyNonNullObject(requestObject, bizOrganizationAplyDtl);
                                BeanUtils.copyProperties(bizOrganizationAplyDtlRepository.saveAndFlush(bizOrganizationAplyDtl), result);

                                String before = oldBizOrgAplyLsnDtlYmd + " " +
                                        oldBizOrgAplyLsnDtlBgngTm + " ~ " +
                                        oldBizOrgAplyLsnDtlEndTm + " (" + oldBizOrgAplyLsnDtlHr + ") ";

                                String after = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd() + " " +
                                        bizOrganizationAplyDtl.getBizOrgAplyLsnDtlBgngTm() + " ~ " +
                                        bizOrganizationAplyDtl.getBizOrgAplyLsnDtlEndTm() + " (" + bizOrganizationAplyDtl.getBizOrgAplyLsnDtlHr() + ") ";

                                if (!before.equals(after))
                                    bizEditHistService.addBizEditHist(bizOrganizationAplyDtl.getBizOrgAplyNo(), 2, before, after, 0);

                                return result;
                            }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3565, "해당 사업 공고 수업계획서 신청 미존재"));
                } else {
                    return bizOrganizationAplyDtlRepository.findBizOrganizationAplyDtlByBizOrgAplyDtlNo(requestObject.getBizOrgAplyDtlNo())
                            .map(bizOrganizationAplyDtl -> {
                                copyNonNullObject(requestObject, bizOrganizationAplyDtl);
                                BeanUtils.copyProperties(bizOrganizationAplyDtlRepository.saveAndFlush(bizOrganizationAplyDtl), result);

                                return result;
                            }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3565, "해당 사업 공고 수업계획서 신청 미존재"));
                }
            } else return null;
        } else return null;
    }

    /**
     사업 공고 신청 수업계획서 정보 삭제
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteBizOrganizationAplyDtl(BizOrganizationAplyDtlApiRequestVO requestObject) {
        /** 저장 시 BizEditHist 정보 추가 */
        if (requestObject != null){
            BizOrganizationAplyDtl bizOrganizationAplyDtl = bizOrganizationAplyDtlRepository.findOne(Example.of(BizOrganizationAplyDtl.builder()
                            .bizOrgAplyDtlNo(requestObject.getBizOrgAplyDtlNo())
                            .bizOrgAplyNo(requestObject.getBizOrgAplyNo())
                            .build()))
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3567, "이미 삭제된 수업 계획 정보입니다."));

            if (bizOrganizationAplyDtl != null) {
                BizOrganizationAply bizOrganizationAply = bizOrganizationAplyRepository.findByBizOrgAplyNo(bizOrganizationAplyDtl.getBizOrgAplyNo());
                if (bizOrganizationAply != null && (bizOrganizationAply.getBizOrgAplyStts() == 2 || bizOrganizationAply.getBizOrgAplyStts() == 9)) {
                    String before = bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd() + " " +
                            bizOrganizationAplyDtl.getBizOrgAplyLsnDtlBgngTm().substring(0,bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd().length()-5) + " ~ " +
                            bizOrganizationAplyDtl.getBizOrgAplyLsnDtlEndTm().substring(0,bizOrganizationAplyDtl.getBizOrgAplyLsnDtlYmd().length()-5) + " (" + bizOrganizationAplyDtl.getBizOrgAplyLsnDtlHr() + ") ";

                    bizEditHistService.addBizEditHist(bizOrganizationAplyDtl.getBizOrgAplyNo(), 0, before, "", 0);
                }
            }
            bizOrganizationAplyDtlRepository.delete(bizOrganizationAplyDtl);
            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        } else return null;
    }


    /**
     사업 공고 상세 보기
     */
}