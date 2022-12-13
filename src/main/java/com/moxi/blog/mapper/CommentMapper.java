package com.moxi.blog.mapper;

import com.moxi.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select nick_name from t_comment where id = (select parent_reply_id from t_comment where id = #{id})")
    String selParentNickNameById(@Param("id") Integer id);
}
