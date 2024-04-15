package kr.or.kpf.lms.common.exceldownload.style;

import org.apache.poi.ss.usermodel.CellStyle;

public interface ExcelCellStyle {
    void apply(CellStyle cellStyle);
}
