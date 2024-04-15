package kr.or.kpf.lms.biz.contents.chapter.service;

import kr.or.kpf.lms.biz.contents.chapter.vo.ChapterUploadExcelVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterApiRequestVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterViewRequestVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.response.ChapterApiResponseVO;
import kr.or.kpf.lms.biz.contents.section.service.SectionService;
import kr.or.kpf.lms.biz.contents.section.vo.request.SectionApiRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonContentsRepository;
import kr.or.kpf.lms.repository.contents.ChapterMasterRepository;
import kr.or.kpf.lms.repository.contents.ContentsChapterRepository;
import kr.or.kpf.lms.repository.contents.ContentsMasterRepository;
import kr.or.kpf.lms.repository.contents.SectionMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.ChapterMaster;
import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import kr.or.kpf.lms.repository.entity.contents.ContentsMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 콘텐츠 관리 > 콘텐츠 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ChapterService extends CSServiceSupport {

    private static final String PREFIX_CHAPTER = "CHAP";

    /** 콘텐츠 관리 공통 */
    private final CommonContentsRepository commonContentsRepository;
    /** 콘텐츠 마스터 */
    private final ContentsMasterRepository contentsMasterRepository;
    /** 챕터 마스터 */
    private final ChapterMasterRepository chapterMasterRepository;
    /** 절 마스터 */
    private final SectionMasterRepository sectionMasterRepository;
    /** 절 서비스 */
    private final SectionService sectionService;
    /** 콘텐츠 챕터 목록 */
    private final ContentsChapterRepository contentsChapterRepository;

    /**
     * 챕터 정보 조회
     *
     * @param <T>
     * @param requestObject
     * @return
     */
    public <T> Page<T> getList(ChapterViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonContentsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 챕터 정보 생성
     *
     * @param chapterApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ChapterApiResponseVO createChapterInfo(ChapterApiRequestVO chapterApiRequestVO) {
        ChapterMaster entity = ChapterMaster.builder().build();
        copyNonNullObject(chapterApiRequestVO, entity);
            ChapterApiResponseVO result = ChapterApiResponseVO.builder().build();
            String chapterCode = commonContentsRepository.generateChapterCode(PREFIX_CHAPTER);
            entity.setChapterCode(chapterCode);
            /** 챕터 정보 생성 */
            BeanUtils.copyProperties(chapterMasterRepository.saveAndFlush(entity), result);
            /** 요청 콘텐츠 코드 확인 후 유효한 콘텐츠 코드일 경우 연결 챕터 생성 */
            Optional.ofNullable(chapterApiRequestVO.getContentsCode())
                    .ifPresent(data -> {
                        if (Optional.ofNullable(contentsMasterRepository.findOne(Example.of(ContentsMaster.builder().contentsCode(data).build()))
                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "요청 콘텐츠 코드 유효하지 않음.(" + data + ")"))).isPresent()) {
                            /** 콘텐츠 연결 챕터 생성 */
                            contentsChapterRepository.saveAndFlush(ContentsChapter.builder()
                                    .contentsCode(chapterApiRequestVO.getContentsCode())
                                    .chapterCode(chapterCode)
                                    .sortNo(chapterApiRequestVO.getSortNo())
                                    .build());
                        }
                    });

            return result;
    }

    /**
     * 챕터 정보 수정
     *
     * @param chapterApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ChapterApiResponseVO updateChapterInfo(ChapterApiRequestVO chapterApiRequestVO) {
        return chapterMasterRepository.findOne(Example.of(ChapterMaster.builder()
                                                            .chapterCode(chapterApiRequestVO.getChapterCode())
                                                            .build()))
                .map(chapterMaster -> {
                    copyNonNullObject(chapterApiRequestVO, chapterMaster);

                    ChapterApiResponseVO result = ChapterApiResponseVO.builder().build();
                    BeanUtils.copyProperties(chapterMasterRepository.saveAndFlush(chapterMaster), result);

                    Optional.ofNullable(chapterApiRequestVO.getContentsCode())
                            .ifPresent(data -> {
                                if (Optional.ofNullable(contentsMasterRepository.findOne(Example.of(ContentsMaster.builder().contentsCode(data).build()))
                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "요청 콘텐츠 코드 유효하지 않음.(" + data + ")"))).isPresent()) {
                                    /**
                                     * 콘텐츠 연결 챕터 정보 업데이트
                                     *
                                     * 기존에 존재하는 챕터 목록이라면 업데이트, 없는 챕터 목록 요청이라면 새로 생성
                                     */
                                    contentsChapterRepository.saveAndFlush(contentsChapterRepository.findOne(Example.of(ContentsChapter.builder()
                                                    .contentsCode(chapterApiRequestVO.getContentsCode())
                                                    .chapterCode(chapterApiRequestVO.getChapterCode())
                                                    .build()))
                                            .map(subData -> {
                                                subData.setSortNo(chapterApiRequestVO.getSortNo());
                                                return subData;
                                            }).orElseGet(() -> ContentsChapter.builder()
                                                    .contentsCode(chapterApiRequestVO.getContentsCode())
                                                    .chapterCode(chapterApiRequestVO.getChapterCode())
                                                    .sortNo(chapterApiRequestVO.getSortNo())
                                                    .build()));
                                }
                            });
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2041, "해당 챕터 미존재"));
    }

    /**
     * 챕터 정보 삭제
     *
     * @param chapterApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteChapterInfo(ChapterApiRequestVO chapterApiRequestVO) {
        /** 콘텐츠 & 챕터 간 연결 정보 획득 */
        ContentsChapter contentsChapter = contentsChapterRepository.findOne(Example.of(ContentsChapter.builder()
                        .contentsCode(chapterApiRequestVO.getContentsCode())
                        .chapterCode(chapterApiRequestVO.getChapterCode())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2041, "연결된 챕터 정보 미존재"));
        /** 챕터와 연결된 절 정보 삭제 */
        if(contentsChapter.getChapterMaster().getSectionMasterList().size() > 0) {
            sectionMasterRepository.deleteAll(contentsChapter.getChapterMaster().getSectionMasterList());
        }
        /** 챕터 정보 삭제 */
        if(Optional.ofNullable(contentsChapter.getChapterMaster()).isPresent()) {
            chapterMasterRepository.delete(contentsChapter.getChapterMaster());
        }
        /** 연결 정보 삭제 */
        contentsChapterRepository.delete(contentsChapter);

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 챕터 정보 수정 (생성 / 업데이트 / 삭제)
     *
     * @param chapterApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport modifyChapterInfoList(ChapterApiRequestVO chapterApiRequestVO) {
        List<String> chapterCodeList = chapterApiRequestVO.getChapterInfoList().stream().map(data -> {
            if(StringUtils.isEmpty(data.getChapterCode())) { /** 챕터 코드가 존재하지 않는 경우는 생성건으로 판단 */
                return createChapterInfo(data).getChapterCode();
            } else { /** 챕터 코드가 존재하는 경우는 수정건으로 판단 */
                return updateChapterInfo(data).getChapterCode();
            }
        }).collect(Collectors.toList());

        /** 요청 챕터 정보에 해당하지 않는 매핑 정보가 존재할 경우... 매핑 정보 및 관련 챕터 데이터 삭제 */
        Optional.of(contentsChapterRepository.findAll(Example.of(ContentsChapter.builder()
                        .contentsCode(chapterApiRequestVO.getContentsCode())
                        .build())))
                .filter(data -> data.size() > 0).ifPresent(data -> {
                    data.stream().filter(subData -> !chapterCodeList.contains(subData.getChapterCode()))
                            .forEach(subData -> {
                                ChapterApiRequestVO deleteObject = ChapterApiRequestVO.builder().build();
                                BeanUtils.copyProperties(subData, deleteObject);
                                deleteChapterInfo(deleteObject);
                            });
                });
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 챕터 정보 생성 By 엑셀 파일
     *
     * @param excelFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport createChapterInfoByFile(String contentsCode, MultipartFile excelFile) {
        Workbook workBook = null;
        Sheet workSheet = null;

        try {
            workBook = new XSSFWorkbook(excelFile.getInputStream());
            workSheet = workBook.getSheetAt(0);
            List<ChapterUploadExcelVO> chapterUploadExcelList = new ArrayList<>();

            for(int index = 1; index < workSheet.getPhysicalNumberOfRows(); ++index) { /** 엑셀에서 데이터 추출하여 리스트 객체에 파싱 */
                ChapterUploadExcelVO data = new ChapterUploadExcelVO();
                Row row = workSheet.getRow(index);
                data.setContentsCode(contentsCode);
                data.setChapterName(row.getCell(0).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(0).getNumericCellValue()) : row.getCell(0).getStringCellValue());
                data.setChapterTitle(row.getCell(1).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(1).getNumericCellValue()) : row.getCell(1).getStringCellValue());
                data.setChapterSortNo(row.getCell(2).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(2).getNumericCellValue()) : row.getCell(2).getStringCellValue());
                data.setSectionName(row.getCell(3).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(3).getNumericCellValue()) : row.getCell(3).getStringCellValue());
                data.setSectionSortNo(row.getCell(4).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(4).getNumericCellValue()) : row.getCell(4).getStringCellValue());
                data.setLink(row.getCell(5).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(5).getNumericCellValue()) : row.getCell(5).getStringCellValue());
                chapterUploadExcelList.add(data);
            }

            String chapterNo = "0";
            for(int i = 0; i < chapterUploadExcelList.size(); i++) {
                ChapterUploadExcelVO chapterData = chapterUploadExcelList.get(i);

                ChapterApiRequestVO requestObject = ChapterApiRequestVO.builder().build();
                requestObject.setContentsCode(chapterData.getContentsCode());
                requestObject.setChapterName(chapterData.getChapterName());
                requestObject.setChapterTitle(chapterData.getChapterTitle());
                requestObject.setSortNo(Integer.parseInt(chapterData.getChapterSortNo()));

                if (i != 0 && !chapterData.getChapterName().equals(chapterUploadExcelList.get(i - 1).getChapterName())) {
                    chapterNo = this.createChapterInfo(requestObject).getChapterCode();
                } else if (i == 0) chapterNo = this.createChapterInfo(requestObject).getChapterCode();

                SectionApiRequestVO requestVO = SectionApiRequestVO.builder().build();
                requestVO.setChapterCode(chapterNo);
                requestVO.setSectionName(chapterData.getSectionName());
                requestVO.setSortNo(Integer.parseInt(chapterData.getSectionSortNo()));
                requestVO.setLink(chapterData.getLink());
                sectionService.createSectionInfo(requestVO);
            }

        } catch (Exception e) {
            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
            throw new KPFException(KPF_RESULT.ERROR9999, "지원하지 않는 엑셀 형식입니다.");
        } finally {
            if(workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    throw new KPFException(KPF_RESULT.ERROR9999, "엑셀파일 처리 중 오류 발생.");
                }
            }
        }

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

}
