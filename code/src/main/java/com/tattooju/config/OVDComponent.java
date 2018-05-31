package com.tattooju.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OVDComponent {
	
	@Value("${alibaba.video.accessKeyId}")
	String accessKeyId;
	
	@Value("${alibaba.video.accessKeySecret}")
	String accessKeySecret;
	
	
	
}
