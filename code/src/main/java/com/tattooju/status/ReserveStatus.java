package com.tattooju.status;

/**
 * 设置预约状态 0)已取消 1)已预约 2)已删除 3)已完成
 * @author 
 *
 */
public enum ReserveStatus {

	CANCAL((byte)0),
	
	RESERVED((byte)1),
	
	DELETE((byte)2),
	
	FINISH((byte)3);
	
	private byte value;
	
	private ReserveStatus(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
	
}
