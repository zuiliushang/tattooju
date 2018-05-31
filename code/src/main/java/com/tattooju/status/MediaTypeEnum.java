package com.tattooju.status;

public enum MediaTypeEnum {

	VIDEO((byte)2),
	
	OTHER((byte)1);
	
	private byte value;
	
	private MediaTypeEnum(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
