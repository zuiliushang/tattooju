package com.tattooju.util;

import com.alibaba.fastjson.JSONArray;

public class Test {

	public static void main(String[] args) {
		JSONArray sArray = JSONArray.parseArray("[{'text':'《回归》这是今年到现在画得最精细的一副了'},{'video':'http://media.tattooju.com/sv/1d4eabf7-163c4bb9d40/1d4eabf7-163c4bb9d40.mp4'}]");
		System.out.println(sArray);
	}
	
}
