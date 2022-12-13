package com.moxi.blog.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonData {
    private int code;
    private String msg;
    private Object data;

    /**
     * 成功 不返回数据
     * @return
     */
    public static JsonData buildSuccess(){
        return new JsonData();
    }

    /**
     * 成功 返回数据
     * @return
     */
    public static JsonData buildSuccess(Object data){
        return new JsonData(1,null,data);
    }

    /**
     * 失败  固定状态码
     * @return
     */
    public static JsonData buildError(String msg){
        return new JsonData(-2,msg,null);
    }

    /**
     * 失败  指定状态码
     * @return
     */
    public static JsonData buildError(int code,String msg){
        return new JsonData(code,msg,null);
    }
}
