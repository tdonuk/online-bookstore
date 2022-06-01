package com.tdonuk.scraper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tdonuk.scraper.adapter.DRAdapter;
import com.tdonuk.scraper.adapter.IdefixAdapter;
import com.tdonuk.scraper.adapter.KitapyurduAdapter;
import com.tdonuk.scraper.adapter.ScraperAdapter;

public enum BookSource {
	KITAPYURDU("Kitapyurdu", new KitapyurduAdapter()), IDEFIX("İdefix", new IdefixAdapter()), DR("D&R", new DRAdapter());
	
	String text;
	BookSource(String text, ScraperAdapter adapter) {
		this.text = text;
		this.adapter = adapter;
	}
	
	@JsonIgnore
	ScraperAdapter adapter;
	public ScraperAdapter adapter() {
		return this.adapter;
	}

	@JsonValue
	public String text() {
		return this.text;
	}
}
