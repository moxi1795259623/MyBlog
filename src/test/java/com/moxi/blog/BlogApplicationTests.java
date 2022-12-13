package com.moxi.blog;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Collections;

@SpringBootTest
@Slf4j
class BlogApplicationTests {
    //记录器 org.slf4j.LoggerFactory; @Slf4j可以替代
//	Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() {
        //我们可以在配置文件中统一修改配置级别，大于等于该级别会输出
        log.trace("跟踪步骤");
        log.debug("调试");
        log.info("springboot默认");
        log.warn("警告");
        log.error("级别最高");
    }

    //输出：数据源类型class com.alibaba.druid.pool.DruidDataSource
    @Test
    void testdataSource() {
        log.info("数据源类型{}", dataSource.getClass());
    }


    //该方法仅适用于mybatis-plus 3.5.1 +以上的版本
  /*  @Test
    void testGenerator() {
        String mapperXMLPath = "D:/Software/Idea/IDEA_2019_x64/blog/src/main/resources/mapper";
        String rootPath = "D:/Software/Idea/IDEA_2019_x64/blog/src/main/java";
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/blog", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("moxi") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(rootPath); // 指定输出目录 直接右键复制项目根目录的绝对路径
                })
                .packageConfig(builder -> {
                    builder
                            .parent("com.moxi.blog") // 设置父包名
//                            .moduleName("blog") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXMLPath));// 设置mapperXml生成路径 直接右键复制项目mapper文件夹的绝对路径

                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user","t_comment","t_type","t_blog","t_tag") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();*/
//    }
}
