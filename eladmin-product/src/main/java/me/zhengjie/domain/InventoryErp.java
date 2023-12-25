package me.zhengjie.domain;

import lombok.Data;

/**
 * @author HL
 * @create 2023/8/31 15:39
 */
@Data
public class InventoryErp {

    //料品编码
    private String code;

    //料品名称
    private String name;

    //料品规格
    private String specs;

    //期初库存
    private Integer storageInitQty;

    //结存
    private Integer balance;

    //入库
    private Integer storage;

    //出库
    private Integer outbound;

    //采购数量
    private Integer purchase;

    //实到数量
    private Integer actual;

    //实收数量
    private Integer received;

    //未到数量
    private Integer notArrived;
}
