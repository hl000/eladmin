package me.zhengjie.enums;

public enum MergeEnum {
    MAIN("主页面", 1),
    GZ_PAPER("工装图纸", 2),
    SHOUHOU("售后记录", 3),
    SB_PAPER("设备图纸", 4),
    CP_PAPER("产品图纸", 5),
    CESHI("活化实验", 6),
    PAPER("知识库文件", 7),
    STACK_REPLACE("电堆更换记录",8),
    CAR_MILEAGE("车辆里程", 9),
    CAR_CONSUME("车辆耗氢", 10);

    private String itemName;
    private int id;

    MergeEnum(String itemName, int id) {
        this.itemName = itemName;
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static MergeEnum getById(int Id) {
        for (MergeEnum plmSelectEnum: values()) {
            if (plmSelectEnum.getId() == Id) {
                return plmSelectEnum;
            }
        }
        return null;
    }
}
