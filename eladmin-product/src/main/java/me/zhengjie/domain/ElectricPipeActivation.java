package me.zhengjie.domain;

/**
 * @author HL
 * @create 2022/8/30 14:53
 */

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

@Entity
@Getter
@Setter
@Table(name = "electric_pipe_activation")
    public class ElectricPipeActivation implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        @ApiModelProperty(value = "主键id")
        private Integer id;

        @JoinColumn(name = "manufacture_order_id")
        @ManyToOne(fetch = FetchType.EAGER)
        private ManufactureOrder manufactureOrder;

        @Column(name = "active_times")
        @ApiModelProperty(value = "活化次数")
        private Integer activeTimes;

        @JoinColumn(name = "work_device_id")
        @ApiModelProperty(value = "设备")
        @ManyToOne(fetch = FetchType.EAGER)
        private WorkDevice workDevice;

        @JoinColumn(name = "user_id")
        @ApiModelProperty(value = "用户")
        @ManyToOne(fetch = FetchType.EAGER)
        private SysUser sysUser;

        @Column(name = "create_date")
        @ApiModelProperty(value = "创建日期")
        @CreationTimestamp
        private Timestamp createDate;

        @Column(name = "remark")
        @ApiModelProperty(value = "备注")
        private String remark;

    //    @OneToMany(mappedBy = "electricPipeActivation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    //    private List<ElectricPipeActivationDetail> electricPipeActivationDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricPipeActivation that = (ElectricPipeActivation) o;
        return Objects.equals(id, that.id) && Objects.equals(activeTimes, that.activeTimes) && Objects.equals(manufactureOrder.getId(), that.manufactureOrder.getId()) && Objects.equals(workDevice.getId(), that.workDevice.getId()) && Objects.equals(sysUser.getUserId(), that.sysUser.getUserId());
    }

    public void copy(ElectricPipeActivation source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
