package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.MachineOriginAction;
import me.zhengjie.mapper.MachineMapper;
import me.zhengjie.service.MachineService;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.RetryT;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {
    @Resource
    private MachineMapper machineMapper;

    @Override
    public List<MachineOriginAction> getMachineOrigin(final long dayId, final String base) {
        try {
            return new RetryT<List<MachineOriginAction>>() {
                @Override
                protected List<MachineOriginAction> doAction() throws Exception {
                    return machineMapper.getMachineOrigin( getOriginTable()  , dayId, base);
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String  getOriginTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[WORKSHOP_Collection_Origin]" : "[CEMT_TEST]..[WORKSHOP_Collection_Origin]";

    }
}
