package kr.or.kpf.lms.common.exceldownload.style;

import kr.or.kpf.lms.common.exceldownload.style.align.DefaultExcelAlign;
import kr.or.kpf.lms.common.exceldownload.style.align.ExcelAlign;
import kr.or.kpf.lms.common.exceldownload.style.border.DefaultExcelBorders;
import kr.or.kpf.lms.common.exceldownload.style.border.ExcelBorderStyle;
import kr.or.kpf.lms.common.exceldownload.style.color.DefaultExcelColor;
import kr.or.kpf.lms.common.exceldownload.style.color.ExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public enum DefaultExcelCellStyle implements ExcelCellStyle {

    GREY_HEADER(DefaultExcelColor.rgb(217, 217, 217),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
    BLUE_HEADER(DefaultExcelColor.rgb(223, 235, 246),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
    BODY(DefaultExcelColor.rgb(255, 255, 255),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.RIGHT_CENTER);

    private final ExcelColor backgroundColor;
    /**
     * like CSS margin or padding rule,
     * List<DefaultExcelBorder> represents rgb TOP RIGHT BOTTOM LEFT
     */
    private final DefaultExcelBorders borders;
    private final ExcelAlign align;

    DefaultExcelCellStyle(ExcelColor backgroundColor, DefaultExcelBorders borders, ExcelAlign align) {
        this.backgroundColor = backgroundColor;
        this.borders = borders;
        this.align = align;
    }

    @Override
    public void apply(CellStyle cellStyle) {
        backgroundColor.applyForeground(cellStyle);
        borders.apply(cellStyle);
        align.apply(cellStyle);
    }

}