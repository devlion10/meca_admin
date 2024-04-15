package kr.or.kpf.lms.common.exceldownload.style.configurer;

import kr.or.kpf.lms.common.exceldownload.style.align.ExcelAlign;
import kr.or.kpf.lms.common.exceldownload.style.align.NoExcelAlign;
import kr.or.kpf.lms.common.exceldownload.style.border.ExcelBorders;
import kr.or.kpf.lms.common.exceldownload.style.border.NoExcelBorders;
import kr.or.kpf.lms.common.exceldownload.style.color.DefaultExcelColor;
import kr.or.kpf.lms.common.exceldownload.style.color.ExcelColor;
import kr.or.kpf.lms.common.exceldownload.style.color.NoExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelCellStyleConfigurer {
    private ExcelAlign excelAlign = new NoExcelAlign();
    private ExcelColor foregroundColor = new NoExcelColor();
    private ExcelBorders excelBorders = new NoExcelBorders();

    public ExcelCellStyleConfigurer() {

    }

    public ExcelCellStyleConfigurer excelAlign(ExcelAlign excelAlign) {
        this.excelAlign = excelAlign;
        return this;
    }

    public ExcelCellStyleConfigurer foregroundColor(int red, int blue, int green) {
        this.foregroundColor = DefaultExcelColor.rgb(red, blue, green);
        return this;
    }

    public ExcelCellStyleConfigurer excelBorders(ExcelBorders excelBorders) {
        this.excelBorders = excelBorders;
        return this;
    }

    public void configure(CellStyle cellStyle) {
        excelAlign.apply(cellStyle);
        foregroundColor.applyForeground(cellStyle);
        excelBorders.apply(cellStyle);
    }
}
