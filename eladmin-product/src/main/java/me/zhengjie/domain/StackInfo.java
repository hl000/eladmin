package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/5/10 9:34
 */
@Entity
@Data
@Table(name="ADMCLXTBH")
public class StackInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FID")
    @ApiModelProperty(value = "FID")
    private Integer FID;

    @Column(name = "FNumber")
    private String FNumber;

    @Column(name = "Fbianhao")
    private String Fbianhao;

    @Column(name = "Fchuchang")
    private String Fchuchang;

    @Column(name = "Fxitong")
    private String Fxitong;

    @Column(name = "Fdiandui")
    private String Fdiandui;

    @Column(name = "Fkongbeng")
    private String Fkongbeng;

    @Column(name = "Fkongqi")
    private String Fkongqi;

    @Column(name = "Fqingbeng")
    private String Fqingbeng;

    @Column(name = "Fqingkong")
    private String Fqingkong;

    @Column(name = "Fmozeng")
    private String Fmozeng;

    @Column(name = "Fbeizhu")
    private String Fbeizhu;

    @Column(name = "Fleixing")
    private String Fleixing;
}
