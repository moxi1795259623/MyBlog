package com.moxi.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moxi.blog.vo.PageBean;
import org.springframework.util.StringUtils;

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
public interface ITypeService extends IService<Type> {

    Page<Type> pageType(String current, String size);

    int saveType(Type type);

    int getTypeByName(String name);

    List<Type> listTypes();

    Map<Type,Integer> typeTop(Integer pageSize);

    Map<Type, Integer> typeTopNotPage();

}
