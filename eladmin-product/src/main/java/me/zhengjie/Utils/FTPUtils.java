package me.zhengjie.Utils;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.dto.FtpConfigEntry;
import org.apache.commons.net.ftp.*;
import org.apache.poi.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FTPUtils {

    private static String LOCAL_CHARSET = "GBK";

    /**
     * 连接（配置通用连接属性）至服务器
     *
     * @param remotePath 当前访问目录
     * @return <b>true</b>：连接成功
     * <br/>
     * <b>false</b>：连接失败
     */
    public boolean connectToTheServer(FtpConfigEntry config, String remotePath, FTPClient ftpClient) {

        // 定义返回值
        boolean result = false;

        try {

            // 连接至服务器，端口默认为21时，可直接通过URL连接
            ftpClient.connect(config.getServerAddress(), Integer.parseInt(config.getServerport()));

            // 设置字符编码
            /*if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
                    "OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }*/
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            FTPClientConfig ftpClientConfig = new FTPClientConfig(FTPClientConfig.SYST_NT);
            ftpClientConfig.setServerLanguageCode("zh");

            // 登录服务器
            ftpClient.login(config.getUserName(), config.getPassWord());
            ftpClient.enterLocalPassiveMode();

            // 判断返回码是否合法
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                // 不合法时断开连接
                ftpClient.disconnect();
                // 结束程序
                return false;
            }
            //设置ftp被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置文件操作目录
            result = ftpClient.changeWorkingDirectory(remotePath);
            if (!result) {
                ftpClient.makeDirectory(remotePath);
                result = ftpClient.changeWorkingDirectory(remotePath);
            }
            // 设置文件类型，二进制
            result = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置缓冲区大小
            ftpClient.setBufferSize(3072);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 上传文件至FTP服务器
     *
     * @param storePath 上传文件存储路径
     * @param fileName  上传文件存储名称
     * @param is        上传文件输入流
     * @return <b>true</b>：上传成功
     * <br/>
     * <b>false</b>：上传失败
     */
    public boolean storeFile(FtpConfigEntry config, String storePath, String fileName, InputStream is) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接至服务器
            result = connectToTheServer(config, storePath, ftpClient);
            // 判断服务器是否连接成功
            if (result) {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), FTP.DEFAULT_CONTROL_ENCODING);
                // 上传文件
                result = ftpClient.storeFile(fileName, is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 判断输入流是否存在
            if (null != is) {
                try {
                    // 关闭输入流
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 登出服务器并断开连接
            logout(ftpClient);
        }

        return result;

    }

    public void downloadFileFromFtpDirect(FtpConfigEntry config, String remotePath, String fileName, HttpServletResponse response) {
        InputStream input = null;
        remotePath = checkFilePath(remotePath);
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接至服务器
            connectToTheServer(config, remotePath, ftpClient);

            String directory = new String(remotePath.getBytes("UTF-8"), StandardCharsets.ISO_8859_1);
            ftpClient.changeWorkingDirectory(directory);// 转移到FTP服务器目录
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] fs = ftpClient.listFiles(); // 获取当前目录下的所有文件
            for (FTPFile ff : fs) {
                /*String fi = new String(ff.getName().getBytes(FTP.DEFAULT_CONTROL_ENCODING),LOCAL_CHARSET);
                String fn = Base64.getEncoder().encodeToString(fileName.getBytes());
                String fnn = new String(fileName.getBytes(LOCAL_CHARSET), FTP.DEFAULT_CONTROL_ENCODING);*/
                if (fileName.equals(ff.getName())) {
                    //防止出现中文文件名是返回null，强制设置成ISO-8859-1的编码方式进行获取输入流
                    input = ftpClient.retrieveFileStream(new String(fileName.getBytes(StandardCharsets.UTF_8), FTP.DEFAULT_CONTROL_ENCODING));
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                    IOUtils.copy(input, response.getOutputStream());
                    response.flushBuffer();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("download from ftp is error", e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                logout(ftpClient);
            } catch (IOException ioe) {

                log.error("download from ftp is error", ioe);
            }
        }
    }

    private String checkFilePath(String remotePath) {
        int index = remotePath.indexOf("$");
        if (index > 0) {
            remotePath = remotePath.substring(index + 1, remotePath.length());
        }
        remotePath = remotePath.replaceAll("\\\\", "\\/");
        if (!remotePath.startsWith("/")) {
            remotePath = "/" + remotePath;
        }
        if (!remotePath.endsWith("/")) {
            remotePath = remotePath + "/";
        }
        return remotePath;
    }

    /**
     * 删除FTP服务器文件
     *
     * @param remotePath 当前访问目录
     * @param fileName   文件存储名称
     * @return <b>true</b>：删除成功
     * <br/>
     * <b>false</b>：删除失败
     */
    public boolean deleteFile(FtpConfigEntry config, String remotePath, String fileName) {
        boolean result = false;
        // 连接至服务器
        FTPClient ftpClient = new FTPClient();
        result = connectToTheServer(config, remotePath, ftpClient);
        // 判断服务器是否连接成功
        if (result) {
            try {
                // 删除文件
                result = ftpClient.deleteFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 登出服务器并断开连接
                logout(ftpClient);
            }
        }
        return result;
    }

    /**
     * 检测FTP服务器文件是否存在
     *
     * @param remotePath 检测文件存储路径
     * @param fileName   检测文件存储名称
     * @return <b>true</b>：文件存在
     * <br/>
     * <b>false</b>：文件不存在
     */
    public boolean checkFile(FtpConfigEntry config, String remotePath, String fileName) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接至服务器
            result = connectToTheServer(config, remotePath, ftpClient);
            // 判断服务器是否连接成功
            if (result) {
                // 默认文件不存在
                result = false;
                // 获取文件操作目录下所有文件名称
                String[] remoteNames = ftpClient.listNames();
                // 循环比对文件名称，判断是否含有当前要下载的文件名
                for (String remoteName : remoteNames) {
                    if (fileName.equals(remoteName)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 登出服务器并断开连接
            logout(ftpClient);
        }
        return result;
    }

    /**
     * 登出服务器并断开连接
     *
     * @return <b>true</b>：操作成功
     * <br/>
     * <b>false</b>：操作失败
     */
    public boolean logout(FTPClient ftpClient) {
        boolean result = false;
        if (null != ftpClient) {
            try {
                // 登出服务器
                result = ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 判断连接是否存在
                if (ftpClient.isConnected()) {
                    try {
                        // 断开连接
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

}
