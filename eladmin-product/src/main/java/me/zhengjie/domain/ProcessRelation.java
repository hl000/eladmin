package me.zhengjie.domain;

/**
 * @author HL
 * @create 2021/5/28 10:31
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="report_process_relation")
public class ProcessRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @Column(name = "process")
    @ApiModelProperty(value = "流程")
    private String process;

    @Column(name = "project_order")
    @ApiModelProperty(value = "序号")
    private Integer projectOrder;

    @JoinColumn(name = "report_product_parameter_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductParameter productParameter;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

}
