package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Doc.FtpFile;
import me.zhengjie.Doc.KnowledgeService;
import me.zhengjie.Doc.PlmFile;
import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.annotation.Log;
import me.zhengjie.constant.PlmConstant;
import me.zhengjie.dto.PaperDto;
import me.zhengjie.enums.FileOperateEnum;
import me.zhengjie.enums.MergeEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.log.FileLog;
import me.zhengjie.mybatis.DbManager;
import me.zhengjie.request.KvDto;
import me.zhengjie.base.ResKv;
import me.zhengjie.utils.ResultUtils;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static me.zhengjie.utils.DateTraUtil.getDayStr;
import static me.zhengjie.utils.ResultUtils.mergeFromPart;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/knowledge")
@Api(tags = "系统：知识库管理")
public class KnowledgeController {

    @Autowired
    DbManager dbManager;
    @Autowired
    KnowledgeService knowledgeService;
    @Autowired
    FileLog fileLog;



    @ApiOperation("文件ftp下载")
    @PostMapping(value = "/download/ftp")
    public ResponseEntity<Object> downloadDocFtp(
            @RequestBody PlmFile goalFile,
            HttpServletRequest request, HttpServletResponse response){
        try{
            knowledgeService.downloadFileFromFtpDirect(response, goalFile);
            fileLog.log(goalFile, FileOperateEnum.DOWNLOAD);
        }catch (Exception e){
            throw new RuntimeException("运行出错",e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload/ftp")
    public Object uploadDocFtp(@RequestParam(value = "file") MultipartFile file,
                               PaperDto paperDto){
        boolean result;
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        String nickName = userDetails.getUserNickName();
        // String userName = SecurityUtils.getCurrentUser().getUsername();
        //content-type  multipart/form-data
        long currentMils = System.currentTimeMillis();
        String version =  getDayStr(currentMils);
        paperDto.setVersion(version);
        PlmFile uploadFile = knowledgeService.tran2PlmFile(file,  nickName,  paperDto);
        result = knowledgeService.saveFileToFtp(file, uploadFile);
        uploadFile.table = PlmConstant.get_K_KNOWLEDGE_FILE();
        if (result) {
            result = dbManager.insertDoc(uploadFile) >=0;
        }
        fileLog.log(uploadFile, FileOperateEnum.UPLOAD, nickName);
        return result;
    }


    @PostMapping(value = "/dir")
    @Log("查询dir")
    @ApiOperation("查询dir")
    public Object queryDir(
            @RequestBody FtpFile ftpFile,
            HttpServletResponse response
    ) {
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        //kvDto.table = PlmConstant.get_K_KNOWLEDGE_DIR_TABLE();
        knowledgeService.createPersonalDir(userDetails.getDeptName(), userDetails.getUsername());
        return knowledgeService.listDirFromFtp(ftpFile, true, response, userDetails);
    }

    @PostMapping(value = "/dir/create")
    @Log("创建dir")
    @ApiOperation("创建dir")
    public Object createDir(
            @RequestBody FtpFile ftpFile
    ) {
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        if (!ftpFile.getFatherDir().endsWith(userDetails.getUsername())) {
            throw new BadRequestException("只允许在自己的个人文件夹下创建目录");
        }else if (!ftpFile.getCanCreate()) {
            throw new BadRequestException("没有创建目录的权限");
        }else if (knowledgeService.checkExist(ftpFile.getFatherDir(), ftpFile.getCurrent(), ftpFile.directory) != 1) {
            throw new BadRequestException("目录已存在，请重命名");
        }
        return knowledgeService.createDir(ftpFile.getFatherDir() + "/" + ftpFile.getCurrent());
    }





    @GetMapping(value = "/layer")
    @Log("查询layer")
    @ApiOperation("查询layer")
    public Object queryLayer(
            KvDto kvDto
    ) {
        kvDto.table = PlmConstant.get_K_KNOWLEDGE_DIR_TABLE();
        return dbManager.getSelectItem(kvDto);
    }



    @GetMapping(value = "/query/papers")
    @Log("查询paper")
    @ApiOperation("查询paper")
    public Object queryPaper(
            PaperDto paperDto,
            Pageable pageable
    ) {
        List<PlmFile> ret = ResultUtils.mergeSame(dbManager.queryPaper(paperDto.toSqlPaper()), paperDto.isSameMerge()) ;
        return mergeFromPart(ret.size(), ret, pageable, MergeEnum.PAPER, null);
    }



    @GetMapping(value = "/query/papers2")
    @Log("查询paper2")
    @ApiOperation("查询paper2")
    public Object queryPaperEasy(
            KvDto kvDto,
            Pageable pageable
    ) {
        KvDto all = new KvDto();
        all.subList = new ArrayList<>();
        all.subList.add(kvDto);
        all.table = PlmConstant.get_K_KNOWLEDGE_FILE();
        List<PlmFile> ret =  dbManager.queryPaper2(all);
        return mergeFromPart(ret.size(), ret, pageable, MergeEnum.PAPER, null);
    }


    @GetMapping(value = "/suffix/set")
    @Log("查询文件后缀集合")
    @ApiOperation("查询文件后缀集合")
    public Object queryDocSuffixSet() {
        try{
            KvDto kvDto = new KvDto("suffix", "");
            kvDto.table = PlmConstant.get_K_KNOWLEDGE_FILE();
            List<String> result = dbManager.getSelectItemValue(kvDto);
            List<ResKv> obj = new ArrayList<>();
            result.forEach(str-> obj.add(new ResKv(kvDto.key, str)));
            return new ResKv(kvDto.key, obj);
        }catch (Exception e) {
            throw new BadRequestException("/suffix/set");
        }
    }















    @ApiOperation("文件上传")
    @RequestMapping("/upload")
    @ResponseBody
    public Object uploadDoc(@RequestParam(value = "file") MultipartFile file, HttpSession httpSession){
        try{
            //content-type  multipart/form-data
            PlmFile uploadFile = knowledgeService.saveFileToLocal(file,file.getOriginalFilename());
            uploadFile.table = PlmConstant.get_K_KNOWLEDGE_FILE();
            dbManager.insertDoc(uploadFile);
        }catch (Exception e){ }
        return true;
    }


    @ApiOperation("文件下载")
    @PostMapping(value = "/download/local")
    public ResponseEntity<Object> downloadDoc(
            @RequestBody PlmFile goalFile,
            HttpServletRequest request, HttpServletResponse response){
        try{
            knowledgeService.downloadFileFromLocal(request, response, goalFile );
        }catch (Exception e){ }
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @ApiOperation("文件临时下载")
    @PostMapping(value = "/download/ftp2")
    public String downloadDocFtpTemp(
            @RequestBody PlmFile goalFile,
            HttpServletRequest request, HttpServletResponse response){
        return  knowledgeService.tranFromFtpToTemp(request, response, goalFile);
    }



    @ApiOperation("文件临时删除")
    @DeleteMapping(value = "/download/delete/temp")
    public boolean deleteDocFtpTemp(
            @RequestBody PlmFile goalFile){
        return  knowledgeService.deleteFileFromAnonymous(goalFile);
    }



}
