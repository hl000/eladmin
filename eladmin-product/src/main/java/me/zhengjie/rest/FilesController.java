package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.Utils.FTPUtils;
import me.zhengjie.service.dto.FtpConfigEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author HL
 * @create 2022/9/28 9:51
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "ftp文件管理")
@RequestMapping("/api/files")
public class FilesController {

    @Value("${ftp1.address}")
    private String address;

    @Value("${ftp1.port}")
    private String port;

    @Value("${ftp1.username}")
    private String userName;

    @Value("${ftp1.password}")
    private String password;

    @Value("${ftp1.storePath}")
    private String storePath;

    @ApiOperation("文件上传")
    @PostMapping("/upload/file")
    public Object uploadDocFtp(@RequestParam(value = "file") MultipartFile file) throws IOException {
        FTPUtils ftpUtils = new FTPUtils();

        FtpConfigEntry config = new FtpConfigEntry(address, port, userName, password, storePath);

        boolean ftpResult = ftpUtils.storeFile(config, config.getStorePath(), file.getOriginalFilename(), file.getInputStream());
        return ftpResult;
    }

}
