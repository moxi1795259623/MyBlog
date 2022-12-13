package com.moxi.blog.mapper;
import org.apache.ibatis.annotations.Param;

import com.moxi.blog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface UserMapper extends BaseMapper<User> {
    User getOneByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

}
