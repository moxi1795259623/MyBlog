package com.moxi.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.moxi.blog.vo.BlogBlockVo;
import com.moxi.blog.vo.BlogPageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 封装Blog查询条件
 */
public interface BlogPageVoMapper extends BaseMapper<BlogPageVo> {

    @Select("select b.id as blog_id, b.title as blogTitle, b.update_time as updateTime,b.description, b.is_published as published," +
            " b.recommend ,t.id as type_id, t.`name` as typeName from t_blog b left join t_type t on  b.type_id = t.id " + "${ew.customSqlSegment}")
    List<BlogPageVo> selectBlogsByCondition(@Param(Constants.WRAPPER) QueryWrapper<BlogPageVo> blogQueryWrapper);

    int countBlogs(@Param(Constants.WRAPPER) QueryWrapper<BlogPageVo> blogQueryWrapper);

    List<BlogPageVo> selectBlogsByCondition1(BlogPageVo blogPageVo);

    List<BlogBlockVo> selectBlogInfo();

    int countBlogInfo();

    List<BlogBlockVo> selectBlogsByFuzzy(@Param("query") String query);

    int countBlogFuzzy(@Param("query") String query);

    BlogBlockVo selectBlogInfoById(@Param("blogId") Long blogId);

    List<BlogBlockVo> selectPageBlogByTypeId(@Param("typeId") Long typeId);

    int countBlogInfoyTypeId(@Param("typeId")Long typeId);

    int countBlogInfoyTagId(@Param("tagId") Long tagId);
}
