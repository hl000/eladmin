package me.zhengjie.utils;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Ftp2 {
    public FTPClient ftp;
    public ArrayList<String> arFiles;

    /**
     * 重载构造函数
     * @param isPrintCommmand 是否打印与FTPServer的交互命令
     */
    public Ftp2(boolean isPrintCommmand){
        ftp = new FTPClient();
        arFiles = new ArrayList<String>();
        if(isPrintCommmand){
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }
    }

    /**
     * 登陆FTP服务器
     * @param host FTPServer IP地址
     * @param port FTPServer 端口
     * @param username FTPServer 登陆用户名
     * @param password FTPServer 登陆密码
     * @return 是否登录成功
     * @throws IOException
     */
    public boolean login(String host,int port,String username,String password) throws IOException{
        this.ftp.connect(host,port);
        if(FTPReply.isPositiveCompletion(this.ftp.getReplyCode())){
            if(this.ftp.login(username, password)){
                this.ftp.setControlEncoding("GBK");
                return true;
            }
        }
        if(this.ftp.isConnected()){
            this.ftp.disconnect();
        }
        return false;
    }

    /**
     * 关闭数据链接
     * @throws IOException
     */
    public void disConnection() throws IOException{
        if(this.ftp.isConnected()){
            this.ftp.disconnect();
        }
    }

    /**
     * 递归遍历出目录下面所有文件
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public void List(String pathName) throws IOException{
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
                    arFiles.add(pathName+file.getName());
                }else if(file.isDirectory()){
                    List(pathName+file.getName()+"/");
                }
            }
        }
    }

    /**
     * 递归遍历目录下面指定的文件名
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext 文件的扩展名
     * @throws IOException
     */
    public void List(String pathName,String ext) throws IOException{
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            String directory = pathName;
            //更换目录到当前目录
            this.ftp.changeWorkingDirectory(directory);
            FTPFile[] files = this.ftp.listFiles();
            for(FTPFile file:files){
                if(file.isFile()){
                    if(file.getName().endsWith(ext)){
                        arFiles.add(directory+file.getName());
                    }
                }else if(file.isDirectory()){
                    List(directory+file.getName()+"/",ext);
                }
            }
        }
    }

    /**
     * @param url        IP
     * @param port       端口
     * @param username   用户名
     * @param password   密码
     * @param remotePath 服务器上的路径
     * @param fileName   要下载的文件
     * @param localPath  保存到本地的文件
     */
    public static boolean downloadFromFTP(String url, int port, String username, String password, String remotePath,
                                          String fileName, String localPath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        InputStream input=null;
        BufferedOutputStream writer=null;
        byte[] by=null;
        try {
            int reply;
            ftp.connect(url, port);
            // ftp.connect(url);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("gbk");
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            String directory = new String(remotePath.getBytes("GBK"), "ISO-8859-1");
            ftp.changeWorkingDirectory(directory);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles(); // 获取当前目录下的所有文件

            for (FTPFile ff : fs) {
                if (new String(ff.getName().getBytes(),"utf-8").equals(fileName)) {
                    //防止出现中文文件名是返回null，强制设置成ISO-8859-1的编码方式进行获取输入流
                    input=ftp.retrieveFileStream((new String(ff.getName().getBytes("gbk"), "ISO-8859-1")));
                    by=new byte[(int)ff.getSize()];
                    //把文件流保存到byte数组中
                    input.read(by);
                    //文件输出到目标目录，不需要判断本地目录是否有相同文件，会自动覆盖
                    writer=new BufferedOutputStream(new FileOutputStream(localPath+"\\"+fileName));
                    writer.write(by);
                    System.out.println("输出指定文件【"+fileName+"】到本地目录："+localPath+"\\"+fileName+"成功");
                }
            }
            //断开FTP连接
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (ftp.isConnected()) {
                try {
                    if(writer!=null) {
                        writer.close();
                    }
                    if(input!=null) {
                        input.close();
                    }
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    /**
     * ftp上传单个文件
     *
     * @param ftpIP      ftp地址
     * @param port        端口号
     * @param userName    ftp的用户名
     * @param password    ftp的密码
     * @param directory   上传至ftp的路径名不包括文件名
     * @param localFilePath 要上传的本地文件全路径名
     * @param destName    上传至ftp后存储的文件名
     * @throws IOException
     */
    public static boolean uploadFromFTP(String ftpIP,Integer port,String userName,String password,String directory,String localFilePath,String destName) {


        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        boolean result = false;
        try {
            directory = new String(directory.getBytes("GBK"), "ISO-8859-1");
            ftpClient.connect(ftpIP, port);
            ftpClient.login(userName, password);
            ftpClient.enterLocalPassiveMode();
            // 设置上传目录
            ftpClient.changeWorkingDirectory(directory);
            ftpClient.setBufferSize(1024);
            ftpClient.setConnectTimeout(10*1000);
            ftpClient.setControlEncoding("gbk");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //防止出现中文文件名是返回null，强制把文件名设置成ISO-8859-1的编码方式进行删除指定文件
            ftpClient.deleteFile(directory+new String(destName.getBytes("gbk"),"ISO-8859-1"));
            File srcFile = new File(localFilePath);
            fis = new FileInputStream(srcFile);
            //上传到FTP服务器==防止出现中文文件名是返回null，强制把文件名设置成ISO-8859-1的编码方式进行上传
            result = ftpClient.storeFile((directory + new String(destName.getBytes("gbk"),"ISO-8859-1")), fis);

        } catch (NumberFormatException e) {
            System.err.println("FTP端口配置错误:不是数字:");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
       /* Ftp2 f = new Ftp2(true);
        if(f.login("10.10.100.235", 21, "fileadmin", "4DwqzZBp")){
            f.List("/");
        }
        f.disConnection();
        Iterator<String> it = f.arFiles.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }*/
        downloadFromFTP("10.10.100.235", 21, "fileadmin", "4DwqzZBp",
                "/设备/1冲压/保养手册/", "冲床保养计划.zip", "C:\\ytupload");

/*        uploadFromFTP("10.10.100.235", 21, "fileadmin", "4DwqzZBp",
               "/设备/1冲压/保养手册/", "C:\\ytupload\\cbd644e9-d7a1-4de1-9a6c-0c5471e6a736.rar", "test.rar" );*/
        /*uploadFromFTP("10.10.100.235", 21, "fileadmin", "4DwqzZBp",
                "/设备/1冲压/保养手册/", "E:\\使用前说明.txt", "使用前说明23.txt" );*/

    }
}
