package com.tattooju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.tattooju.entity")
public class TattoojuApp {

	public static void main(String[] args) {
		SpringApplication.run(TattoojuApp.class, args);
	}
	
}
