package com.tattooju.status;

public enum AccountRoleEnum {

	ACCOUNT((byte)1),
	
	ADMIN((byte)2);
	
	private byte value;
	
	AccountRoleEnum(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
	
}
