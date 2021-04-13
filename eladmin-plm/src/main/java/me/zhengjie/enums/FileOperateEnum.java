package me.zhengjie.enums;

public enum FileOperateEnum {
    UPLOAD("上传", 1),
    DOWNLOAD("下载", 2),
    UPDATE("更新覆盖", 3),
    DELETE("删除", 4);

    private String operation;
    private int id;

    FileOperateEnum(String operation, int id) {
        this.operation = operation;
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static FileOperateEnum getById(int Id) {
        for (FileOperateEnum fileOperateEnum: values()) {
            if (fileOperateEnum.getId() == Id) {
                return fileOperateEnum;
            }
        }
        return null;
    }
}
