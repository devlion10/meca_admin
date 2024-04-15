package kr.or.kpf.lms.biz.common.service;

import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;

/**
 * 공통 Service
 */
@Service
@RequiredArgsConstructor
public class CommonService extends CSServiceSupport {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String MAIL_TEMPLATE = "views/mail-template/";

    /**
     * 이메일 발송
     */
    public CSResponseVOSupport sendMail() {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            /** 메일 제목 설정 */
            mimeMessageHelper.setSubject("[메카] 메일 제목");
            /** 수신자 메일 주소 */
            mimeMessageHelper.setTo("");
            /** 템플릿에 전달할 데이터 설정 */
            Context context = new Context();
            /** 템플릿에 전달할 데이터 셋팅 */
            HashMap<String, String> emailValues = new HashMap<>();
            emailValues.put("name", "meca");
            emailValues.forEach(context::setVariable);
            /** 메일 내용 설정 : 템플릿 프로세스 */
            String html = templateEngine.process(MAIL_TEMPLATE + "mailSample", context);
            mimeMessageHelper.setText(html, true);
            /** 메일 보내기 */
            javaMailSender.send(mimeMessage);

            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        } catch (Exception e) {
            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
            throw new RuntimeException("이메일 전송 실패");
        }
    }
}
