package com.soldier.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地址管理
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 性别 0 女 1 男
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省级区划编号
     */
    private String provinceCode;

    /**
     * 省级名称
     */
    private String provinceName;

    /**
     * 市级区划编号
     */
    private String cityCode;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 区级区划编号
     */
    private String districtCode;

    /**
     * 区级名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 标签
     */
    private String label;

    /**
     * 默认 0 否 1是
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 是否删除
     */
    private Integer isDeleted;


}
