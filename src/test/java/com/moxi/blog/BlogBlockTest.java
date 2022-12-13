package com.moxi.blog;

import com.moxi.blog.service.impl.BlogServiceImpl;
import com.moxi.blog.vo.BlogBlockVo;
import com.moxi.blog.vo.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Slf4j
public class BlogBlockTest {
    @Autowired
    BlogServiceImpl blogService;

    @Test  //ok
    void pageIndexBlogTest(){
        PageBean<BlogBlockVo> blockPage = new PageBean<>();
        blockPage.setCurrentPage(1);
        blockPage.setPageSize(3);
        blockPage = blogService.pageIndexBlog(blockPage);
        System.out.println(blockPage);
    }


    /**
     * 测试getMap方法() 返回值为：
     * isRecommend--->1
     * flag--->1
     * create_time--->2022-12-01 10:49:37.0
     * first_picture--->https://picsum.photos/id/1027/100/100
     * type_id--->1
     * appreciation--->0
     */
    @Test
    void testMap(){
        Map<String, Object> map = blogService.getMap(null);
        System.out.println("----------");
//        map.keySet().stream().forEach(System.out::println);
        for (String s : map.keySet()) {
            System.out.println(s+"--->"+map.get(s));
        }
    }
}
