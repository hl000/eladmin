package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author HL
 * @create 2022/9/1 19:21
 */
@Entity
@Getter
@Setter
@Table(name = "electric_pipe_activation_detail")
public class ElectricPipeActivationDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @JoinColumn(name = "electric_pipe_activation_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ElectricPipeActivation electricPipeActivation;

    @Column(name = "ampere_density")
    @ApiModelProperty(value = "电密")
    private Double ampereDensity;

    @Column(name = "electricity")
    @ApiModelProperty(value = "电流")
    private Double electricity;

    @Column(name = "voltage")
    @ApiModelProperty(value = "总电压")
    private Double voltage;

    @Column(name = "power")
    @ApiModelProperty(value = "功率")
    private Double power;

    @Column(name = "mincell")
    @ApiModelProperty(value = "最低节数")
    private Integer mincell;

    @Column(name = "avgvoltage")
    @ApiModelProperty(value = "平均电压")
    private Double avgvoltage;

    @Column(name = "minvoltage")
    @ApiModelProperty(value = "最小电压")
    private Double minvoltage;

    @Column(name = "first_voltage")
    @ApiModelProperty(value = "首节电压")
    private Double firstVoltage;

    @Column(name = "last_voltage")
    @ApiModelProperty(value = "末节电压")
    private Double lastVoltage;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建日期")
    @CreationTimestamp
    private Timestamp createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricPipeActivationDetail that = (ElectricPipeActivationDetail) o;
        return Objects.equals(id, that.id) && Objects.equals(electricPipeActivation.getId(), that.electricPipeActivation.getId()) && Objects.equals(ampereDensity, that.ampereDensity) && Objects.equals(electricity, that.electricity) && Objects.equals(voltage, that.voltage) && Objects.equals(power, that.power) && Objects.equals(mincell, that.mincell) && Objects.equals(avgvoltage, that.avgvoltage) && Objects.equals(minvoltage, that.minvoltage) && Objects.equals(firstVoltage, that.firstVoltage) && Objects.equals(lastVoltage, that.lastVoltage);
    }

    public void copy(ElectricPipeActivationDetail source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
