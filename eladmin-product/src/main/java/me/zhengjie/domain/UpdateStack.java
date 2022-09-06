package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/5/11 15:42
 */
@Entity
@Data
@Table(name="ADMXTGH")
public class UpdateStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FID")
    @ApiModelProperty(value = "FID")
    private Integer FID;

    @Column(name = "FDATE")
    private String FDATE;

    @Column(name = "FCHEPAI")
    private String FCHEPAI;

    @Column(name = "FGONGLI")
    private Float FGONGLI;

    @Column(name = "FSHIJIAN")
    private Float FSHIJIAN;

    @Column(name = "FYUANYIN")
    private String FYUANYIN;

    @Column(name = "FYBIANHAO")
    private String FYBIANHAO;

    @Column(name = "FGBIANHAO")
    private String FGBIANHAO;

    @Column(name = "FRENYUAN")
    private String FRENYUAN;

    @Column(name = "FGONGSHI")
    private Float FGONGSHI;

    @Column(name = "FNOTE1")
    private String FNOTE1;

    @Column(name = "FNOTE2")
    private String FNOTE2;

    @Column(name = "FNOTE3")
    private String FNOTE3;

    @Column(name = "FCATE")
    private String FCATE;

    @Column(name = "Fjidi")
    private String Fjidi;

    @Column(name = "Fdaima")
    private String Fdaima;

    @Column(name = "Fjibie")
    private String Fjibie;

    @Column(name = "Fyuanxitong")
    private String Fyuanxitong;

    @Column(name = "Fxinxitong")
    private String Fxinxitong;

    @Column(name = "Fleixing")
    private String Fleixing;

    @Column(name = "FPname")
    private String FPname;

    @Column(name = "FEstimatedTime")
    private String FEstimatedTime;


}
