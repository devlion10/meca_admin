package kr.or.kpf.lms.biz.user.instructor.history.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.user.instructor.service.InstructorService;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 강사 관리 > 강사 관리 API 관련 Controller
 */
@Tag(name = "Instructor Management", description = "강사 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/instructor/history")
public class InstructorHistoryApiController extends CSApiControllerSupport {
    public interface CreateInstructor {}
    public interface UpdateInstructor {}
}