package kr.or.kpf.lms.common.exceldownload.resource.collection;

import kr.or.kpf.lms.common.exceldownload.resource.DataFormatDecider;
import kr.or.kpf.lms.common.exceldownload.resource.ExcelCellKey;
import kr.or.kpf.lms.common.exceldownload.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class PreCalculatedCellStyleMap {
    private final DataFormatDecider dataFormatDecider;

    public PreCalculatedCellStyleMap(DataFormatDecider dataFormatDecider) {
        this.dataFormatDecider = dataFormatDecider;
    }

    private final Map<ExcelCellKey, CellStyle> cellStyleMap = new HashMap<>();

    public void put(Class<?> fieldType, ExcelCellKey excelCellKey, ExcelCellStyle excelCellStyle, Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormatDecider.getDataFormat(dataFormat, fieldType));
        excelCellStyle.apply(cellStyle);
        cellStyleMap.put(excelCellKey, cellStyle);
    }

    public CellStyle get(ExcelCellKey excelCellKey) {
        return cellStyleMap.get(excelCellKey);
    }

    public boolean isEmpty() {
        return cellStyleMap.isEmpty();
    }
}