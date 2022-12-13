package com.moxi.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxi.blog.entity.Blog;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.mapper.TagMapper;
import com.moxi.blog.service.ITagService;
import com.moxi.blog.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    TagMapper tagMapper;

    @Override
    public List<Tag> listTags() {
        return tagMapper.selectList(null);
    }

    /**
     * 保存中间表blogid-tagid
     *
     * @param id
     * @param blog
     */
    @Override
    public void saveBlogTags(Integer id, Blog blog) {
        List<Long> tagIdList = convertToList(blog.getTagIds());
        HashMap map = new HashMap<>();
        map.put("id", id);
        map.put("tagIds", tagIdList);
        tagMapper.saveBlogTags(map);
    }

    /**
     * 对tags[1,2,3]进行解析->List<Tag>
     *
     * @param tagIds
     * @return
     */
    public List<Long> convertToList(String tagIds) {
        List<Long> tagList = new ArrayList<>();
        if (tagIds != "" && tagIds != null) {
            String[] tags = tagIds.split(",");
            for (String tag : tags) {
                tagList.add(Long.valueOf(tag));
            }
        }
        return tagList;
    }

    /**
     * 根据blogid获得所有的标签Ids
     *
     * @param blogId
     * @return
     */
    @Override
    public List<Integer> getTags(Integer blogId) {
        return tagMapper.getTags(blogId);
    }

    /**
     * 删除中间表
     *
     * @param blogId 博客ID
     */
    @Override
    public void removeByBlogId(Long blogId) {
        tagMapper.deletebyBlogId(blogId);
    }

    /**
     * Mybatis 标签+分页
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public Page<Tag> pageTag(String current, String size) {
        if (!StringUtils.hasLength(current)) {
            current = "1";
        }
        if (!StringUtils.hasLength(size)) {
            size = "3";
        }
        Page<Tag> page = new Page<>(Long.valueOf(current), Long.valueOf(size));
        page.addOrder(OrderItem.desc("id"));
        return tagMapper.selectPage(page, null);
    }


    /**
     * 类型是否存在
     *
     * @param tagName 标签名
     * @return
     */
    @Override
    public int getTagByName(String tagName) {
        return tagMapper.selectByName(tagName);
    }

    /**
     * 保存标签 单表t_tags
     *
     * @param tag
     * @return
     */
    @Override
    public int saveTag(Tag tag) {
        return tagMapper.saveOneTag(tag);
    }


    /**
     *  记录每个标签 和对应标签的博客数量
     * @return
     */
    public Map<Tag, Integer> blogCountByTag() {
        Map<Tag, Integer> tagMap = new LinkedHashMap<>();
        List<Tag> tags = tagMapper.selectList(null);
        for (Tag tag : tags) {
            int count = tagMapper.selectBlogCountByTagId(Long.valueOf(tag.getId()));
            tagMap.put(tag, count);
        }
        return tagMap;
    }

    /**
     * 首页标签列表 分页+统计类型数量+排序
     * @param pageSize
     * @return
     */
    @Override
    public Map<Tag, Integer> tagTop(Integer pageSize) {
        Map<Tag, Integer> tagMap = blogCountByTag();
        tagMap = CommonUtils.sortByValue(tagMap);
        tagMap = tagMap.entrySet().stream().limit(pageSize).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return tagMap;
    }

    /**
     * 不分页 + [tag,count]
     * @return
     */
    @Override
    public Map<Tag, Integer> tagTopNotPage() {
        return blogCountByTag();
    }


}
