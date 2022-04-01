package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.DeviceManagerType;
import me.zhengjie.service.dto.AssetDto;
import me.zhengjie.service.dto.DeviceManagerDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2022/3/16 10:45
 */
@DynamicDao(type = DatabaseType.second)
public interface DeviceManagerMapper {
    List<DeviceManagerDto> getDeviceManager(@Param("deviceCode") String deviceCode);

    List<DeviceManagerType> findAllDeviceType();

    int saveDeviceManager(DeviceManagerDto deviceManagerDto);

    List<AssetDto> getAsset();

    List<DeviceManagerDto> getDeviceManagerHistory(@Param("assetCode") String assetCode);

    DeviceManagerDto getLastDeviceManager();
}
