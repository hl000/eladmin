package me.zhengjie.Doc;

public class FtpFile {
    public String path = "";
    public String current = "";
    public boolean directory = true;
    public String suffix;
    public String fatherDir = "";
    public String fileSize;
    public String writer;
    public boolean canCreate = false;
    public String fileType = "";


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
       this.directory = directory;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFatherDir() {
        return fatherDir;
    }

    public void setFatherDir(String fatherDir) {
        this.fatherDir = fatherDir;
    }

    public boolean getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
