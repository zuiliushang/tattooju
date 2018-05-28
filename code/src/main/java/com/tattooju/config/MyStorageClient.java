package com.tattooju.config;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyStorageClient {
	
	@Autowired
	StorageClient1 storageClient1;
	
	public String[] upload_file(byte[] file_buff, String fileName, NameValuePair[] meta_list) throws IOException, MyException {
		try {
			return  storageClient1.upload_file(file_buff, fileName, meta_list);
		} catch (Exception e) {
			e.printStackTrace();
			return  storageClient1.upload_file(file_buff, fileName, meta_list);
		}
	}

	public void delete_file1(String fileName) throws IOException, MyException {
		try {
			storageClient1.delete_file1(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			storageClient1.delete_file1(fileName);
		}
	}
	
	
}
