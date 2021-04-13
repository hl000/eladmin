package me.zhengjie.ftp;

import me.zhengjie.config.FtpProperties;
import me.zhengjie.utils.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class FtpClient {

    public FTPClient ftp;

    public FTPClient anonymousFtp;

    @Autowired
    FtpProperties ftpProperties;

    public void connet() {
        try {
            ftp = new FTPClient();
            ftp.connect(ftpProperties.getUrl(), ftpProperties.getPort());
            // ftp.connect(url);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpProperties.getUserName(), ftpProperties.getPassword());// 登录
            ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("gbk");
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
        } catch (Exception e) {

        }
    }


    public void destroy() {
        try {
            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {

        }
    }

    public FTPClient getConnect() {
        return ftp;
    }

    public void AnonymousConnet() {
        try {
            anonymousFtp = new FTPClient();
            anonymousFtp.connect(ftpProperties.getUrl(), ftpProperties.getPort());
            // ftp.connect(url);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            anonymousFtp.login("Anonymous", "User@");// 登录
            anonymousFtp.enterLocalPassiveMode();
            anonymousFtp.setControlEncoding("ISO-8859-1");
            int reply = anonymousFtp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                anonymousFtp.disconnect();
            }
        } catch (Exception e) {

        }
    }

    public void anonymousDestroy() {
        try {
            anonymousFtp.logout();
            anonymousFtp.disconnect();
        } catch (Exception e) {

        }
    }

    public String getUrl() {return ftpProperties.getUrl();}

    public String getFtpTempDir() {
        return "ftp://" + ftpProperties.getUrl() + ftpProperties.getTempDir();
    }

    public String getTempDir() {
        return  ftpProperties.getTempDir();
    }

    public FTPClient getAnonymousConnect() {
        return anonymousFtp;
    }

    /**
     * 创建目录(有则切换目录，没有则创建目录)
     * @param dir
     * @return
     */
    public boolean createDir(String dir){
        if(StringUtils.isEmpty(dir))
            return true;
        String d;
        try {
            //目录编码，解决中文路径问题
            d = new String(dir.toString().getBytes("GBK"),"iso-8859-1");
            //尝试切入目录
            if(ftp.changeWorkingDirectory(d))
                return true;
            String[] arr =  dir.split("/");
            StringBuffer sbfDir=new StringBuffer();
            //循环生成子目录
            for(String s : arr){
                sbfDir.append("/");
                sbfDir.append(s);
                //目录编码，解决中文路径问题
                d = new String(sbfDir.toString().getBytes("GBK"),"iso-8859-1");
                //尝试切入目录
                if(ftp.changeWorkingDirectory(d))
                    continue;
                if(!ftp.makeDirectory(d)){
                    return false;
                }
            }
            //将目录切换至指定路径
            return ftp.changeWorkingDirectory(d);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    public void List(String pathName, ArrayList<String> fileList) throws IOException {
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            String directory = new String(pathName.getBytes("GBK"), "ISO-8859-1");
            //更换目录到当前目录
            this.ftp.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftp.listFiles();
            for(FTPFile file:files){
                if (file.getName().equals(".") || file.getName().equals("..")) {
                    continue;
                }
                if(file.isFile() || file.getName().contains(".")){
                    fileList.add(pathName+file.getName());
                }else if(file.isDirectory()){
                    List(pathName+file.getName()+"/", fileList);
                }
            }
        }
    }



}
