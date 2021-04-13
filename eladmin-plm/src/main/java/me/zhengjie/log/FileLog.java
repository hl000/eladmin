package me.zhengjie.log;

import me.zhengjie.Doc.PlmFile;
import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.constant.PlmConstant;
import me.zhengjie.dto.FileLogDto;
import me.zhengjie.enums.FileOperateEnum;
import me.zhengjie.mybatis.DbManager;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static me.zhengjie.utils.DateTraUtil.getDayStr;

@Component
public class FileLog {
    @Autowired
    DbManager dbManager;

    public void log(PlmFile goalFile, FileOperateEnum fileOperateEnum) {
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        String nickName = userDetails.getUserNickName();
        log(goalFile, fileOperateEnum, nickName );
    }

    public void log(PlmFile goalFile, FileOperateEnum fileOperateEnum, String nickName) {
        long currentMils = System.currentTimeMillis();
        String current =  getDayStr(currentMils);
        FileLogDto fileLogDto = new FileLogDto(goalFile.getFileName(), nickName, current, fileOperateEnum.getOperation() );
        fileLogDto.table = PlmConstant.get_K_KNOWLEDGE_LOG_TABLE();
        dbManager.insertFileLog(fileLogDto);
    }
}
