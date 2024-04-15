package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;

import java.util.List;

/**
 * 공통 Repository
 */
public interface CommonRepository {

    List<CommonCodeMaster> findTopCommonCode(CommonCodeViewRequestVO requestObject);
}
