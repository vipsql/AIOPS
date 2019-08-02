package com.coocaa.common.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-21 15:51
 * @Description: MyBatis-plus 代码生成器
 */
public class CodeGeneratorUtil {

    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();

        GlobalConfig globalConfig = new GlobalConfig();
        String projectDir = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectDir + "/user_message/src/main/java");
        globalConfig.setFileOverride(false);
        globalConfig.setActiveRecord(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setAuthor("wyx");
        globalConfig.setOpen(false);
        autoGenerator.setGlobalConfig(globalConfig);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/home_work?allowMultiQueries=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=CTT");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        autoGenerator.setDataSource(dataSourceConfig);

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.no_change);
        strategyConfig.setInclude("user", "user_message");
        autoGenerator.setStrategy(strategyConfig);

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.message.user_message");
        autoGenerator.setPackageInfo(packageConfig);

        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        String templatePath = "/templates/mapper.xml.ftl";
        List<FileOutConfig> fileOutConfigs = new ArrayList<>();
        fileOutConfigs.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectDir + "/user_message/src/main/java/com/message/user_message/mapper/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(fileOutConfigs);
        autoGenerator.setCfg(injectionConfig);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        autoGenerator.execute();
    }

}
