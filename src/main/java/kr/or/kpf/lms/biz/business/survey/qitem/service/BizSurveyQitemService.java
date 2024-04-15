package kr.or.kpf.lms.biz.business.survey.qitem.service;

import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemApiRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizSurveyMasterRepository;
import kr.or.kpf.lms.repository.BizSurveyQitemItemRepository;
import kr.or.kpf.lms.repository.BizSurveyQitemRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizSurveyMaster;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizSurveyQitemService extends CSServiceSupport {

    private static final String PREFIX_SQITEM_IDENTIFY = "BSI";
    private static final String PREFIX_SQITEMITEM_IDENTIFY = "BSII";
    private final CommonBusinessRepository commonBusinessRepository;

    private final BizSurveyQitemRepository bizSurveyQitemRepository;
    private final BizSurveyQitemItemRepository bizSurveyQitemItemRepository;
    private final BizSurveyMasterRepository bizSurveyMasterRepository;

    /**
     상호평가 정보 생성
     */
    public BizSurveyQitemApiResponseVO createBizSurveyQitem(BizSurveyQitemApiRequestVO requestObject) {
        BizSurveyQitem entity = BizSurveyQitem.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizSurveyQitemNo(commonBusinessRepository.generateCode(PREFIX_SQITEM_IDENTIFY));
        BizSurveyQitemApiResponseVO result = BizSurveyQitemApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizSurveyQitemRepository.saveAndFlush(entity), result);
        if(!result.equals(null) && !requestObject.getBizSurveyQitemItems().isEmpty()){
            List<BizSurveyQitemItem> resultItem = new ArrayList<>();
            for(BizSurveyQitemItem item : requestObject.getBizSurveyQitemItems()){
                item.setBizSurveyQitemNo(result.getBizSurveyQitemNo());
                item.setBizSurveyQitemItemNo(commonBusinessRepository.generateCode(PREFIX_SQITEMITEM_IDENTIFY));
                resultItem.add(bizSurveyQitemItemRepository.saveAndFlush(item));
            }
            result.setBizSurveyQitemItems(resultItem);
        }
        return result;
    }

    /**
     상호평가 정보 업데이트
     */
    public BizSurveyQitemApiResponseVO updateBizSurveyQitem(BizSurveyQitemApiRequestVO requestObject) {
        return bizSurveyQitemRepository.findOne(Example.of(BizSurveyQitem.builder()
                        .bizSurveyQitemNo(requestObject.getBizSurveyQitemNo())
                        .build()))
                .map(bizSurveyQitem -> {
                    List<BizSurveyQitemItem> resultItem = requestObject.getBizSurveyQitemItems();
                    if(resultItem.size()>0) {
                        bizSurveyQitemItemRepository.deleteAll(bizSurveyQitemItemRepository.findAll(Example.of(BizSurveyQitemItem.builder()
                                        .bizSurveyQitemNo(requestObject.getBizSurveyQitemNo())
                                        .build())));
                        for (BizSurveyQitemItem item : resultItem) {
                            if(!item.equals(null)){
                                item.setBizSurveyQitemItemNo(commonBusinessRepository.generateCode(PREFIX_SQITEMITEM_IDENTIFY));
                                bizSurveyQitemItemRepository.saveAndFlush(item);
                            }
                        }
                    }
                    copyNonNullObject(requestObject, bizSurveyQitem);
                    BizSurveyQitemApiResponseVO result = BizSurveyQitemApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizSurveyQitemRepository.saveAndFlush(bizSurveyQitem), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3585, "상호평가 문항 업데이트 오류"));
    }

    /**
     상호평가 정보 삭제
     */
    public CSResponseVOSupport deleteBizSurveyQitem(BizSurveyQitemApiRequestVO requestObject) {
        if(requestObject.getBizSurveyQitemItems().size()>0) {
            bizSurveyQitemItemRepository.deleteAll(bizSurveyQitemItemRepository.findAll(Example.of(BizSurveyQitemItem.builder()
                    .bizSurveyQitemNo(requestObject.getBizSurveyQitemNo())
                    .build())));
        }
        bizSurveyMasterRepository.deleteAll(bizSurveyMasterRepository.findAll(Example.of(BizSurveyMaster.builder()
                .bizSurveyQitemNo(requestObject.getBizSurveyQitemNo())
                .build())));
        bizSurveyQitemRepository.delete(bizSurveyQitemRepository.findOne(Example.of(BizSurveyQitem.builder()
                        .bizSurveyQitemNo(requestObject.getBizSurveyQitemNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3587, "삭제된 상호평가 문항 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
    /**
     상호평가 정보 삭제
     */
    public CSResponseVOSupport deleteBizSurveyQitemItem(String bizSurveyQitemItemNo) {
        bizSurveyQitemItemRepository.delete(bizSurveyQitemItemRepository.findOne(Example.of(BizSurveyQitemItem.builder()
                        .bizSurveyQitemItemNo(bizSurveyQitemItemNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3587, "삭제된 상호평가 문항 아이템입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
    /**
     상호평가 리스트
     */
    public <T> Page<T> getBizSurveyQitemList(BizSurveyQitemViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     상호평가 상세
     */
    public <T> T getBizSurvey(BizSurveyQitemViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    /**
     엑셀 다운로드
     */
    public <T> List<BizSurveyQitemExcelVO> getExcel(BizSurveyQitemViewRequestVO requestObject) {
        List<BizSurveyQitemExcelVO> bizSurveyQitemExcelVOList = (List<BizSurveyQitemExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizSurveyQitemExcelVOList;
    }

}
