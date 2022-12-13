package com.moxi.blog;

import com.moxi.blog.entity.Type;
import com.moxi.blog.service.impl.TypeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Slf4j
public class TypeServiceTest {
    @Autowired
    TypeServiceImpl typeService;

    //测试 ok
   /* @Test
    void testTypeTop(){
        Map<String, Integer> typeIntegerMap = typeService.typeTop(3);
        System.out.println(typeIntegerMap.toString());
    }*/
}
