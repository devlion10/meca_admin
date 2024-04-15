package kr.or.kpf.lms.common.exceldownload.exception;

import kr.or.kpf.lms.common.exceldownload.ExcelException;

public class NoExcelColumnAnnotationsException extends ExcelException {

    public NoExcelColumnAnnotationsException(String message) {
        super(message, null);
    }

}
