package kr.or.kpf.lms.biz.homepage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 홈페이지 관리 > 게시판 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage")
public class HomepageViewController {

    private static final String BOARD = "views/homepage/";

    /**
     * 홈페이지 관리 > 게시판 관리 조회
     */
    @GetMapping(path={""})
    public String getList() {
        return new StringBuilder(BOARD).append("board").toString();
    }

    /**
     * 홈페이지 관리 > 게시판 관리 등록
     */
    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(BOARD).append("boardForm").toString();
    }

    /**
     * 홈페이지 관리 > 게시판 관리 상세 및 수정
     */
    @GetMapping(path={"/{No}"})
    public String getForm() {
        return new StringBuilder(BOARD).append("boardForm").toString();
    }
}
