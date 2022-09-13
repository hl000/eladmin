package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.repository.*;
import me.zhengjie.service.ManufactureOrderService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/7/14 23:45
 */
@Service
@RequiredArgsConstructor
public class ManufactureOrderServiceImpl implements ManufactureOrderService {

    private final ManufactureOrderRepository manufactureOrderRepository;

    private final WorkDeviceRepository workDeviceRepository;

    private final ElectricPipeActivationRepository electricPipeActivationRepository;

    private final ElectricPipeActivationDetailRepository electricPipeActivationDetailRepository;

    private final WorkGroupRepository workGroupRepository;

    @Override
    public Map<String, Object> queryAll(ManufactureOrderQueryCriteria criteria, Pageable pageable) {
        Page<ManufactureOrder> page = manufactureOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<ManufactureOrder> queryAll(ManufactureOrderQueryCriteria criteria) {
        return manufactureOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public ManufactureOrder create(ManufactureOrder resources) {
        resources.setAverageInnerRuler(convertDouble((resources.getInOptionOne() + resources.getInOptionTwo()) / 2.0, "#########.#"));
        resources.setInnerRulerGap(convertDouble(Math.abs((resources.getInOptionOne() - resources.getInOptionTwo())), "#########.#"));
        resources.setAverageOuterRuler(convertDouble((resources.getOutOptionOne() + resources.getOutOptionTwo()) / 2.0, "#########.#"));
        resources.setOuterRulerGap(convertDouble(Math.abs(resources.getOutOptionOne() - resources.getOutOptionTwo()), "#########.#"));
        Double max = resources.getSpringDimension1() > resources.getSpringDimension2() ? resources.getSpringDimension1() : resources.getSpringDimension2();
        max = max > resources.getSpringDimension3() ? max : resources.getSpringDimension3();

        Double min = resources.getSpringDimension1() < resources.getSpringDimension2() ? resources.getSpringDimension1() : resources.getSpringDimension2();
        min = min < resources.getSpringDimension3() ? min : resources.getSpringDimension3();
        resources.setSpringDimensionGap(convertDouble((max - min), "#########.#"));
        resources.setAverageMix(convertDouble((resources.getHydrogenOxygenMix() + resources.getOxygenHydrogenMix()) / 2.0, "#########.#"));
        WorkDevice workDevice = workDeviceRepository.findById(resources.getWorkDevice().getId()).orElseGet(WorkDevice::new);
        resources.setWorkDevice(workDevice);
        return manufactureOrderRepository.save(resources);
    }

    @Override
    public ManufactureOrder update(ManufactureOrder resources) {
        resources.setAverageInnerRuler(convertDouble((resources.getInOptionOne() + resources.getInOptionTwo()) / 2.0, "#########.#"));
        resources.setInnerRulerGap(convertDouble(Math.abs((resources.getInOptionOne() - resources.getInOptionTwo())), "#########.#"));
        resources.setAverageOuterRuler(convertDouble((resources.getOutOptionOne() + resources.getOutOptionTwo()) / 2.0, "#########.#"));
        resources.setOuterRulerGap(convertDouble(Math.abs(resources.getOutOptionOne() - resources.getOutOptionTwo()), "#########.#"));
        Double max = resources.getSpringDimension1() > resources.getSpringDimension2() ? resources.getSpringDimension1() : resources.getSpringDimension2();
        max = max > resources.getSpringDimension3() ? max : resources.getSpringDimension3();

        Double min = resources.getSpringDimension1() < resources.getSpringDimension2() ? resources.getSpringDimension1() : resources.getSpringDimension2();
        min = min < resources.getSpringDimension3() ? min : resources.getSpringDimension3();
        resources.setSpringDimensionGap(convertDouble((max - min), "#########.#"));
        resources.setAverageMix(convertDouble((resources.getHydrogenOxygenMix() + resources.getOxygenHydrogenMix()) / 2.0, "#########.#"));
        ManufactureOrder manufactureOrder = manufactureOrderRepository.findById(resources.getId()).orElseGet(ManufactureOrder::new);
        ValidationUtil.isNull(manufactureOrder.getId(), "manufactureOrder", "id", resources.getId());
        manufactureOrder.copy(resources);
        WorkDevice workDevice = workDeviceRepository.findById(resources.getWorkDevice().getId()).orElseGet(WorkDevice::new);
        manufactureOrder.setWorkDevice(workDevice);
        return manufactureOrderRepository.save(manufactureOrder);
    }

    @Override
    public ElectricPipeActivationDto updateActive(ElectricPipeActivationDto electricPipeActivationDto) {
        ElectricPipeActivationDto electricPipeActivationDto1 = new ElectricPipeActivationDto();
        ElectricPipeActivation electricPipeActivation = electricPipeActivationDto.getElectricPipeActivation();
        ElectricPipeActivation electricPipeActivationResult = electricPipeActivationRepository.save(electricPipeActivation);

        ElectricPipeActivation electricPipeActivation1 = electricPipeActivationRepository.findById(electricPipeActivation.getId()).orElseGet(ElectricPipeActivation::new);
        ValidationUtil.isNull(electricPipeActivation.getId(), "electricPipeActivation", "id", electricPipeActivation1.getId());

        ElectricPipeActivationDetailQueryCriteria criteria = new ElectricPipeActivationDetailQueryCriteria();
        criteria.setElectricPipeActivationId(electricPipeActivation.getId());
        List<ElectricPipeActivationDetail> electricPipeActivationDetails = electricPipeActivationDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<ElectricPipeActivationDetail> electricPipeActivationDetailList = electricPipeActivationDto.getElectricPipeActivationDetailList();
        electricPipeActivationDetailList.stream().forEach(a -> {
            a.setElectricPipeActivation(electricPipeActivationResult);
//            a.setElectricity(convertDouble(a.getAmpereDensity() / 1000 * electricPipeActivation.getManufactureOrder().getActiveArea(), "#########.#"));
//            a.setPower(convertDouble(a.getElectricity() * a.getVoltage() / 1000, "#########.#"));
//            a.setAvgvoltage(convertDouble(a.getVoltage() / electricPipeActivation.getManufactureOrder().getPitchNumber() * 1.0, "#########.###"));
        });

        //处理新增
        List<ElectricPipeActivationDetail> electricPipeActivationDetails1 = electricPipeActivationDetailList.stream().filter(a -> a.getId() == null).collect(Collectors.toList());
        electricPipeActivationDetailRepository.saveAll(electricPipeActivationDetails1);

        //update
        for (ElectricPipeActivationDetail electricPipeActivationDetail : electricPipeActivationDetailList) {
            for (ElectricPipeActivationDetail electricPipeActivationDetail1 : electricPipeActivationDetails) {
                if (electricPipeActivationDetail.getId() != null && electricPipeActivationDetail.getId() == electricPipeActivationDetail1.getId()) {
                    if (!electricPipeActivationDetail1.equals(electricPipeActivationDetail)) {
                        electricPipeActivationDetail1.copy(electricPipeActivationDetail);
                        electricPipeActivationDetailRepository.save(electricPipeActivationDetail1);
                    }
                    break;
                }
            }
        }
        //delete
        List<ElectricPipeActivationDetail> electricPipeActivationList2 = electricPipeActivationDetailList.stream().filter(a -> a.getId() != null).collect(Collectors.toList());
        for (ElectricPipeActivationDetail electricPipeActivationDetail1 : electricPipeActivationDetails) {
            boolean flag = false;
            for (ElectricPipeActivationDetail electricPipeActivationDetail2 : electricPipeActivationList2) {
                if (electricPipeActivationDetail1.getId() != electricPipeActivationDetail2.getId()) {
                    continue;
                } else {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                electricPipeActivationDetailRepository.deleteById(electricPipeActivationDetail1.getId());
            }
        }

        List<ElectricPipeActivationDetail> electricPipeActivationDetailList1 = electricPipeActivationDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        electricPipeActivationDto1.setElectricPipeActivation(electricPipeActivationResult);
        electricPipeActivationDto1.setElectricPipeActivationDetailList(electricPipeActivationDetailList1);
        return electricPipeActivationDto1;
    }

    @Override
    public ElectricPipeActivationDto createActive(ElectricPipeActivationDto electricPipeActivationDto) {
        ElectricPipeActivationDto electricPipeActivationDto1 = new ElectricPipeActivationDto();
        ElectricPipeActivation electricPipeActivation = electricPipeActivationDto.getElectricPipeActivation();
        ElectricPipeActivation electricPipeActivation1 = electricPipeActivationRepository.save(electricPipeActivation);

        List<ElectricPipeActivationDetail> electricPipeActivationDetails = electricPipeActivationDto.getElectricPipeActivationDetailList();
        electricPipeActivationDetails.stream().forEach(a -> {
            a.setElectricPipeActivation(electricPipeActivation1);
//            a.setElectricity(convertDouble(a.getAmpereDensity() / 1000 * electricPipeActivation.getManufactureOrder().getActiveArea(), "#########.#"));
//            a.setPower(convertDouble(a.getElectricity() * a.getVoltage() / 1000, "#########.#"));
//            a.setAvgvoltage(convertDouble(a.getVoltage() / electricPipeActivation.getManufactureOrder().getPitchNumber() * 1.0, "#########.###"));
        });
        List<ElectricPipeActivationDetail> electricPipeActivationDetailList = electricPipeActivationDetailRepository.saveAll(electricPipeActivationDetails);

        electricPipeActivationDto1.setElectricPipeActivation(electricPipeActivation1);
        electricPipeActivationDto1.setElectricPipeActivationDetailList(electricPipeActivationDetailList);
        return electricPipeActivationDto1;
    }

    @Override
    public Object queryElectricActivation(ElectricPipeActivationQueryCriteria criteria, Pageable pageable) {
        Page<ElectricPipeActivation> page = electricPipeActivationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public ElectricPipeActivationDto getElectricActivationById(Integer id) {
        ElectricPipeActivationDto electricPipeActivationDto = new ElectricPipeActivationDto();
        ElectricPipeActivation electricPipeActivation = electricPipeActivationRepository.findById(id).orElseGet(ElectricPipeActivation::new);
        if (electricPipeActivation != null) {
            ElectricPipeActivationDetailQueryCriteria criteria = new ElectricPipeActivationDetailQueryCriteria();
            criteria.setElectricPipeActivationId(electricPipeActivation.getId());
            List<ElectricPipeActivationDetail> electricPipeActivationDetails = electricPipeActivationDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
            electricPipeActivationDto.setElectricPipeActivation(electricPipeActivation);
            electricPipeActivationDto.setElectricPipeActivationDetailList(electricPipeActivationDetails);
        }
        return electricPipeActivationDto;
    }

    @Override
    public List<WorkDevice> queryWorkDevice() {
        return workDeviceRepository.findAll();
    }

    @Override
    public ManufactureOrderActiveDto getManufactureOrderActive(String stackNumber) {
        ManufactureOrderActiveDto manufactureOrderActiveDto = new ManufactureOrderActiveDto();
        ManufactureOrderQueryCriteria criteria = new ManufactureOrderQueryCriteria();
        criteria.setStackNumber(stackNumber);
        List<ManufactureOrder> manufactureOrders = manufactureOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (manufactureOrders != null && manufactureOrders.size() > 0) {
            manufactureOrderActiveDto.setManufactureOrder(manufactureOrders.get(0));
            ElectricPipeActivationQueryCriteria electricPipeActivationQueryCriteria = new ElectricPipeActivationQueryCriteria();
            electricPipeActivationQueryCriteria.setStackNumber(manufactureOrders.get(0).getStackNumber());
            List<ElectricPipeActivation> electricPipeActivations = electricPipeActivationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, electricPipeActivationQueryCriteria, criteriaBuilder));
            manufactureOrderActiveDto.setActiveTimes(electricPipeActivations.size() + 1);
        }
        return manufactureOrderActiveDto;
    }

    @Override
    public List<WorkGroup> queryWorkGroup(WorkGroupQueryCriteria criteria) {
        return workGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    private Double convertDouble(Double source, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return Double.valueOf(decimalFormat.format(source));
    }


}
