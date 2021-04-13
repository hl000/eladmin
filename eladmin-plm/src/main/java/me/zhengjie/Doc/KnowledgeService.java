package me.zhengjie.Doc;

import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.dto.PaperDto;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.base.ResKv;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface KnowledgeService {

    PlmFile saveFileToLocal(MultipartFile file, String desc);

    void downloadFileFromLocal(HttpServletRequest request, HttpServletResponse response, PlmFile plmFile);

    void downloadFileFromFtpDirect(HttpServletResponse response, PlmFile plmFile);

    boolean saveFileToFtp(MultipartFile file,PlmFile uploadFile);

    String tranFromFtpToTemp(HttpServletRequest request, HttpServletResponse response, PlmFile plmFile);

    boolean saveFileToFtpAnonymous(String fileName, InputStream inputStream);

    boolean deleteFileFromAnonymous(PlmFile plmFile);

    PlmFile tran2PlmFile(MultipartFile file, String writer, PaperDto paperDto) throws BadRequestException;

    List<ResKv> listDirFromFtp(FtpFile ftpFile, boolean isQuery, HttpServletResponse response,  UserMoreDetail userDetails);

    void createPersonalDir(String deptName, String username);

    boolean createDir(String path);

    int checkExist(String path, String name, boolean isDirectory);
}

