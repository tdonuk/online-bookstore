package com.tdonuk.userservice.model;


public enum BookSource {
	KITAPYURDU("Kitapyurdu"), IDEFIX("İdefix"), DR("D&R");
	
	String text;
	BookSource(String text) {
		this.text = text;
	}
	
	public String text() {
		return this.text;
	}
}
