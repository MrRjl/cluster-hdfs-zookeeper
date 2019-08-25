package com.cluster.common.hadoopConfig;


import java.net.URI;

import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * HDFS  Configuration
 * @author Administrator
 *
 */
@Configuration
public class HadoopConfig {
	private static final Logger logger = LoggerFactory.getLogger(HadoopConfig.class);
    @Value("${spring.hadoop.config.fs.defaultFS}")
    private String defaultFS;
    @Value("${spring.hadoop.config.user}")
    private String user;

    @Bean
    public org.apache.hadoop.conf.Configuration getConfiguration(){
    	org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
    	//conf.set("fs.defalutFS", defaultFS.trim());
        //conf.set("dfs.replication", "1");
    	//conf.set("user", user.trim());
    	return conf;
    }
    @Bean
    public FileSystem getFileSystem(){
        FileSystem fs = null;
        try {
            //fs = FileSystem.get(new URI(nameNode.trim()),conf,"user");
            fs = FileSystem.get(new URI(defaultFS.trim()),getConfiguration(),user.trim());
            logger.info("Hadoop Configuration success !");
        } catch (Exception e) {
        	logger.error("Hadoop Configuration error : {}", e);
        }
        return  fs;
    }
    
}
