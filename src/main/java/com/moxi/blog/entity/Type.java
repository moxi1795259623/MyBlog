package com.moxi.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.moxi.blog.annotation.EncryptId;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@TableName("t_type")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class) //类型名称修改的时候不能为空
    private Integer id;

    /**
     * 类型名称
     */
//    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY)
     //保存的时候校验，更新的时候不用校验，用于分类的类信息
    @NotBlank(message = "用户名不能为空~",groups = Save.class) //只能作用在String上，不能为null，而且调用trim()后，长度必须大于0
    private String name;

    @EncryptId
    @TableField(exist = false)
    private String testId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "Type{" +
            "id=" + id +
            ", name=" + name +
        "}";
    }

    /**
     * 保存的时候校验分组
     */
    public interface Save {
    }

    /**
     * 更新的时候校验分组
     */
    public interface Update {
    }
}
