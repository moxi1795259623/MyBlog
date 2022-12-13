package com.moxi.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moxi.blog.vo.BlogBlockVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface TypeMapper extends BaseMapper<Type> {
     int saveOneType(@Param("type")Type type);

     int selectByName(@Param("name")String typeName);


//    List<Type> selectTypeByIds(@Param("set")Set<Integer> keySet);

    int selectTopCount(@Param("typeId") Integer id);

    Type selectOneType(@Param("blogId") Integer blogId);

}
