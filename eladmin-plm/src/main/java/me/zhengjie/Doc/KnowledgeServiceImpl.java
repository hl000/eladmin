package me.zhengjie.Doc;

import cn.hutool.core.util.IdUtil;
import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.config.FileProperties;
import me.zhengjie.constant.PlmConstant;
import me.zhengjie.dto.PaperDto;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.ftp.FtpClient;
import me.zhengjie.mybatis.DbManager;
import me.zhengjie.request.KvDto;
import me.zhengjie.base.ResKv;
import me.zhengjie.utils.DateTraUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.zhengjie.utils.FileUtil.*;


@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    FileProperties fileProperties;

    @Autowired
    FtpClient ftpClient;

    @Autowired
    DbManager dbManager;


    @Override
    public PlmFile saveFileToLocal(MultipartFile file, String desc) {
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
            PlmFile uploadFile = new PlmFile();
            uploadFile.setFileId(uid);
            uploadFile.setFileDesc(desc);
            uploadFile.setFileName(file.getName());
            uploadFile.setFilePath(dist.getAbsolutePath());
            uploadFile.setOriginalFileName(file.getOriginalFilename());
            uploadFile.setFileType(file.getContentType());
            uploadFile.setFileSize(FileUtil.getSize(file.getSize()));
            uploadFile.setSuffix(suffix);
            // uploadFile.setSha256(HashUtil.sha256(dist));
            return uploadFile;
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public void downloadFileFromLocal(HttpServletRequest request, HttpServletResponse response, PlmFile plmFile) {
        //  downloadFile(request, response, new File(plmFile.getFilePath() + plmFile.getFileName()), false);
        //response.setContentType("application/octet-stream");
        InputStream input = null;
        checkFilePath(plmFile);
        BufferedOutputStream writer = null;
        byte[] by = null;
        String uid = IdUtil.simpleUUID();
        String suffix = ".bin";
        try {
            String directory = new String(plmFile.getFilePath().getBytes("GBK"), "ISO-8859-1");
            ftpClient.connet();
            ftpClient.getConnect().changeWorkingDirectory(directory);// 转移到FTP服务器目录
            ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
            for (FTPFile ff : fs) {
                if (new String(ff.getName().getBytes(), "utf-8").equals(plmFile.getFileName())) {
                    //防止出现中文文件名是返回null，强制设置成ISO-8859-1的编码方式进行获取输入流
                    input = ftpClient.getConnect().retrieveFileStream((new String(ff.getName().getBytes("gbk"), "ISO-8859-1")));
                    suffix = "." + getExtensionName(plmFile.getFileName());
                    by = new byte[(int) ff.getSize()];
                    //把文件流保存到byte数组中
                    input.read(by);
                    //文件输出到目标目录，不需要判断本地目录是否有相同文件，会自动覆盖
                    File dir = new File(fileProperties.getPath().getPlmDownload());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    writer = new BufferedOutputStream(new FileOutputStream(fileProperties.getPath().getPlmDownload() + (uid + suffix)));
                    writer.write(by);
                    break;
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (input != null) {
                    input.close();
                }
                ftpClient.destroy();
            } catch (IOException ioe) {
            }
        }

    }

    @Override
    public void downloadFileFromFtpDirect( HttpServletResponse response, PlmFile plmFile) {
        //response.setContentType("application/octet-stream");
        InputStream input = null;
        checkFilePath(plmFile);
        try {
            String directory = new String(plmFile.getFilePath().getBytes("GBK"), "ISO-8859-1");
            ftpClient.connet();
            ftpClient.getConnect().changeWorkingDirectory(directory);// 转移到FTP服务器目录
            FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
            ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            String uid = IdUtil.simpleUUID();
            String suffix = ".bin";
            for (FTPFile ff : fs) {
                if (new String(ff.getName().getBytes(), "utf-8").equals(plmFile.getFileName())) {
                    //防止出现中文文件名是返回null，强制设置成ISO-8859-1的编码方式进行获取输入流
                    input = ftpClient.getConnect().retrieveFileStream((new String(ff.getName().getBytes("gbk"), "ISO-8859-1")));
                    suffix = "." + getExtensionName(plmFile.getFileName());
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=" + (uid + suffix));
                    // response.setHeader("Content-disposition","attachment;filename="+  java.net.URLEncoder.encode("C2板子", "ISO-8859-1"));

                    IOUtils.copy(input, response.getOutputStream());
                    response.flushBuffer();
                    break;
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                ftpClient.destroy();
            } catch (IOException ioe) {
            }
        }
    }

    private void checkFilePath(PlmFile plmFile) {
        int index = plmFile.getFilePath().indexOf("$");
        if (index > 0) {
            plmFile.setFilePath(plmFile.getFilePath().substring(index + 1, plmFile.getFilePath().length()));
        }
        plmFile.setFilePath(plmFile.getFilePath().replaceAll("\\\\", "\\/"));
        if (!plmFile.getFilePath().startsWith("/")) {
            plmFile.setFilePath("/" + plmFile.getFilePath());
        }
        if (!plmFile.getFilePath().endsWith("/")) {
            plmFile.setFilePath(plmFile.getFilePath() + "/");
        }

    }

    @Override
    public boolean saveFileToFtp(MultipartFile file, PlmFile uploadFile) {
        boolean result = false;
        try {
            ftpClient.connet();
            // 设置上传目录
            // ftpClient.getConnect().changeWorkingDirectory(directory);
            String directory = uploadFile.getFtpDir();
            ftpClient.createDir(directory);
            //FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
            ftpClient.getConnect().setBufferSize(1024);
            ftpClient.getConnect().setConnectTimeout(10 * 1000);
            ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            directory = new String(directory.getBytes("GBK"), "ISO-8859-1");
            //防止出现中文文件名是返回null，强制把文件名设置成ISO-8859-1的编码方式进行删除指定文件
            ftpClient.getConnect().deleteFile(directory + new String(uploadFile.getOriginalFileName().getBytes("gbk"), "ISO-8859-1") + "_" + uploadFile.getVersion() + uploadFile.getSuffix());
            //上传到FTP服务器==防止出现中文文件名是返回null，强制把文件名设置成ISO-8859-1的编码方式进行上传
            result = ftpClient.getConnect().storeFile((directory + new String(uploadFile.getOriginalFileName().getBytes("gbk"), "ISO-8859-1") + "_" + uploadFile.getVersion() + uploadFile.getSuffix()), file.getInputStream());

        } catch (NumberFormatException e) {
            System.err.println("FTP端口配置错误:不是数字:");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ftpClient.destroy();
        }
        return result;
    }


    @Override
    public String tranFromFtpToTemp(HttpServletRequest request, HttpServletResponse response, PlmFile plmFile) {
        checkFilePath(plmFile);
        String uid = IdUtil.simpleUUID();
        String suffix = ".bin";
        InputStream input = null;
        try {
            String directory = new String(plmFile.getFilePath().getBytes("GBK"), "ISO-8859-1");
            ftpClient.connet();
            ftpClient.getConnect().changeWorkingDirectory(directory);// 转移到FTP服务器目录
            ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
            for (FTPFile ff : fs) {
                if (new String(ff.getName().getBytes(), "utf-8").equals(plmFile.getFileName())) {
                    //防止出现中文文件名是返回null，强制设置成ISO-8859-1的编码方式进行获取输入流
                    input = ftpClient.getConnect().retrieveFileStream((new String(ff.getName().getBytes("gbk"), "ISO-8859-1")));
                    suffix = "." + getExtensionName(plmFile.getFileName());
                    saveFileToFtpAnonymous(uid + suffix, input);
                    break;
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                ftpClient.destroy();
            } catch (IOException ioe) {
            }
        }
        return ftpClient.getFtpTempDir() + (uid + suffix);
    }

    @Override
    public boolean saveFileToFtpAnonymous(String fileName, InputStream inputStream) {
        boolean result = false;
        try {
            ftpClient.AnonymousConnet();
            // 设置上传目录
            ftpClient.getAnonymousConnect().changeWorkingDirectory("/temp/");
            // FTPFile[] fs = ftpClient.getAnonymousConnect().listFiles(); // 获取当前目录下的所有文件
            ftpClient.getAnonymousConnect().setBufferSize(1024);
            ftpClient.getAnonymousConnect().setConnectTimeout(10 * 1000);
            ftpClient.getAnonymousConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            //防止出现中文文件名是返回null，强制把文件名设置成ISO-8859-1的编码方式进行删除指定文件
            result = ftpClient.getAnonymousConnect().storeFile("/temp/" + fileName, inputStream);

        } catch (NumberFormatException e) {
            System.err.println("FTP端口配置错误:不是数字:");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (Exception e) {

                }
            }
            ftpClient.anonymousDestroy();
        }
        return result;
    }

    @Override
    public boolean deleteFileFromAnonymous(PlmFile plmFile) {
        try {
            ftpClient.AnonymousConnet();
            String goal = plmFile.getFilePath().substring(plmFile.getFilePath().indexOf(ftpClient.getTempDir()), plmFile.getFilePath().length());
            return ftpClient.getAnonymousConnect().deleteFile(goal);
        } catch (Exception e) {
            return false;
        } finally {
            ftpClient.anonymousDestroy();
        }

    }

    @Override
    public PlmFile tran2PlmFile(MultipartFile file, String writer, PaperDto paperDto) throws BadRequestException {

        PlmFile plmFile = new PlmFile();
        plmFile.setFileDesc(paperDto.getDesc());
        String originFileName = getFileNameNoEx(file.getOriginalFilename());

        if (FileUtil.getFileType(getExtensionName(file.getOriginalFilename())).equals(FileUtil.OTHER)) {
            throw new BadRequestException("file type not supported");
        }
        String suffix = "." + getExtensionName(file.getOriginalFilename());
        plmFile.setOriginalFileName(originFileName);
        plmFile.setSuffix(suffix);
        plmFile.setFileName(originFileName + "_" + paperDto.getVersion() + suffix);
        plmFile.setWriter(writer);
        plmFile.setVersion(paperDto.getVersion());
        plmFile.setFileSize(FileUtil.getSize(file.getSize()));
        plmFile.setTag(null);

        if (!paperDto.getFilePath().startsWith("/")) {
            paperDto.setFilePath("/" + paperDto.getFilePath());
        }
        String[] dirs = paperDto.getFilePath().split("/");
        if (dirs.length < 4) {
            throw  new BadRequestException("can not upload here");
        }

       /* String storePath = "\\\\" + ftpClient.getUrl() + "\\技术文档$\\" + paperDto.getFirstDir() + "\\"
                + (StringUtils.isNotEmpty(paperDto.getSecondDir()) ? paperDto.getSecondDir() + "\\" : "")
                + (StringUtils.isNotEmpty(paperDto.getThirdDir()) ? paperDto.getThirdDir() + "\\" : "")
                + (StringUtils.isNotEmpty(paperDto.getForthDir()) ? paperDto.getForthDir() + "\\" : "");
        plmFile.setStorePath(storePath);
        String ftpLayer = "/" + paperDto.getFirstDir() + "/"
                + (StringUtils.isNotEmpty(paperDto.getSecondDir()) ? paperDto.getSecondDir() + "/" : "")
                + (StringUtils.isNotEmpty(paperDto.getThirdDir()) ? paperDto.getThirdDir() + "/" : "")
                + (StringUtils.isNotEmpty(paperDto.getForthDir()) ? paperDto.getForthDir() + "/" : "");
        */
        String ftpLayer = paperDto.getFilePath();
        String storePath = "\\\\" + ftpClient.getUrl() + "\\技术文档$\\" ;
        for (String s : dirs) {
            if (StringUtils.isNotEmpty(s)) {
                storePath = storePath + s + "\\";
            }
        }
        plmFile.setFtpDir(ftpLayer);
        plmFile.setStorePath(storePath);
       /* plmFile.setFirstDir(paperDto.getFirstDir());
        plmFile.setSecondDir(paperDto.getSecondDir());
        plmFile.setThirdDir(paperDto.getThirdDir());
        plmFile.setForthDir(paperDto.getForthDir());*/
        plmFile.setFirstDir(dirs[1]);
        plmFile.setSecondDir(dirs[2]);
        plmFile.setThirdDir(dirs[3]);
        String left = "";
        for (int i =4; i < dirs.length; i++) {
            left =  "/" +  dirs[i] ;
        }
        plmFile.setForthDir(left);
        return plmFile;
    }

    @Override
    public List<ResKv> listDirFromFtp(FtpFile ftpFile, boolean isQuery,HttpServletResponse response,  UserMoreDetail userDetails) {
        List<ResKv> dirlist = new ArrayList<>();
        List<ResKv> filelist = new ArrayList<>();
        try {
            String co = ftpFile.getPath();
            List<String> allowedPart = new ArrayList<>();
            if(StringUtils.isEmpty(ftpFile.getPath())) {
                allowedPart = dbManager.queryUserAllowedPart(PlmConstant.get_K_PLM_USER_ALLOWED(), SecurityUtils.getCurrentUser().getUsername(), "knowledge");
            }
            if (ftpFile.isDirectory()) {
                String directory = new String(co.getBytes("GBK"), "ISO-8859-1");
                ftpClient.connet();
                ftpClient.getConnect().changeWorkingDirectory(directory);// 转移到FTP服务器目录
                ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
                FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
                for (FTPFile ff : fs) {
                    String name = new String(ff.getName().getBytes(), "utf-8");
                    if (name.equals(".")) {
                        continue;
                    }
                    if (!name.equals("..")) {
                        if (ftpFile.getPath().equals("/个人文件夹")) {
                            if (!userDetails.getUsername().contains("admin")
                                &&!name.contains(userDetails.getDeptName())) {
                                continue;
                            }
                        }
                        if (ftpFile.getPath().equals("/个人文件夹/" + userDetails.getDeptName())) {
                            if (!userDetails.getUsername().contains("admin")
                                &&!name.equals(userDetails.getUsername())) {
                                continue;
                            }
                        }
                    }
                    ResKv resKv = new ResKv();
                    resKv.label = name;
                    FtpFile directoryFile = new FtpFile();
                    directoryFile.setFatherDir(co);
                    directoryFile.setCurrent(name);
                    directoryFile.setPath(co + (co.endsWith("/") ? "" : "/") + name);
                    directoryFile.setDirectory(ff.isDirectory());
                    if (!ff.isDirectory()) {
                        directoryFile.setSuffix(getExtensionName(name));
                        directoryFile.setFileType(getFileType(directoryFile.getSuffix()));
                    }

                    if (name.equals("..")) {
                        List<String> part = Arrays.asList( directoryFile.getPath().split("/"));
                        if (part.size() <= 2) {
                            directoryFile.setPath("");
                        }else {
                            part = part.subList(0, part.size()-2);
                            String fixPath = part.stream().collect(Collectors.joining("/"));
                            directoryFile.setPath(fixPath);
                        }

                        part = Arrays.asList( directoryFile.getFatherDir().split("/"));
                        if (part.size() <= 2) {
                            directoryFile.setFatherDir("");
                        }else {
                            part = part.subList(0, part.size()-2);
                            String fixPath = part.stream().collect(Collectors.joining("/"));
                            directoryFile.setFatherDir(fixPath);
                        }
                    }
                    if (ftpFile.getPath().equals("/个人文件夹/" +userDetails.getDeptName() + "/" + userDetails.getUsername() )
                            && name.equals("..")) {
                        directoryFile.setCanCreate(true);
                    }
                    if(ftpFile.getPath().endsWith(userDetails.getUsername()) && !name.equals("..")) {
                        directoryFile.setCanCreate(false);
                    }
                    resKv.value = directoryFile;
                    if(directoryFile.isDirectory()  || StringUtils.isNotEmpty(directoryFile.getFatherDir()) ) {
                        if (directoryFile.isDirectory()) {
                            if ( StringUtils.isNotEmpty(directoryFile.getFatherDir())
                                    || allowedPart.contains(directoryFile.getCurrent())
                                    || directoryFile.getCurrent().equals("个人文件夹")
                                    || directoryFile.getCurrent().equals("..")
                                    || userDetails.getUsername().contains("admin")
                                    || userDetails.getUsername().contains("test")) {
                                if (name.equals("..")) {
                                    dirlist.add(0, resKv);
                                } else {
                                    dirlist.add(resKv);
                                }
                            }
                        }else {
                            filelist.add(resKv);
                        }
                    }
                    ftpClient.destroy();
                }
            }else { //下载 or 查看详情
                KvDto all = new KvDto();
                all.subList = new ArrayList<>();
                KvDto sub = new KvDto("fileName", ftpFile.current);
                all.subList.add(sub);
                all.table = PlmConstant.get_K_KNOWLEDGE_FILE();
                List<PlmFile> ret =  dbManager.queryPaper2(all);
                for (PlmFile plmFile :ret) {
                    ResKv resKv1 = new ResKv("原始文件名", plmFile.getOriginalFileName());
                    ResKv resKv2 = new ResKv("文件描述", plmFile.getFileDesc());
                    ResKv resKv3 = new ResKv("文件后缀", plmFile.getSuffix());
                    ResKv resKv6 = new ResKv("文件类型", getFileType(plmFile.getSuffix()));
                    ResKv resKv4 = new ResKv("上传者", plmFile.getWriter());
                    ResKv resKv5 = new ResKv("文件大小", plmFile.getFileSize());
                    filelist.add(resKv1);
                    filelist.add(resKv2);
                    filelist.add(resKv3);
                    filelist.add(resKv4);
                    filelist.add(resKv5);
                    filelist.add(resKv6);
                }
                ftpClient.destroy();
            }

        } catch (Exception e) {

        }
        dirlist.addAll(filelist);
        return dirlist;
    }

    @Override
    public void createPersonalDir(String deptName, String username) {
        try {
            ftpClient.connet();
            // 设置上传目录
            // ftpClient.getConnect().changeWorkingDirectory(directory);
            String directory = "/个人文件夹/" + deptName + "/" + username;
            ftpClient.createDir(directory);
        }catch (Exception e) {

        }finally {
            ftpClient.destroy();
        }
    }

    public boolean createDir(String path) {
        try {
            ftpClient.connet();
            ftpClient.createDir(path);
            return true;
        }catch (Exception e) {
            return false;
        }finally {
            ftpClient.destroy();
        }
    }

    public int checkExist(String path, String name, boolean isDirectory) {
        try {
            String directory = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftpClient.connet();
            ftpClient.getConnect().changeWorkingDirectory(directory);// 转移到FTP服务器目录
            ftpClient.getConnect().setFileType(FTPClient.BINARY_FILE_TYPE);
            FTPFile[] fs = ftpClient.getConnect().listFiles(); // 获取当前目录下的所有文件
            for (FTPFile ff : fs) {
                String existed = new String(ff.getName().getBytes(), "utf-8");
                if (isDirectory && ff.isDirectory() && existed.equals(name)) {
                    ftpClient.destroy();
                    return 0;
                }
                if (!isDirectory && !ff.isDirectory() && existed.equals(name)) {
                    ftpClient.destroy();
                    return 0;
                }
            }
            ftpClient.destroy();
            return 1;
        }catch (Exception e) {
            return -1;
        }
    }

}
