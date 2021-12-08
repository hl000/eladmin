package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ProductionReportDetailsView;
import me.zhengjie.repository.ProductionReportDetailsViewRepository;
import me.zhengjie.service.ProductionReportDetailsService;
import me.zhengjie.service.dto.ProductionReportDetailsCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/12/6 19:45
 */
@Service
@RequiredArgsConstructor
public class ProductionReportDetailsServiceImpl implements ProductionReportDetailsService {
    private final ProductionReportDetailsViewRepository productionReportDetailsViewRepository;

    @Override
    public Map<String, Object> getProductionReport(ProductionReportDetailsCriteria criteria, Pageable pageable) {
        Page<ProductionReportDetailsView> page = productionReportDetailsViewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<ProductionReportDetailsView> queryAll(ProductionReportDetailsCriteria criteria) {
        return productionReportDetailsViewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

    }

    @Override
    public void download(HttpServletResponse response, ProductionReportDetailsCriteria criteria) {
        List<ProductionReportDetailsView> productionReportDetailsViews = productionReportDetailsViewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (ProductionReportDetailsView productionReportDetailsView : productionReportDetailsViews) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("报工日期", productionReportDetailsView.getVSubmitDate());
            map.put("工序名称", productionReportDetailsView.getVArcName());
            map.put("产品规格", productionReportDetailsView.getVSpec());
            map.put("报工人", productionReportDetailsView.getVPerName());
            map.put("生产订单号", productionReportDetailsView.getVOrderNo());
            map.put("流程卡号", productionReportDetailsView.getVDocNo());
            map.put("产品编码", productionReportDetailsView.getVInvCode());
            map.put("产品名称", productionReportDetailsView.getVInvName());
            map.put("工站", productionReportDetailsView.getVArcCode());
            map.put("设备名称", productionReportDetailsView.getVDevice());

            map.put("任务数量", productionReportDetailsView.getVOrderQty());
            map.put("工时单位", productionReportDetailsView.getVUnutTime());
            map.put("标准工时", productionReportDetailsView.getVWorkTime());
            map.put("实际工时", productionReportDetailsView.getVActualHour());
            map.put("生产批号", productionReportDetailsView.getVPackageNo());

            map.put("报工数量", productionReportDetailsView.getVCompleteQty());
            map.put("不良数量", productionReportDetailsView.getVUnQuaQty());

            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }
}
