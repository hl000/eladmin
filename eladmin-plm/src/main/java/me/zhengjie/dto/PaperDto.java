package me.zhengjie.dto;


import me.zhengjie.constant.PlmConstant;
import me.zhengjie.request.Sql;

import static me.zhengjie.utils.DateTraUtil.getDayStr;

public class PaperDto extends  Sql {
    private String suffix = "";
    private String writer = "";
    private String version = "";
    private String firstDir = "";
    private String secondDir = "";
    private String thirdDir = "";
    private String forthDir = "";
    private String fileName = "";
    private String desc = "";
    private Long start = 1609383151000L;
    private Long end = 4133904751000L;
    private String startTime ;
    private String endTime ;
    private boolean sameMerge;
    private String filePath;


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirstDir() {
        return firstDir;
    }

    public void setFirstDir(String firstDir) {
        this.firstDir = firstDir;
    }

    public String getSecondDir() {
        return secondDir;
    }

    public void setSecondDir(String secondDir) {
        this.secondDir = secondDir;
    }

    public String getThirdDir() {
        return thirdDir;
    }

    public void setThirdDir(String thirdDir) {
        this.thirdDir = thirdDir;
    }

    public String getForthDir() {
        return forthDir;
    }

    public void setForthDir(String forthDir) {
        this.forthDir = forthDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSameMerge() {
        return sameMerge;
    }

    public void setSameMerge(boolean sameMerge) {
        this.sameMerge = sameMerge;
    }

    public PaperDto toSqlPaper() {
        PaperDto sqlPaper = new PaperDto();
        sqlPaper.setSuffix("%"+ this.suffix.toLowerCase() + "%");
        sqlPaper.setWriter("%"+ this.writer + "%");
        sqlPaper.setFileName("%"+ this.fileName + "%");
        sqlPaper.setFirstDir("%"+ this.firstDir + "%");
        sqlPaper.setSecondDir("%"+ this.secondDir + "%");
        sqlPaper.setThirdDir("%"+ this.thirdDir + "%");
        sqlPaper.setForthDir("%"+ this.forthDir + "%");
        sqlPaper.setStartTime(getDayStr(this.start));
        sqlPaper.setEndTime(getDayStr(this.end));
        sqlPaper.setTable(PlmConstant.get_K_KNOWLEDGE_FILE());
        return sqlPaper;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
