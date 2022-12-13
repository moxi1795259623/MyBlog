package com.moxi.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.entity.Blog;
import com.moxi.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moxi.blog.entity.Type;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface ITagService extends IService<Tag> {

    List<Tag> listTags();

    void saveBlogTags(Integer id, Blog blog);

    List<Integer> getTags(Integer id);

    void removeByBlogId(Long id);

    Page<Tag> pageTag(String current, String size);

    int getTagByName(String tagName);

    int saveTag(Tag tag);


    Map<Tag, Integer> tagTop(Integer pageSize);

    Map<Tag, Integer> tagTopNotPage();
}
