package me.zhengjie.rest;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.DeviceManagerType;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.DeviceManagerService;
import me.zhengjie.service.dto.AssetDto;
import me.zhengjie.service.dto.DeviceManagerDto;
import me.zhengjie.utils.DateTraUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

import static me.zhengjie.utils.FileUtil.getExtensionName;

/**
 * @author HL
 * @create 2022/3/16 10:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "设备资产管理")
@RequestMapping("/api/device")
public class DeviceManagerController {
    @Autowired
    FileProperties fileProperties;

    private final DeviceManagerService deviceManagerService;

    @GetMapping("/getDeviceManager")
    @Log("查询设备档案")
    @ApiOperation("查询设备档案")
    public Object getDeviceManager(String deviceCode, Pageable pageable) {
        List<DeviceManagerDto> deviceManagerList = deviceManagerService.getDeviceManager(deviceCode);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = deviceManagerList.size();
        mergeResult.totalPages = deviceManagerList.size() % pageable.getPageSize() == 0 ? deviceManagerList.size() / pageable.getPageSize() : deviceManagerList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), deviceManagerList);
        return mergeResult;
    }

    @GetMapping("/getDeviceManagerHistory")
    @Log("查询设备档案历史记录")
    @ApiOperation("查询设备档案历史记录")
    public ResponseEntity<Object> getDeviceManagerHistory(String assetCode) {
        return new ResponseEntity<>(deviceManagerService.getDeviceManagerHistory(assetCode), HttpStatus.OK);
    }


    @GetMapping("/getDeviceManager/download")
    @Log("查询设备档案下载")
    @ApiOperation("查询设备档案下载")
    public void deviceManagerDownload(HttpServletResponse response, String deviceCode) {
        deviceManagerService.deviceManagerDownload(response, deviceCode);
    }

    @PostMapping("/saveDeviceManager")
    @Log("新增设备资产管理")
    @ApiOperation("新增设备资产管理")
    public Object saveDeviceManager(@Validated @RequestBody DeviceManagerDto resources) {
        return deviceManagerService.saveDeviceManager(resources);
    }

    @GetMapping("/getDeviceManagerType")
    @Log("设备类型列表")
    @ApiOperation("设备类型列表")
    public List<DeviceManagerType> getDeviceManagerType() {
        return deviceManagerService.getAllDeviceType();
    }

    @GetMapping("/getAsset")
    @Log("固定资产列表")
    @ApiOperation("固定资产列表")
    public List<AssetDto> getAsset() {
        return deviceManagerService.getAsset();
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public String uploadDoc(@RequestParam(value = "file") MultipartFile file) {
        String fileUrl = upload(file);
        return fileUrl;
    }


    @PostMapping("/pictures")
    @ApiOperation("上传图片")
    public Object uploadPictures(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = deviceManagerService.create(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }

    @ApiOperation("文件下载")
    @GetMapping("/download")
    public void download(String fileUrl, HttpServletResponse response) {
        File file = new File(fileUrl);
        if (file.exists()) {
            response.setContentType("application/octet-stream");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally { // 做关闭操作
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public String upload(MultipartFile file) {
        try {
            String date = DateTraUtil.getDateStr(System.currentTimeMillis());
            File todayDir = new File(fileProperties.getPath().getPlmUpload() + date);
            if (!todayDir.exists()) {
                todayDir.mkdirs();
            }
            todayDir.setWritable(true, false);
            String uid = IdUtil.simpleUUID();
            String suffix = getExtensionName(file.getOriginalFilename());
            File dist = new File(todayDir, uid + "." + suffix);
            file.transferTo(dist);
            return todayDir + File.separator + uid + "." + suffix;
        } catch (IOException e) {

        }
        return null;
    }

    @ApiOperation("读取图片")
    @GetMapping("/iomoreimgcom")
    public synchronized void iomoreimgcom(String fileUrl, HttpServletResponse response) throws Exception {
//        String url = request.getParameter("url");
        File file = new File(fileUrl);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        response.setHeader("Content-Type", "image/jpeg");
        byte b[] = new byte[1024];
        int read;
        try {
            while ((read = bis.read(b)) != -1) {
                bos.write(b, 0, read);
            }
            //request.getRequestDispatcher("/components/hazard/yscchird.html").forward(request, response);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (bis != null) {
                bis.close();
            }
        }
    }

}
