package com.moxi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.moxi.blog.entity.Blog;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.entity.Type;
import com.moxi.blog.mapper.BlogMapper;
import com.moxi.blog.mapper.BlogPageVoMapper;
import com.moxi.blog.mapper.TagMapper;
import com.moxi.blog.mapper.TypeMapper;
import com.moxi.blog.service.IBlogService;
import com.moxi.blog.utils.MarkdownUtils;
import com.moxi.blog.vo.BlogBlockVo;
import com.moxi.blog.vo.BlogPageVo;
import com.moxi.blog.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogPageVoMapper blogPageVoMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    TypeMapper typeMapper;

    /**
     * 检查页码信息
     *
     * @param pageBean
     * @return
     */
    PageBean checkPageInfo(PageBean pageBean) {
        int current = pageBean.getCurrentPage();
        int size = pageBean.getPageSize();
        if (!StringUtils.hasLength(String.valueOf(current))) {
            pageBean.setCurrentPage(1);
        }
        if (!StringUtils.hasLength(String.valueOf(size))) {
            pageBean.setPageSize(3);
        }
        return pageBean;
    }


    /**
     * 条件分页查询
     *
     * @param pageBean
     * @param blogPageVo
     * @return
     */
    @Override
    @Transactional
    public PageBean<BlogPageVo> pageBlog(PageBean pageBean, BlogPageVo blogPageVo) {
        pageBean = checkPageInfo(pageBean);
        //条件构造器
        QueryWrapper<BlogPageVo> blogQueryWrapper = new QueryWrapper<>();
        if (blogPageVo != null) {
            blogQueryWrapper = blogsCondition(blogQueryWrapper, blogPageVo);
        }
        //分页插件
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //代码组装条件 + sql补充剩余条件
        List<BlogPageVo> allItems = blogPageVoMapper.selectBlogsByCondition(blogQueryWrapper);
        //封装返回
        int countNums = blogPageVoMapper.countBlogs(blogQueryWrapper);            //总记录数
        PageBean<BlogPageVo> pageData = new PageBean(pageBean.getCurrentPage(), pageBean.getPageSize(), countNums);
        pageData.setItems(allItems);
        return pageData;
    }

    /**
     * 后台新增和修改
     *
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public int saveBlog(Blog blog) {
        if (blog.getId() == null) {//新增
            blog.setCreateTime(LocalDateTime.now());
            blog.setUpdateTime(LocalDateTime.now());
            blog.setViews(0);
            return blogMapper.insert(blog);
        } else {//修改
            blog.setUpdateTime(LocalDateTime.now());
            return blogMapper.updateById(blog);
        }
    }

    /**
     * 首页每个博客块  分页+无条件博客块
     *
     * @param pageBean
     * @return
     */
    @Transactional
    @Override
    public PageBean<BlogBlockVo> pageIndexBlog(PageBean pageBean) {
        pageBean = checkPageInfo(pageBean);
        //分页插件
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //用户和博客信息ok
        List<BlogBlockVo> blogBlockVos = blogPageVoMapper.selectBlogInfo();
        //标签信息
        for (BlogBlockVo blogBlockVo : blogBlockVos) {
            //根据id获得标签信息
            Tag t = tagMapper.selectOneTag(blogBlockVo.getBlogId());
            ArrayList<Tag> tags = new ArrayList<>();
            tags.add(t);
            blogBlockVo.setTag(tags);
        }
        getType(blogBlockVos);
        //总记录数
        int countNums = blogPageVoMapper.countBlogInfo();            //总记录数
        return savePageBean(pageBean, blogBlockVos, countNums);
    }

    /**
     * 首页点击类型进入列表 分页+博客块typeid
     *
     * @param pageBean
     * @param typeId
     * @return
     */
    @Override
    public PageBean selectPageBlogByTypeId(PageBean pageBean, Long typeId) {
        pageBean = checkPageInfo(pageBean);
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<BlogBlockVo> blogBlockVos = blogPageVoMapper.selectPageBlogByTypeId(typeId);
        /*for (BlogBlockVo blogBlockVo : blogBlockVos) {
            Type type = blogBlockVo.getType();
            type.setName(typeMapper.selectById(typeId).getName());
        }*/
        getType(blogBlockVos);
        int countNums = blogPageVoMapper.countBlogInfoyTypeId(typeId);            //总记录数
        return savePageBean(pageBean, blogBlockVos, countNums);
    }

    /**
     * 首页点击标签进入列表 分页+博客块tagid
     *
     * @param pageBean
     * @param tagId
     * @return
     */
    @Override
    public PageBean selectPageBlogByTagId(PageBean pageBean, Long tagId) {
        pageBean = checkPageInfo(pageBean);
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //去中间表查询tag对应的blogid集合
        List<Integer> blogIds = tagMapper.selectBlogBlockVoByTypeId(tagId);
        //根据blogId查询 List<BlogBlockVo> blogBlockVos
        ArrayList<BlogBlockVo> blogBlockVos = new ArrayList<>();
        for (Integer blogId : blogIds) {
            BlogBlockVo bvo = blogPageVoMapper.selectBlogInfoById(Long.valueOf(blogId));
            //把该博客的标签信息添加进去
            List<Tag> tagList = tagMapper.selectAllTag(bvo.getBlogId());
            bvo.setTag(tagList);
            blogBlockVos.add(bvo);
            Type type = typeMapper.selectOneType(bvo.getType().getId());
            bvo.setType(type);
        }
        int countNums = blogPageVoMapper.countBlogInfoyTagId(tagId);            //总记录数
        return savePageBean(pageBean, blogBlockVos, countNums);
    }

    /**
     * 返回归档信息 <年份,该年份的博客列表>
     * @return  Map<String, List<Blog>>
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        Map<String, List<Blog>> blogYearMap = new LinkedHashMap<>();
        //先获得年份
        List<String> yearList = blogMapper.selectYearList();
        for (String year : yearList) {
            List<Blog> blogList = blogMapper.selectBlogByYear(year);
            blogYearMap.put(year, blogList);
        }
        return blogYearMap;
    }

    @Override
    public int blogCount() {
        return blogMapper.selectCount(null);
    }

    /**
     * 推荐列表
     *
     * @param pageSize
     * @return
     */
    @Override
    public List<Blog> getRecommandTitle(Integer pageSize) {
        return blogMapper.selectRecommand(pageSize);
    }

    /**
     * 全局搜索
     *
     * @param pageBean
     * @param query
     * @return
     */
    @Override
    public PageBean<BlogBlockVo> selectBlogByQuery(PageBean pageBean, String query) {
        pageBean = checkPageInfo(pageBean);
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<BlogBlockVo> blogBlockVos = blogPageVoMapper.selectBlogsByFuzzy(query);
        getType(blogBlockVos);
        //总记录数
        int countNums = blogPageVoMapper.countBlogFuzzy(query); //总记录数
        return savePageBean(pageBean, blogBlockVos, countNums);
    }

    /**
     * 首页点击博客返回详情
     *
     * @param blogId
     * @return
     */
    @Transactional
    @Override
    public BlogBlockVo getBlogDetailsById(Long blogId) {
        blogMapper.updateViewsById(blogId);
        //用户和博客信息-->根据id
        BlogBlockVo blogBlockVo = blogPageVoMapper.selectBlogInfoById(blogId);
        //这里的content内容要转换下 markdown->html
        blogBlockVo.setContent(MarkdownUtils.markdownToHtmlExtensions(blogBlockVo.getContent()));
        //标签信息
        List<Tag> tag = tagMapper.selectAllTag(blogBlockVo.getBlogId());
        blogBlockVo.setTag(tag);
        //类型信息
        Type type = typeMapper.selectOneType(blogBlockVo.getType().getId());
        blogBlockVo.setType(type);
        return blogBlockVo;
    }

    //封装 --条件判断和封装【全局搜索用】
    private QueryWrapper<BlogPageVo> blogsCondition(QueryWrapper<BlogPageVo> blogQueryWrapper, BlogPageVo blogPageVo) {
        //全部查询需要条件  条件查询不需要id的连接条件
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
        blogQueryWrapper.orderByDesc("b.id");
        return blogQueryWrapper;
    }

    //封装 -- 根据blogid列表获得类型信息
    public void getType(List<BlogBlockVo> blogBlockVos) {
        for (BlogBlockVo blogBlockVo : blogBlockVos) {
            //根据id获得类型信息
            Type t = typeMapper.selectOneType(blogBlockVo.getType().getId());
            blogBlockVo.setType(t);
        }
    }

    //封装 -- 分页结果数据封装
    public PageBean<BlogBlockVo> savePageBean(PageBean pageBean, List<BlogBlockVo> blogBlockVos, int countNums) {
        PageBean<BlogBlockVo> pageData = new PageBean(pageBean.getCurrentPage(), pageBean.getPageSize(), countNums);
        //封装返回
        pageData.setItems(blogBlockVos);
        return pageData;
    }

}
