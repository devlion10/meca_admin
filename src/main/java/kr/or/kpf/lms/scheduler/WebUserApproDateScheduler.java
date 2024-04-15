package kr.or.kpf.lms.scheduler;

import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 언론인 승인 유효기간 스케쥴러
 */
@Component
@RequiredArgsConstructor
public class WebUserApproDateScheduler {
    private static final Logger logger = LoggerFactory.getLogger(WebUserApproDateScheduler.class);
    private final LmsUserRepository lmsUserRepository;

    /** 0시 1분 0초 마다 반복*/
    @Scheduled(cron = "0 1 0 * * *")
    public void ebUserApproDateCheck() {
        logger.info("=============================== LMS 언론인 회원 승인유효일자 검사 시작 ===============================");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        List<LmsUser> userList = lmsUserRepository.findByApproFlagDateIsNotNull();
        for(LmsUser data: userList){
            if(data.getApproFlag().equals("Y")){
                try {
                    /** 오늘이 승인유효일자를 지났으면 승인상태 변경 */
                    Date date = sdf.parse(data.getApproFlagDate());
                    if(today.after(date)){
                        data.setApproFlag("N");
                        data.setApproFlagDate(null);
                        lmsUserRepository.saveAndFlush(data);
                    }
                } catch (ParseException e) {
                    throw new KPFException(KPF_RESULT.ERROR9999, "승인유효일자 파싱 실패.");
                }
            }else{
                /** 승인 상태가 아닌데 승인유효일자가 있는 경우 유효일자 삭제 */
                data.setApproFlagDate(null);
                lmsUserRepository.saveAndFlush(data);
            }
        }
        logger.info("=============================== LMS 언론인 회원 승인유효일자 검사 종료 ===============================");

    }


}
