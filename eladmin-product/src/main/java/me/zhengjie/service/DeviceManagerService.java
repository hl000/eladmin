package me.zhengjie.service;

import me.zhengjie.domain.DeviceManagerType;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.service.dto.AssetDto;
import me.zhengjie.service.dto.DeviceManagerDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2022/3/16 10:38
 */
public interface DeviceManagerService {
    List<DeviceManagerDto> getDeviceManager(String deviceCode);

    DeviceManagerDto saveDeviceManager(DeviceManagerDto deviceManagerDto);

    List<DeviceManagerType> getAllDeviceType();

    List<AssetDto> getAsset();

    LocalStorage create(String name, MultipartFile multipartFile);

    void deviceManagerDownload(HttpServletResponse response, String deviceCode);

    List<DeviceManagerDto> getDeviceManagerHistory(String assetCode);
}
