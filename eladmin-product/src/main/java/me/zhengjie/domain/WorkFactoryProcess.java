package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/14 14:02
 */
@Entity
@Data
@Table(name = "work_factory_process")
public class WorkFactoryProcess {

    @Id
    @Column(name = "process_code")
    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @Column(name = "process_name")
    @ApiModelProperty(value = "工序名称")
    private String processName;
}
