package com.moxi.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxi.blog.entity.Type;
import com.moxi.blog.mapper.BlogPageVoMapper;
import com.moxi.blog.mapper.TypeMapper;
import com.moxi.blog.service.ITypeService;
import com.moxi.blog.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Transactional
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {
    @Autowired
    TypeMapper typeMapper;
    @Autowired
    BlogPageVoMapper blogPageVoMapper;

    /**
     * 无条件type列表
     *
     * @return
     */
    @Override
    public List<Type> listTypes() {
        return typeMapper.selectList(null);
    }

    /**
     * 无条件 分页type【首页展示用】
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public Page<Type> pageType(String current, String size) {
        if (!StringUtils.hasLength(current)) {
            current = "1";
        }
        if (!StringUtils.hasLength(size)) {
            size = "3";
        }
        Page<Type> page = new Page<Type>(Long.valueOf(current), Long.valueOf(size));
        page.addOrder(OrderItem.desc("id"));
        return typeMapper.selectPage(page, null);
    }

    /**
     * 分页 + [type,count]
     *
     * @param pageSize
     * @return [type, count]
     */
    @Transactional
    @Override
    public Map<Type, Integer> typeTop(Integer pageSize) {
        Map<Type, Integer> typeMap = blogCountByType();
        typeMap = CommonUtils.sortByValue(typeMap);
        //限制大小 这里需要学一学流的知识了【果然简单很多】
        //出现的问题，排序又逆转了->再排序一次
        typeMap = typeMap.entrySet().stream().limit(pageSize).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        typeMap = CommonUtils.sortByValue(typeMap);
        return typeMap;
    }

    /**
     * 不分页 + [type,count]
     * @return
     */
    @Override
    public Map<Type, Integer> typeTopNotPage() {
       return  blogCountByType();
    }



    //新增类型
    @Override
    public int saveType(Type type) {
        return typeMapper.saveOneType(type);
    }

    @Override
    public int getTypeByName(String name) {
        return typeMapper.selectByName(name);
    }

    //封装：记录每个类别 和对应类别的博客数量
    public Map<Type, Integer> blogCountByType() {
        Map<Type, Integer> typeMap = new LinkedHashMap<>();
        //1. 查出所有的类别id
        //2. 根据id遍历，获得数量，放到map中  //{typename,num}
        QueryWrapper<Type> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        List<Type> types = typeMapper.selectList(wrapper);
        for (Type type : types) {
            int count = typeMapper.selectTopCount(type.getId());
            typeMap.put(type, count);
        }
        return typeMap;
    }




}
