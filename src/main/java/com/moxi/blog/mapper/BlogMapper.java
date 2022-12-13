package com.moxi.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxi.blog.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface BlogMapper extends BaseMapper<Blog> {

    List<Blog> selectRecommand(@Param("pageSize") Integer pageSize);


    @Update("update t_blog set views = views + 1 where id = #{blogId}")
    void updateViewsById(@Param("blogId") Long blogId);

    List<String> selectYearList();

    List<Blog> selectBlogByYear(@Param("year") String year);
}
