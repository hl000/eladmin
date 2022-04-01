package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.base.MergeResult;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.DeviceManagerType;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.domain.ManufactureSummary;
import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapper.DeviceManagerMapper;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.service.DeviceManagerService;
import me.zhengjie.service.dto.AssetDto;
import me.zhengjie.service.dto.DeviceManagerDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2022/3/16 10:43
 */
@Service
@RequiredArgsConstructor
public class DeviceManagerServiceImpl implements DeviceManagerService {
    @Autowired
    FileProperties fileProperties;

    private final LocalStorageRepository localStorageRepository;

    @Resource
    private final DeviceManagerMapper deviceManagerMapper;

    @Override
    public List<DeviceManagerDto> getDeviceManager(String deviceCode) {
        List<DeviceManagerDto> deviceManagerDtos = deviceManagerMapper.getDeviceManager(deviceCode);
        Map<String, DeviceManagerDto> map = deviceManagerDtos.stream().collect(Collectors.groupingBy(a -> a.getAssetCode(), Collectors.collectingAndThen(Collectors.reducing((o1, o2) ->
                Long.valueOf(o1.getDeviceId()).compareTo(Long.valueOf(o2.getDeviceId())) > 0 ? o1 : o2), Optional::get)));

        List<DeviceManagerDto> deviceManagerDtoList = new ArrayList<>();
        for (String key : map.keySet()) {
            deviceManagerDtoList.add(map.get(key));
        }
        deviceManagerDtoList = deviceManagerDtoList.stream().sorted(Comparator.comparing(a -> a.getDeviceId(), Comparator.reverseOrder())).collect(Collectors.toList());
        return deviceManagerDtoList;
    }

    @Override
    public DeviceManagerDto saveDeviceManager(DeviceManagerDto deviceManagerDto) {
        deviceManagerDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
        int count = deviceManagerMapper.saveDeviceManager(deviceManagerDto);
        if (count == 1) {
            return deviceManagerMapper.getLastDeviceManager();
        }
        return null;
    }

    @Override
    public List<DeviceManagerType> getAllDeviceType() {
        return deviceManagerMapper.findAllDeviceType();
    }

    @Override
    public List<AssetDto> getAsset() {
        return deviceManagerMapper.getAsset();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalStorage create(String name, MultipartFile multipartFile) {
        FileUtil.checkSize(fileProperties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, fileProperties.getPath().getPath() + "pictures" + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );
            return localStorageRepository.save(localStorage);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    @Override
    public void deviceManagerDownload(HttpServletResponse response, String deviceCode) {
        List<DeviceManagerDto> deviceManagerDtoList = getDeviceManager(deviceCode);

        List<Map<String, Object>> list = new ArrayList<>();
        for (DeviceManagerDto managerDto : deviceManagerDtoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("设备编码", managerDto.getDeviceCode());
            map.put("存放地址", managerDto.getDeviceSite());
            map.put("使用人员", managerDto.getUserName());
            map.put("固定资产编号", managerDto.getAssetCode());
            map.put("MAC地址1", managerDto.getLanAddressOne());
            map.put("MAC地址2", managerDto.getLanAddressTwo());
            map.put("MAC地址3", managerDto.getLanAddressThree());
            map.put("MAC地址4", managerDto.getLanAddressFour());
            map.put("设备型号", managerDto.getDeviceTypeName());
            map.put("购买日期", managerDto.getPurchaseDate());
            map.put("资产图片", managerDto.getPictureAddress() == null ? null : managerDto.getPictureAddress());
            map.put("创建时间", managerDto.getCreateDate());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public List<DeviceManagerDto> getDeviceManagerHistory(String assetCode) {
        return deviceManagerMapper.getDeviceManagerHistory(assetCode);
    }


}
