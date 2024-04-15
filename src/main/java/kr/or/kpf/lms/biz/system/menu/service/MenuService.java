package kr.or.kpf.lms.biz.system.menu.service;

import kr.or.kpf.lms.biz.system.menu.vo.request.MenuApiRequestVO;
import kr.or.kpf.lms.biz.system.menu.vo.request.MenuViewRequestVO;
import kr.or.kpf.lms.biz.system.menu.vo.response.MenuApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.CommonSystemRepository;
import kr.or.kpf.lms.repository.entity.system.AdminMenu;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import kr.or.kpf.lms.repository.system.AdminMenuRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 메뉴 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class MenuService extends CSServiceSupport {

    private final CommonSystemRepository commonSystemRepository;

    private final AdminMenuRepository adminMenuRepository;

    /**
     * 시스템 관리 > 메뉴 관리 목록 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(MenuViewRequestVO requestObject) {
        List<AdminMenu> adminMenuList = allMenu();
        ResponseSummary summary = ResponseSummary.builder()
                .count(adminMenuList.size())
                .build();
        Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());
        return (Page<T>) CSPageImpl.of(adminMenuList, pageableToApply, adminMenuList.size());
    }

    public List<AdminMenu> allMenu() {
        List<AdminMenu> adminMenuList = new ArrayList<>();
        List<AdminMenu> allAdminMenu = adminMenuRepository.findAll();
        allAdminMenu.stream()
                .filter(adminMenu -> adminMenu.getDepth() == 0 && adminMenu.getSort() != 0)
                .forEach(adminMenu -> {
                    adminMenu.getSubAdminMenu().addAll(allAdminMenu.stream()
                            .filter(first -> first.getDepth() == 1 && Objects.equals(first.getTopSequenceNo(), adminMenu.getSequenceNo()))
                            .peek(first -> first.getSubAdminMenu().addAll(allAdminMenu.stream()
                                    .filter(second -> second.getDepth() == 2 && Objects.equals(second.getTopSequenceNo(), first.getSequenceNo()))
                                    .collect(Collectors.toList()).stream().sorted(Comparator.comparing(AdminMenu::getSort)).collect(Collectors.toList())))
                            .collect(Collectors.toList()).stream().sorted(Comparator.comparing(AdminMenu::getSort)).collect(Collectors.toList()));
                    adminMenuList.add(adminMenu);
                });
        adminMenuList.sort(Comparator.comparing(AdminMenu::getSort));
        return adminMenuList;
    }

    /**
     * 시스템 관리 > 메뉴 생성
     *
     * 최상위 메뉴는 topSequenceNo와 uri 가 null 이여야만 함.
     * Sample: {"depth":0,"isUsable":false,"menuName":"개발 테스트","sort":8}
     *
     * Depth 상관없이 하위 메뉴는 동일한 uri는 존재할 수 없으며, 유효한 topSequenceNo가 존재해야 함.
     * Sample: {"depth":1,"isUsable":false,"menuFullName":"개발 테스트 > 개발 테스트 서브 메뉴 2","menuName":"개발 테스트 서브 메뉴 2","sort":2,"topSequenceNo":72,"uri":"/test/sample2"}
     *
     * @param menuApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public MenuApiResponseVO createMenu(MenuApiRequestVO menuApiRequestVO) {

        if(!StringUtils.isEmpty(menuApiRequestVO.getUri()) && menuApiRequestVO.getTopSequenceNo() == null) {
            throw new RuntimeException("하위메뉴는 상위 메뉴 일련번호가 필요합니다.");
        }

        if(StringUtils.isEmpty(menuApiRequestVO.getUri()) && menuApiRequestVO.getTopSequenceNo() != null) {
            throw new RuntimeException("상위메뉴는 URI가 존재할 수 없습니다..");
        }

        if(!StringUtils.isEmpty(menuApiRequestVO.getUri()) && adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                .uri(menuApiRequestVO.getUri())
                .build())).isPresent()) {
            throw new RuntimeException("동일한 메뉴가 존재합니다.");
        }

        if (menuApiRequestVO.getTopSequenceNo() == null && adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                .depth(menuApiRequestVO.getDepth())
                .sort(menuApiRequestVO.getSort())
                .build())).isPresent()) {
            throw new RuntimeException("동일한 메뉴 순서가 존재합니다.");
        } else if(adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                        .depth(menuApiRequestVO.getDepth())
                        .topSequenceNo(menuApiRequestVO.getTopSequenceNo())
                        .sort(menuApiRequestVO.getSort())
                .build())).isPresent()) {
            throw new RuntimeException("동일한 메뉴 순서가 존재합니다.");
        }

        if (menuApiRequestVO.getTopSequenceNo() != null && adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                .sequenceNo(menuApiRequestVO.getTopSequenceNo())
                .build())).isEmpty()) {
            throw new RuntimeException("상위 메뉴가 존재하지 않습니다.");
        }

        AdminMenu adminMenu = AdminMenu.builder().build();
        copyNonNullObject(menuApiRequestVO, adminMenu);

        MenuApiResponseVO result = MenuApiResponseVO.builder().build();
        BeanUtils.copyProperties(adminMenuRepository.saveAndFlush(adminMenu), result);
        return result;
    }

    /**
     * 시스템 관리 > 메뉴 업데이트
     *
     *
     *
     * @param menuApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public MenuApiResponseVO updateMenu(MenuApiRequestVO menuApiRequestVO) {
        return adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                        .sequenceNo(menuApiRequestVO.getSequenceNo())
                        .build()))
                .map(adminMenu -> {
                    List<AdminMenu> adminMenus = adminMenuRepository.findAll(Example.of(AdminMenu.builder()
                            .topSequenceNo(menuApiRequestVO.getTopSequenceNo())
                            .build()));
                    if(!Objects.equals(adminMenu.getSort(), menuApiRequestVO.getSort()) && adminMenus.size() != menuApiRequestVO.getSort()) {
                        adminMenus.stream().filter(imsiData -> imsiData.getSort() >= menuApiRequestVO.getSort())
                            .forEach(imsiData -> {
                                imsiData.setSort(imsiData.getSort() + NumberUtils.INTEGER_ONE);
                                adminMenuRepository.saveAndFlush(imsiData);
                            });
                    } else if(!Objects.equals(adminMenu.getSort(), menuApiRequestVO.getSort()) && adminMenus.size() == menuApiRequestVO.getSort()) {
                        adminMenus.forEach(imsiData -> {
                            imsiData.setSort(imsiData.getSort() - NumberUtils.INTEGER_ONE);
                            adminMenuRepository.saveAndFlush(imsiData);
                        });
                    }

                    copyNonNullObject(menuApiRequestVO, adminMenu);

                    MenuApiResponseVO result = MenuApiResponseVO.builder().build();
                    BeanUtils.copyProperties(adminMenuRepository.saveAndFlush(adminMenu), result);

                    /** 정렬 재배치 */
                    AtomicInteger sortNumber = new AtomicInteger(0);
                    adminMenuRepository.findAll(Example.of(AdminMenu.builder()
                            .topSequenceNo(menuApiRequestVO.getTopSequenceNo())
                            .build())).stream()
                        .sorted(Comparator.comparing(AdminMenu::getSort))
                        .forEach(imsiData -> {
                            imsiData.setSort(sortNumber.incrementAndGet());
                            adminMenuRepository.saveAndFlush(imsiData);
                        });

                    return result;
                }).orElseThrow(() -> new RuntimeException("존재하지 않는 메뉴입니다."));
    }

    /**
     * 시스템 관리 > 메뉴 삭제
     *
     * @param sequenceNo
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteMenu(Long sequenceNo) {
        return adminMenuRepository.findOne(Example.of(AdminMenu.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .map(adminMenu -> {

                    if (adminMenuRepository.findAll(Example.of(AdminMenu.builder()
                            .topSequenceNo(sequenceNo)
                            .build())).size() > 0) {
                        throw new RuntimeException("하위 메뉴가 존재할 경우 삭제할 수 없습니다.");
                    }

                    Integer sort = adminMenu.getSort();
                    Long topSequenceNo = adminMenu.getTopSequenceNo();

                    MenuApiResponseVO result = MenuApiResponseVO.builder().build();
                    adminMenuRepository.delete(adminMenu);

                    /** 정렬 재배치 */
                    AtomicInteger sortNumber = new AtomicInteger(0);
                    adminMenuRepository.findAll(Example.of(AdminMenu.builder()
                                    .topSequenceNo(topSequenceNo)
                                    .build())).stream()
                            .sorted(Comparator.comparing(AdminMenu::getSort))
                            .forEach(imsiData -> {
                                imsiData.setSort(sortNumber.incrementAndGet());
                                adminMenuRepository.saveAndFlush(imsiData);
                            });

                    return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
                }).orElseThrow(() -> new RuntimeException("존재하지 않는 메뉴입니다."));
    }
}
