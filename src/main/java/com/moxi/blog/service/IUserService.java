package com.moxi.blog.service;

import com.moxi.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface IUserService extends IService<User> {

    User checkUser(String username, String password);

    User getUser(String username,String pwd);
}
