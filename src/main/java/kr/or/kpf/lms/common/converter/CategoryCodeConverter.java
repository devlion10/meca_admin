package kr.or.kpf.lms.common.converter;

import kr.or.kpf.lms.common.support.Code;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CategoryCodeConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return Code.CTS_CTGR.enumOfCode(dbData).codeName;
    }
}
