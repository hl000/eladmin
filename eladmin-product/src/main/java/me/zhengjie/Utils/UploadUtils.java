package me.zhengjie.Utils;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;


/**
 * @author HL
 * @create 2022/9/22 11:37
 */

public class UploadUtils {

    private static final Logger log = LoggerFactory.getLogger(UploadUtils.class);
    private static String PATH = null;

    static {
        try {
            PATH = ResourceUtils.getURL("classpath:").getPath() + "/static/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * description:
     * 传入文件夹名和文件，保存至那个文件夹
     *
     * @param directoryName 需要上传到哪个文件家
     * @param uploads       上传的文件
     * @return int 返回上传成功的条数
     */
    public static void upload(String directoryName, MultipartFile[] uploads, List<String> fileList) throws IOException {

        String path = PATH + directoryName;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        int flag = 0;
        for (MultipartFile upload : uploads) {
            String filename = fileList.get(flag++);
            upload.transferTo(new File(path, filename));
        }
        ;
    }


    /**
     * @param directoryName 要保存的文件加
     * @param upload        下载的文件
     * @param fileName      需要保存的文件名
     * @return java.lang.String
     */
    public static String upload(String directoryName, MultipartFile upload, String fileName) throws IOException {
        String path = PATH + directoryName;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        upload.transferTo(new File(path, fileName));
        return fileName;
    }

    //下载

    /**
     * @param path     需要下载的文件路径
     * @param fileName 传入下载后的文件名字
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void download(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        log.info("正在下载的资源路径===========" + path);
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file = new File(path);
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 从jar包中下载
     *
     * @param path     需要下载的文件路径
     * @param fileName 传入下载后的文件名字
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void jarDownload(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        log.info("正在下载的资源路径===========" + path);
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream = classPathResource.getInputStream();
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从jar包中复制出文件
     *
     * @param copyFilePath jar中的文件
     * @param filePath     复制出的文件
     */
    public static void jarCopyFile(String copyFilePath, String filePath) {
        log.info("jar中从" + copyFilePath + "复制到" + filePath);
        FileOutputStream ois = null;

        try {
            ClassPathResource classPathResource = new ClassPathResource(copyFilePath);
            InputStream inputStream = classPathResource.getInputStream();
            ois = new FileOutputStream(filePath);
            IOUtils.copy(inputStream, ois);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}