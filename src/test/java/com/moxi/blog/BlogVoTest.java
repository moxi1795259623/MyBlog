package com.moxi.blog;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.moxi.blog.entity.Blog;
import com.moxi.blog.mapper.BlogMapper;
import com.moxi.blog.mapper.BlogPageVoMapper;
import com.moxi.blog.vo.BlogPageVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class BlogVoTest {
    @Autowired
    BlogPageVoMapper blogPageVoMapper;
    @Autowired
    BlogMapper blogMapper;

    //测试前端数据到来-》条件查询 【目标：rcommand 到来true-->1查询--->返回true】
    @Test
    public void BlogVoListCondtion() {
        //前端数据
        BlogPageVo blogPageVo = new BlogPageVo();
        blogPageVo.setBlogTitle("");
        blogPageVo.setTypeId(1);//转载
        blogPageVo.setRecommend(true);

        //后端查询
        QueryWrapper<BlogPageVo> blogQueryWrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(blogPageVo.getBlogTitle())) {
            //数据库列(不同表 别名的方式),模糊查询的字段--> like "aa"
            blogQueryWrapper.like("title", blogPageVo.getBlogTitle());
        }
        if (blogPageVo.getTypeId() != null) {
            blogQueryWrapper.eq("type_id", blogPageVo.getTypeId());
        }
        //推荐
        if (blogPageVo.isRecommend()) {
            blogQueryWrapper.eq("recommend", blogPageVo.isRecommend());
        }
        PageHelper.startPage(1,2);
        List<BlogPageVo> list = blogPageVoMapper.selectBlogsByCondition(blogQueryWrapper);
        list.forEach(System.out::println);
    }

    /**
     * 方法很好
     * 优点：前端很容易获得每个类型的博客数量：collect.value.size
     * 缺点：查询type的时候用不到那么多的blog信息
     */
    @Test
    public void streamTest(){
        List<Blog> blogs = blogMapper.selectList(null);
        System.out.println("-----------------------------");
        Map<Integer, List<Blog>> collect = blogs.stream().collect(Collectors.groupingBy(Blog::getTypeId));
        for (Integer integer : collect.keySet()) {
            System.out.println(integer+"--->"+collect.get(integer));
        }
    }





}
