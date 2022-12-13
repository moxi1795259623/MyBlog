package com.moxi.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.vo.BlogBlockVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface TagMapper extends BaseMapper<Tag> {

    void saveBlogTags(HashMap map);

    List<Integer> getTags(@Param("blogId") Integer blogId);

    void deletebyBlogId(@Param("blogId") Long blogId);

    int selectByName(String tagName);

    int saveOneTag(@Param("tag")Tag tag);


    List<Tag> tagTop(Integer pageSize);

    Tag selectOneTag(Integer blogId);

    int selectBlogCountByTagId(@Param("tagId") Long tagId);

    List<Tag> selectAllTag(@Param("blogId") Integer blogId);

    List<Integer> selectBlogBlockVoByTypeId(Long tagId);
}
