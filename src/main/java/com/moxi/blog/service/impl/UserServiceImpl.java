package com.moxi.blog.service.impl;

import com.moxi.blog.entity.User;
import com.moxi.blog.mapper.UserMapper;
import com.moxi.blog.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxi.blog.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper mapper;

    @Override
    public User checkUser(String username, String password) {
        return mapper.getOneByUserNameAndPassword(username, CommonUtils.MD5(password));
    }

    @Override
    public User getUser(String username, String password) {
       return mapper.getOneByUserNameAndPassword(username, CommonUtils.MD5(password));
    }
}
