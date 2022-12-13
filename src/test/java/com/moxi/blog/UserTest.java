package com.moxi.blog;

import com.moxi.blog.entity.User;
import com.moxi.blog.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserTest {

    //测试密码加密
    @Test
    public void testUserPwd(){
        User user = new User("moxi","123456");
        String md5 = CommonUtils.MD5(user.getPassword());
        System.out.println(md5);
    }

}
