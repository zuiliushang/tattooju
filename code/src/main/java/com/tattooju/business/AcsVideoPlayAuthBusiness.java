/*package com.tattooju.business;

import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse.VideoMeta;

@Service
public class AcsVideoPlayAuthBusiness {
	
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
	    *//**
	     * 
			playAuth = eyJTZWN1cml0eVRva2VuIjoiQ0FJUzN3SjFxNkZ0NUIyeWZTaklyNG5mQThqNXF1MXg0N1MrVHg3WTNVVXNmY3B1cnIzeXVqejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsck1xRzhRWkd4YWFNWllndHNvSXIxdndKcExGc3QySjZyOEpqc1V2ZzlWWDJGcXBzdlhKYXNEVkVma3VFNVhFTWlJNS8wMGU2TC8rY2lyWVhEN0JHSmFWaUpsaFE4MEtWdzJqRjFSdkQ4dFhJUTBRazYxOUszemRaOW1nTGlidWkzdnhDa1J2MkhCaWptOHR4cW1qL015UTV4MzFpMXYweStCM3dZSHRPY3FjYThCOU1ZMVdUc3Uxdm9oemFyR1Q2Q3BaK2psTStxQVU2cWxZNG1YcnM5cUhFa0ZOd0JpWFNaMjJsT2RpTndoa2ZLTTNOcmRacGZ6bjc1MUN0L2ZVaXA3OHhtUW1YNGdYY1Z5R0dOLzZuNU9aUXJ6emI0WmhKZWVsQVJtWGpJRFRiS3VTbWhnL2ZIY1dPRGxOZjljY01YSnFBWFF1TUdxQ2QvTDlwdzJYT2x6NUd2WFZnUHRuaTRBSjVsSHA3TWVNR1YrRGVMeVF5aDBFSWFVN2EwNDQvNWVUWWFwazFNVWFnQUVpNHNTelZ0VHIzejRLS0JqZUppVFBlMW9WUThUVVhBSHhlQm5oeVl0NVRQalBkY0JCUFF4S3ZucjVRSkQ2UVdYMnQ3L2xWRzVQK1EzUk95MHoxTlgwZVh1cUtnV0hOaCtCYS9FSHBvNkdtUlQwTDk2Q0J4QUpoUXQvMmllbStZYWlUYUpGUnpvVk9jQzFuV0wyZHB6eXN6d2pybHZ5d1VzTitaMXJvbjhEZ1E9PSIsIkF1dGhJbmZvIjoie1wiQ2FsbGVyXCI6XCJhZEI4SkNIZ2JFQmlNdUwwQmNwTnZBaHZYcmxZUnh3OERlY3FxZ0o5Y2tJPVxcclxcblwiLFwiRXhwaXJlVGltZVwiOlwiMjAxOC0wNS0zMVQwODo0NjowMlpcIixcIk1lZGlhSWRcIjpcIjJlNDNkNzk1ZTA2YjRhNjY5YzlhOTljNTJhMGU3Mzg1XCIsXCJTaWduYXR1cmVcIjpcIklYWW9nVFJFNG9tRUJtYTUxazJEYVhDZFRPTT1cIn0iLCJWaWRlb01ldGEiOnsiU3RhdHVzIjoiTm9ybWFsIiwiVmlkZW9JZCI6IjJlNDNkNzk1ZTA2YjRhNjY5YzlhOTljNTJhMGU3Mzg1IiwiVGl0bGUiOiLmtYvor5XmoIfpopgiLCJDb3ZlclVSTCI6Imh0dHA6Ly9tZWRpYS50YXR0b29qdS5jb20vMmU0M2Q3OTVlMDZiNGE2NjljOWE5OWM1MmEwZTczODUvY292ZXJzL2ViZWNmNzNiNmU3ZjQ5NTU4NGJhMmE3MzJmYjhmYTU3LTAwMDAxLmpwZyIsIkR1cmF0aW9uIjozLjIxNX0sIkFjY2Vzc0tleUlkIjoiU1RTLk5KakhyTUcyUFR2VU04aTlFeXFFQkF4WVgiLCJQbGF5RG9tYWluIjoibWVkaWEudGF0dG9vanUuY29tIiwiQWNjZXNzS2V5U2VjcmV0IjoiR1pKSFpOWXRmU3Q5cm92Qnd2RGJXQzIybXlQMm00OHhIWTNDNmZnM3lCYVAiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSIsIkN1c3RvbWVySWQiOjE4MzQ2MjY0NTgwNjc2MDl9
			videoMeta = com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse$VideoMeta@28eaa59a
			url = http://vod.cn-shanghai.aliyuncs.com/?SignatureVersion=1.0&Action=GetVideoPlayAuth&Format=XML&VideoId=2e43d795e06b4a669c9a99c52a0e7385&SignatureNonce=93ec0ede-0a71-45b3-a289-0c7e8b17506c&Version=2017-03-21&AccessKeyId=LTAIKGcm5gyqIHSZ&Signature=eajs8DPsW8FP5pMrR5A58RWUfeo%3D&SignatureMethod=HMAC-SHA1&RegionId=cn-shanghai&Timestamp=2018-05-31T08%3A44%3A19Z
	     * 
	     * Normal
	     *//*
	    VideoMeta videoMeta = response.getVideoMeta();
	    
	    System.out.println("videoMeta = "+response.getVideoMeta());
	    System.out.println("playAuth = "+response.getPlayAuth());
	    System.out.println("coverURL = " + videoMeta.getCoverURL());
	    System.out.println("status=" + videoMeta.getStatus());
	    System.out.println("title="+videoMeta.getTitle());
	    System.out.println("duration="+videoMeta.getDuration());
	}
	
}
*/