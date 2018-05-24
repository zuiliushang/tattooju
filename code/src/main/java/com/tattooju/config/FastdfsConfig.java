package com.tattooju.config;

import java.io.IOException;
import java.util.Properties;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastdfsConfig {

	@Value("${fdfs.tracker_server}")
	private String trackerServer;
	
	@Value("${fdfs.connect_timeout}")
	private String connectTimeout;
	
	@Value("${fdfs.network_timeout}")
	private String networkTimeout;
	
	@Value("${fdfs.charset}")
	private String charset;
	
	@Bean
	public StorageClient1 storageClient() throws IOException, MyException {
		Properties properties = new Properties();
		properties.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, trackerServer);
		properties.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, connectTimeout);
		properties.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, networkTimeout);
		properties.put(ClientGlobal.DEFAULT_CHARSET, charset);
		ClientGlobal.initByProperties(properties);
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		return storageClient;
	}
	
}
