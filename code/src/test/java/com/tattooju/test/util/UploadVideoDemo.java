package com.tattooju.test.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse.VideoMeta;

/**
 * 以下Java示例代码演示了如何在服务端上传文件至视频点播。 目前支持两种方式上传：
 * 1.上传本地文件，使用分片上传，并支持断点续传，参见testUploadVideo函数。 1.1
 * 当断点续传关闭时，最大支持上传任务执行时间为3000秒，具体可上传文件大小与您的网络带宽及磁盘读写能力有关。 1.2
 * 当断点续传开启时，最大支持48.8TB的单个文件，注意，断点续传开启后，上传任务执行过程中，同时会将当前上传位置写入本地磁盘文件，影响您上传文件的速度，请您根据文件大小选择是否开启
 * 2.上传网络流，可指定文件URL进行上传，不支持断点续传，最大支持5GB的单个文件。参见testUploadURLStream函数。
 * 请替换示例中的必选参数，示例中的可选参数如果您不需要设置，请将其删除，以免设置无效参数值与您的预期不符。
 */
public class UploadVideoDemo {
	// 账号AK信息请填写(必选)
	private static final String accessKeyId = "";
	// 账号AK信息请填写(必选)
	private static final String accessKeySecret = "";

	public static void main(String[] args) {
		getVideoPlay("2e43d795e06b4a669c9a99c52a0e7385");//187ff1b25aab4813aba05135426dc6cf
		
	}

