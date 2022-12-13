package com.moxi.blog.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//public class MybatisTypeHandler extends BaseTypeHandler<Boolean> {
//    /**
//     * 功能描述: <br>
//     *
//     * @param: [ps, i, parameter, jdbcType]
//     * i:Jdbc预编译时设置参数的索引值
//     * parameter:前端参数值  true false
//     * jdbcType: JDBC的类型
//     */
//
//    //这个方法： 前端转后端
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, Boolean param, JdbcType jdbcType) throws SQLException {
//        if (param) {
//            ps.setInt(i, 1);
//        } else {
//            ps.setInt(i, 0);
//        }
//    }
//
//    @Override
//    public Boolean getNullableResult(ResultSet resultSet, String s) throws SQLException {
//        return null;
//    }
//
//    //后端转前端
//    @Override
//    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
//        int x = resultSet.getInt(i);
//        return x == 1 ? true : false;
//    }
//
//    @Override
//    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
//        return null;
//    }
//}
