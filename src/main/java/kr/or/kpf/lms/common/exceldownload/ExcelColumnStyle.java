package kr.or.kpf.lms.common.exceldownload;

import kr.or.kpf.lms.common.exceldownload.style.ExcelCellStyle;

public @interface ExcelColumnStyle {

    Class<? extends ExcelCellStyle> excelCellStyleClass();

    String enumName() default "";

}
