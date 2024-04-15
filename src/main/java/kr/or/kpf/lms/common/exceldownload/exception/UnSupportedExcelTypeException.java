package kr.or.kpf.lms.common.exceldownload.exception;

import kr.or.kpf.lms.common.exceldownload.ExcelException;

public class UnSupportedExcelTypeException extends ExcelException {

    public UnSupportedExcelTypeException(String message) {
        super(message, null);
    }

}