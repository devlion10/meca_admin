package kr.or.kpf.lms.common.exceldownload.resource;

import org.apache.poi.ss.usermodel.DataFormat;

public interface DataFormatDecider {
    short getDataFormat(DataFormat dataFormat, Class<?> type);
}
