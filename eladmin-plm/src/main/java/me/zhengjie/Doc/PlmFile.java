package me.zhengjie.Doc;

import lombok.Data;
import me.zhengjie.request.Sql;

import java.io.Serializable;

public class PlmFile extends Sql implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileId;
    private String filePath;
    private String sha256;
    private String fileSize;
    private String fileType;
    private String fileDesc;
    private String fileName;
    private String originalFileName;
    private String suffix;
    private String writer;
    private String tag;
    private String version;
    private String storePath;
    private String ftpDir;
    private String firstDir;
    private String secondDir;
    private String thirdDir;
    private String forthDir;
    private int sameCount = 1;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getFtpDir() {
        return ftpDir;
    }

    public void setFtpDir(String ftpDir) {
        this.ftpDir = ftpDir;
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

    public int getSameCount() {
        return sameCount;
    }

    public void setSameCount(int sameCount) {
        this.sameCount = sameCount;
    }
}