	private static void getVideoPlay( String videoId) {
		DefaultAcsClient client = new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));
		GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
	    request.setVideoId(videoId);
	    GetVideoPlayAuthResponse response = null;
	    try {
	        response = client.getAcsResponse(request);
	    } catch (ServerException e) {
	        throw new RuntimeException("GetVideoPlayAuthRequest Server failed");
	    } catch (ClientException e) {
	        throw new RuntimeException("GetVideoPlayAuthRequest Client failed");
	    }
	    /**
	     * 
			playAuth = eyJTZWN1cml0eVRva2VuIjoiQ0FJUzN3SjFxNkZ0NUIyeWZTaklyNG5mQThqNXF1MXg0N1MrVHg3WTNVVXNmY3B1cnIzeXVqejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsck1xRzhRWkd4YWFNWllndHNvSXIxdndKcExGc3QySjZyOEpqc1V2ZzlWWDJGcXBzdlhKYXNEVkVma3VFNVhFTWlJNS8wMGU2TC8rY2lyWVhEN0JHSmFWaUpsaFE4MEtWdzJqRjFSdkQ4dFhJUTBRazYxOUszemRaOW1nTGlidWkzdnhDa1J2MkhCaWptOHR4cW1qL015UTV4MzFpMXYweStCM3dZSHRPY3FjYThCOU1ZMVdUc3Uxdm9oemFyR1Q2Q3BaK2psTStxQVU2cWxZNG1YcnM5cUhFa0ZOd0JpWFNaMjJsT2RpTndoa2ZLTTNOcmRacGZ6bjc1MUN0L2ZVaXA3OHhtUW1YNGdYY1Z5R0dOLzZuNU9aUXJ6emI0WmhKZWVsQVJtWGpJRFRiS3VTbWhnL2ZIY1dPRGxOZjljY01YSnFBWFF1TUdxQ2QvTDlwdzJYT2x6NUd2WFZnUHRuaTRBSjVsSHA3TWVNR1YrRGVMeVF5aDBFSWFVN2EwNDQvNWVUWWFwazFNVWFnQUVpNHNTelZ0VHIzejRLS0JqZUppVFBlMW9WUThUVVhBSHhlQm5oeVl0NVRQalBkY0JCUFF4S3ZucjVRSkQ2UVdYMnQ3L2xWRzVQK1EzUk95MHoxTlgwZVh1cUtnV0hOaCtCYS9FSHBvNkdtUlQwTDk2Q0J4QUpoUXQvMmllbStZYWlUYUpGUnpvVk9jQzFuV0wyZHB6eXN6d2pybHZ5d1VzTitaMXJvbjhEZ1E9PSIsIkF1dGhJbmZvIjoie1wiQ2FsbGVyXCI6XCJhZEI4SkNIZ2JFQmlNdUwwQmNwTnZBaHZYcmxZUnh3OERlY3FxZ0o5Y2tJPVxcclxcblwiLFwiRXhwaXJlVGltZVwiOlwiMjAxOC0wNS0zMVQwODo0NjowMlpcIixcIk1lZGlhSWRcIjpcIjJlNDNkNzk1ZTA2YjRhNjY5YzlhOTljNTJhMGU3Mzg1XCIsXCJTaWduYXR1cmVcIjpcIklYWW9nVFJFNG9tRUJtYTUxazJEYVhDZFRPTT1cIn0iLCJWaWRlb01ldGEiOnsiU3RhdHVzIjoiTm9ybWFsIiwiVmlkZW9JZCI6IjJlNDNkNzk1ZTA2YjRhNjY5YzlhOTljNTJhMGU3Mzg1IiwiVGl0bGUiOiLmtYvor5XmoIfpopgiLCJDb3ZlclVSTCI6Imh0dHA6Ly9tZWRpYS50YXR0b29qdS5jb20vMmU0M2Q3OTVlMDZiNGE2NjljOWE5OWM1MmEwZTczODUvY292ZXJzL2ViZWNmNzNiNmU3ZjQ5NTU4NGJhMmE3MzJmYjhmYTU3LTAwMDAxLmpwZyIsIkR1cmF0aW9uIjozLjIxNX0sIkFjY2Vzc0tleUlkIjoiU1RTLk5KakhyTUcyUFR2VU04aTlFeXFFQkF4WVgiLCJQbGF5RG9tYWluIjoibWVkaWEudGF0dG9vanUuY29tIiwiQWNjZXNzS2V5U2VjcmV0IjoiR1pKSFpOWXRmU3Q5cm92Qnd2RGJXQzIybXlQMm00OHhIWTNDNmZnM3lCYVAiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSIsIkN1c3RvbWVySWQiOjE4MzQ2MjY0NTgwNjc2MDl9
			videoMeta = com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse$VideoMeta@28eaa59a
			url = http://vod.cn-shanghai.aliyuncs.com/?SignatureVersion=1.0&Action=GetVideoPlayAuth&Format=XML&VideoId=2e43d795e06b4a669c9a99c52a0e7385&SignatureNonce=93ec0ede-0a71-45b3-a289-0c7e8b17506c&Version=2017-03-21&AccessKeyId=LTAIKGcm5gyqIHSZ&Signature=eajs8DPsW8FP5pMrR5A58RWUfeo%3D&SignatureMethod=HMAC-SHA1&RegionId=cn-shanghai&Timestamp=2018-05-31T08%3A44%3A19Z
	     * 
	     * Normal
	     */
	    VideoMeta videoMeta = response.getVideoMeta();
	    
	    System.out.println("videoMeta = "+response.getVideoMeta());
	    System.out.println("playAuth = "+response.getPlayAuth());
	    System.out.println("coverURL = " + videoMeta.getCoverURL());
	    System.out.println("status=" + videoMeta.getStatus());
	    System.out.println("title="+videoMeta.getTitle());
	    System.out.println("duration="+videoMeta.getDuration());
	}
	
	private static void refreshUploadVideo(DefaultAcsClient client, String videoId) {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        RefreshUploadVideoResponse response = null;
        try {
              request.setVideoId(videoId);
              response = client.getAcsResponse(request);
        } catch (ServerException e) {
              System.out.println("RefreshUploadVideoRequest Server Exception:");
              e.printStackTrace();
              return;
        } catch (ClientException e) {
              System.out.println("RefreshUploadVideoRequest Client Exception:");
              e.printStackTrace();
              return;
        }
        System.out.println("RequestId:" + response.getRequestId());
        System.out.println("UploadAuth:" + response.getUploadAuth());
  }
	
	private static void testCreate() {
		DefaultAcsClient client = new DefaultAcsClient(DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));
		CreateUploadVideoRequest request = new CreateUploadVideoRequest();
	    CreateUploadVideoResponse response = null;
	    byte[] bs;
		try {
			bs = Files.readAllBytes(new File("E:\\C360VID_20180529_222621.mp4").toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		}
		try {
		      /*必选，视频源文件名称（必须带后缀, 支持 "3GP","AVI","FLV","MP4","M3U8","MPG","ASF","WMV","MKV","MOV","TS","WebM","MPEG","RM","RMVB","DAT","ASX","WVX","MPE","MPA","F4V","MTS","VOB","GIF"）*/
		      request.setFileName("C360VID_20180529_222621.mp4");//上传的视频文件名称
		      //必选，视频源文件字节数
		      request.setFileSize(Long.valueOf(bs.length));//;上传视频的大小
		      //必选，视频标题
		      request.setTitle("测试测试"); //上传视频的标题
		      response = client.getAcsResponse(request);
		    } catch (ServerException e) {
		      System.out.println("CreateUploadVideoRequest Server Exception:");
		      e.printStackTrace();
		    } catch (ClientException e) {
		      System.out.println("CreateUploadVideoRequest Client Exception:");
		      e.printStackTrace();
		    }
		    System.out.println("UploadAuth:"+response.getUploadAuth());
		    System.out.println("UploadAddress:"+response.getUploadAddress());
		    System.out.println("videoId:"+response.getVideoId()); //videoId很重要，后续播放的时候需要用到，要记得保存
		
		/*// 视频标题(必选)
		String title = "测试标题";
		// 1.本地文件上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
		// 2.网络流上传时，文件名称为源文件名，如文件名称.mp4(必选)。任何上传方式文件名必须包含扩展名
		String fileName = "E:\\C360VID_20180529_222621.mp4";
		// 本地文件上传
		//testUploadVideo(accessKeyId, accessKeySecret, title, fileName);
		// 待上传视频的网络流地址
		
		 * String url = "http://video.sample.com/sample.mp4"; //网络流上传
		 * testUploadURLStream(accessKeyId, accessKeySecret, title, fileName, url);
		 
		testCreate();*/
	}

	/**
	 * 本地文件上传接口
	 * 
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param title
	 * @param fileName
	 */
	/*private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
		UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
		 可指定分片上传时每个分片的大小，默认为1M字节 
		request.setPartSize(1 * 1024 * 1024L);
		 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定） 
		request.setTaskNum(1);
		
		 * 是否开启断点续传,
		 * 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。 注意:
		 * 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启
		 
		request.setEnableCheckpoint(false);
		
		 * OSS慢请求日志打印超时时间，是指每个分片上传时间超过该阈值时会打印debug日志，如果想屏蔽此日志，请调整该阈值。单位: 毫秒，默认为300000毫秒
		 
		// request.setSlowRequestsThreshold(300000L);
		 可指定每个分片慢请求时打印日志的时间阈值，默认为300s 
		// request.setSlowRequestsThreshold(300000L);
		 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印 
		// request.setIsShowWaterMark(true);
		
		 * 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档
		 * https://help.aliyun.com/document_detail/57029.html
		 
		// request.setCallback("http://callback.sample.com");
		 视频分类ID(可选) 
		// request.setCateId(0);
		 视频标签,多个用逗号分隔(可选) 
		// request.setTags("标签1,标签2");
		 视频描述(可选) 
		// request.setDescription("视频描述");
		 封面图片(可选) 
		// request.setCoverURL("http://cover.sample.com/sample.jpg");
		 模板组ID(可选) 
		// request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
		 存储区域(可选) 
		// request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
		UploadVideoImpl uploader = new UploadVideoImpl();
		UploadVideoResponse response = uploader.uploadVideo(request);
		System.out.print("RequestId=" + response.getRequestId() + "\n"); // 请求视频点播服务的请求ID
		if (response.isSuccess()) {
			System.out.print("VideoId=" + response.getVideoId() + "\n");
		} else {
			
			 * 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，
			 * 此时需要根据返回错误码分析具体错误原因
			 
			System.out.print("VideoId=" + response.getVideoId() + "\n");
			System.out.print("ErrorCode=" + response.getCode() + "\n");
			System.out.print("ErrorMessage=" + response.getMessage() + "\n");
		}
	}

	*//**
	 * 网络流上传接口
	 * 
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param title
	 * @param fileName
	 * @param url
	 *//*
	private static void testUploadURLStream(String accessKeyId, String accessKeySecret, String title, String fileName,
			String url) {
		UploadURLStreamRequest request = new UploadURLStreamRequest(accessKeyId, accessKeySecret, title, fileName, url);
		 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印 
		// request.setIsShowWaterMark(true);
		
		 * 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档
		 * https://help.aliyun.com/document_detail/57029.html
		 
		// request.setCallback("http://callback.sample.com");
		 视频分类ID(可选) 
		// request.setCateId(0);
		 视频标签,多个用逗号分隔(可选) 
		// request.setTags("标签1,标签2");
		 视频描述(可选) 
		// request.setDescription("视频描述");
		 封面图片(可选) 
		// request.setCoverURL("http://cover.sample.com/sample.jpg");
		 模板组ID(可选) 
		// request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
		 存储区域(可选) 
		// request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
		UploadVideoImpl uploader = new UploadVideoImpl();
		UploadURLStreamResponse response = uploader.uploadURLStream(request);
		System.out.print("RequestId=" + response.getRequestId() + "\n"); // 请求视频点播服务的请求ID
		if (response.isSuccess()) {
			System.out.print("VideoId=" + response.getVideoId() + "\n");
		} else {
			
			 * 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，
			 * 此时需要根据返回错误码分析具体错误原因
			 
			System.out.print("VideoId=" + response.getVideoId() + "\n");
			System.out.print("ErrorCode=" + response.getCode() + "\n");
			System.out.print("ErrorMessage=" + response.getMessage() + "\n");
		}
	}*/
}
