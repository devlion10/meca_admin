package kr.or.kpf.lms.scheduler;

import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.statistics.WebUserStateHistorySummary;
import kr.or.kpf.lms.repository.statistics.WebUserStateHistorySummaryRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 웹 회원 상태 집계 스케쥴러
 */
@Component
@RequiredArgsConstructor
public class WebUserSummaryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(WebUserSummaryScheduler.class);

    private final CommonUserRepository commonUserRepository;
    private final WebUserStateHistorySummaryRepository webUserStateHistorySummaryRepository;

    @Scheduled(cron = "0 30 * * * *")
    public void webUserStateSummary() {
        logger.info("=============================== LMS 회원 상태 집계 Scheduler 시작 ===============================");
        String today = new DateTime().toString("yyyy-MM-dd");
        String yesterday = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        webUserStateHistorySummaryRepository.findOne(Example.of(WebUserStateHistorySummary.builder()
                                                                    .summaryDate(yesterday.replace("-", ""))
                                                                    .build())).map(data -> {
            try {
                if (new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getUpdateDateTime())).equals(today)) {
                    /** 조회 기준 하루 전 집계 데이터가 오늘 업데이트가 되었을 경우... 더 이상 업데이트 필요 없음. */
                    return data;
                } else {
                    WebUserStateHistorySummary imsiData = webUserStateHistorySummary(yesterday, data);
                    return webUserStateHistorySummaryRepository.saveAndFlush(imsiData);
                }
            } catch (ParseException e) {
                throw new KPFException(KPF_RESULT.ERROR9999, "LMS 회원 상태 집계 프로시저 오류 발생.");
            }
        }).orElseGet(() -> webUserStateHistorySummaryRepository.saveAndFlush(webUserStateHistorySummary(yesterday, WebUserStateHistorySummary.builder()
                .summaryDate(yesterday.replace("-", ""))
                .build())));

        webUserStateHistorySummaryRepository.findOne(Example.of(WebUserStateHistorySummary.builder()
                                                                    .summaryDate(today.replace("-", ""))
                                                                    .build()))
                .map(data -> webUserStateHistorySummaryRepository.saveAndFlush(webUserStateHistorySummary(today, data)))
                .orElseGet(() -> webUserStateHistorySummaryRepository.saveAndFlush(webUserStateHistorySummary(today, WebUserStateHistorySummary.builder().summaryDate(today.replace("-", "")).build())));

        logger.info("=============================== LMS 회원 상태 집계 Scheduler 종료 ===============================");

    }

    public WebUserStateHistorySummary webUserStateHistorySummary(String day, WebUserStateHistorySummary imsiData) {

        PageRequest pageRequest = PageRequest.of(0, 10);

        /** 신규 가입 회원 수 */
        WebUserViewRequestVO joinUser = new WebUserViewRequestVO();
        joinUser.setJoinDate(day);
        joinUser.setStartDate(day);
        joinUser.setEndDate(day);
        joinUser.setPageable(pageRequest);
        long joinUserCount = commonUserRepository.findEntityList(joinUser).getTotalElements();
        imsiData.setJoinCount(joinUserCount);

        /** 탈퇴 회원 수 */
        WebUserViewRequestVO withDrawUser = new WebUserViewRequestVO();
        withDrawUser.setState(Code.USER_STATE.WITHDRAWAL.enumCode);
        withDrawUser.setWithDrawDate(day);
        withDrawUser.setPageable(pageRequest);
        long withDrawUserCount = commonUserRepository.findEntityList(withDrawUser).getTotalElements();
        imsiData.setWithDrawalCount(withDrawUserCount);

        /** 휴면 계정 전환 회원 수 */
        WebUserViewRequestVO dormancyUser = new WebUserViewRequestVO();
        dormancyUser.setState(Code.USER_STATE.DORMANCY.enumCode);
        dormancyUser.setDormancyDate(day);
        dormancyUser.setPageable(pageRequest);
        long dormancyUserCount = commonUserRepository.findEntityList(dormancyUser).getTotalElements();
        imsiData.setDormancyCount(dormancyUserCount);

        /** 접속 회원 수 */
        WebUserViewRequestVO loginUser = new WebUserViewRequestVO();
        loginUser.setLastLoginDate(day);
        loginUser.setStartDate(day);
        loginUser.setEndDate(day);
        loginUser.setPageable(pageRequest);
        long loginUserCount = commonUserRepository.findEntityList(loginUser).getTotalElements();
        imsiData.setLoginCount(loginUserCount);

        return imsiData;
    }
}
