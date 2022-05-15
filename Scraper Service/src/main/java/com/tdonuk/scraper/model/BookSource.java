package com.tdonuk.scraper.model;

public enum BookSource {
	KITAPYURDU("Kitapyurdu"), HEPSIBURADA("Hepsiburada"), IDEFIX("İdefix"), AMAZON("Amazon");
	
	String text;
	BookSource(String text) {
		this.text = text;
	}
	
	public String text() {
		return this.text;
	}
}
