package com.tattooju.status;

public enum MediaTypeEnum {

	VIDEO((byte)1),
	
	OTHER((byte)2);
	
	private byte value;
	
	private MediaTypeEnum(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
