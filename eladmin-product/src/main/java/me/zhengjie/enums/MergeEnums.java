package me.zhengjie.enums;

public enum MergeEnums {
    SHOUHOU("售后记录", 1),
    STACK_REPLACE("电堆更换记录", 2);

    private String itemName;
    private int id;

    MergeEnums(String itemName, int id) {
        this.itemName = itemName;
        this.id = id;
    }
}
