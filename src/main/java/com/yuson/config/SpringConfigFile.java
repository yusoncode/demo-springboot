package com.yuson.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 自定义读取文件
 *
 * @author yuson
 */
@Configuration
public class SpringConfigFile {
    private static final Logger logger = LoggerFactory.getLogger(SpringConfigFile.class);

    private static final String[] ymlFileNameArray = {"task.yml"};
    private static final String[] propertiesFileNameArray = {"task.properties"};

    /**
     * 自定义读取yml跟properties文件
     * 通过启动参数获取FileSystem绝对路径, 没有则默认classpath相对路径
     *
     * @return
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer configurerFile() throws IOException {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        String path = System.getProperty("spring.config.location");
        //yml
        Properties yamlObject = setYmlFile(path);

        //properties
        Properties propertiesObject = setPropertiesFile(path);

        configurer.setPropertiesArray(yamlObject, propertiesObject);
        return configurer;
    }

    private Properties setYmlFile(String path) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        //yml
        List<FileSystemResource> fileSystemResourceList = new ArrayList<>();
        List<ClassPathResource> classPathResourceList = new ArrayList<>();
        addFile(path, ymlFileNameArray, fileSystemResourceList, classPathResourceList);

        if (!fileSystemResourceList.isEmpty()) {
            yaml.setResources(fileSystemResourceList.toArray(new FileSystemResource[fileSystemResourceList.size()]));
        }

        if (!classPathResourceList.isEmpty()) {
            yaml.setResources(classPathResourceList.toArray(new ClassPathResource[classPathResourceList.size()]));
        }
        return yaml.getObject();
    }

    private Properties setPropertiesFile(String path) throws IOException {
        PropertiesFactoryBean properties = new PropertiesFactoryBean();

        List<FileSystemResource> fileSystemResourceList = new ArrayList<>();
        List<ClassPathResource> classPathResourceList = new ArrayList<>();
        addFile(path, propertiesFileNameArray, fileSystemResourceList, classPathResourceList);

        if (!fileSystemResourceList.isEmpty()) {
            properties.setLocations(fileSystemResourceList.toArray(new FileSystemResource[fileSystemResourceList.size()]));
        }

        if (!classPathResourceList.isEmpty()) {
            properties.setLocations(classPathResourceList.toArray(new ClassPathResource[classPathResourceList.size()]));
        }
        properties.afterPropertiesSet();
        return properties.getObject();
    }

    private void addFile(String path, String[] fileNameArray, List<FileSystemResource> fileSystemResourceList, List<ClassPathResource> classPathResourceList) {
        for (int i = 0; i < fileNameArray.length; i++) {
            if (path == null) {
                logger.info("加载自定义配置文件-ClassPath: {}", fileNameArray[i]);
                classPathResourceList.add(new ClassPathResource(fileNameArray[i]));
            } else {
                String filePath = path + fileNameArray[i];
                logger.info("加载自定义配置文件-FileSystem: filePath={}", filePath);
                fileSystemResourceList.add(new FileSystemResource(filePath));
            }
        }

    }


}
