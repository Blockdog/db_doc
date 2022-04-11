package com.example.db_doc.util;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;

public class CreatDbDoc {

    public static void main(String[] args) {
        runCreatDbDoc();
    }

    public static void runCreatDbDoc(){
        // 1.获取数据源
        DataSource dataSource = getDataSource();
        // 2.获取数据库文档生成配置（文件路径、文件类型）
        EngineConfig engineConfig = getEngineConfig();
        // 3.获取数据库表的处理配置，可忽略
        ProcessConfig processConfig = getProcessConfig();
        // 4.Screw 完整配置
        Configuration config = getScrewConfig(dataSource, engineConfig, processConfig);
        // 5.执行生成数据库文档
        new DocumentationExecute(config).execute();
    }


    /**
     * 获取数据库源
     */
    private static DataSource getDataSource() {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://172.18.10.133:3306/ump_2.0.5-qz");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("cy888888");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 获取文件生成配置
     */
    private static EngineConfig getEngineConfig() {
        //生成配置
        return EngineConfig.builder()
                //生成文件路径
                .fileOutputDir("D:\\开发工具包\\新建文件夹\\doc")
                //打开目录
                .openOutputDir(true)
                //文件类型
                .fileType(EngineFileType.WORD)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName("ump_2.0.5-qz").build();
    }

    /**
     * 获取数据库表的处理配置，可指定忽略指定表
     */
    private static ProcessConfig getProcessConfig() {
        return ProcessConfig.builder()
                //.designatedTableName(new ArrayList<>(Collections.singletonList("blog")))
                .build();
    }

/*      private static ProcessConfig getProcessConfig() {
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        return ProcessConfig.builder()
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }*/

    private static Configuration getScrewConfig(DataSource dataSource, EngineConfig engineConfig, ProcessConfig processConfig) {
        return Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("数据库设计文档生成")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();
    }

}
