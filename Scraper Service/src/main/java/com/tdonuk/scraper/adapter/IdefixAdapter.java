package com.tdonuk.scraper.adapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tdonuk.scraper.model.Author;
import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.Name;
import com.tdonuk.scraper.model.entity.Book;
import com.tdonuk.scraper.util.NumericUtils;
import com.tdonuk.scraper.util.UserAgentGenerator;

public class IdefixAdapter extends ScraperAdapter{
	public static final String BASE_URL = "https://www.idefix.com";
	public static final String SEARCH_PATH = "/search/?Q=";

	@Override
	protected Book handleJson(JsonNode node) throws Exception {
		Book book = parseDefault();
		
		String title = node.get("name").asText();
		String description = node.get("description").asText();
		String imageUrl = node.get("image").get(0).asText();
		String publishDate = node.get("datePublished").asText();
		String isbn = node.get("isbn").asText();
		String language = node.get("inLanguage").asText();
		long rateCount = node.get("aggregateRating").get("reviewCount").asLong();
		float rating = Float.parseFloat(node.get("aggregateRating").get("ratingValue").asText().replaceAll(",", "."));
		double price = node.get("offers").get("price").asDouble();
		
		book.setTitle(title);
		book.setImageUrl(imageUrl);
		book.setDescription(description);
		book.setPublishDate(new SimpleDateFormat("yyyy").parse(publishDate));
		book.setIsbn(isbn);
		book.setLanguage(language);
		book.setRateCount(rateCount);
		book.setRating(rating);
		book.setPrice(price);
		
		book.setSource(getSource());
		
		return book;
	}

	@Override
	public void parseTitle(Book book) throws Exception {
		String title = getCurrentPage().select("div.prodyctDetailTopTitle h1").text();
		
		book.setTitle(title);
	}

	@Override
	public void parseIsbn(Book book) throws Exception {
		String isbn = getCurrentPage().select("div.product-info-list ul li:contains(Barkod) a").text();
		
		if(null == isbn || "".equals(isbn)) throw new Exception("ISBN alanı zorunludur.");
		
		book.setIsbn(isbn);
	}

	@Override
	public void parseDescription(Book book) throws Exception {
		String description = getCurrentPage().select("div.product-description p").text();

		book.setDescription(description);
	}

	@Override
	public void parsePrice(Book book) throws Exception {
		return;
	}

	@Override
	public void parseRating(Book book) throws Exception {
		book.setRating(0);
	}

	@Override
	public void parseRateCount(Book book) throws Exception {
		book.setRateCount(0);
	}

	@Override
	public void parseImageUrl(Book book) throws Exception {
		String imageUrl = getCurrentPage().select("img#main-product-img").attr("src");
		
		book.setImageUrl(imageUrl);
	}

	@Override
	public void parseBookUrl(Book book) throws Exception {
		book.setUrl(getCurrentPage().location());
	}

	@Override
	public void parseTags(Book book) throws Exception {
		Set<String> tags = getCurrentPage().select("ul.breadcrumb li a[href^='/kategori']").eachText().stream().collect(Collectors.toSet());
		
		book.setTags(tags);
	}

	@Override
	public void parseLanguage(Book book) throws Exception {
		String descriptionText = getCurrentPage().select("div.product-description p").text();
		
		String details = descriptionText.substring(descriptionText.indexOf("Dili: ") + "Dili: ".length());
		
		String language = details.substring(0, details.indexOf(" "));
		
		book.setLanguage(language);
	}

	@Override
	public void parsePageCount(Book book) throws Exception {
		String descriptionText = getCurrentPage().select("div.product-description p").text();
		
		String details = descriptionText.substring(descriptionText.indexOf("Sayfa Sayısı: ") + "Sayfa Sayısı: ".length());
		
		String pageCount = details.substring(0, details.indexOf(" "));
		
		if(! NumericUtils.isInteger(pageCount)) {
			book.setPageCount(0);
			return;
		}
		
		book.setPageCount(Integer.valueOf(pageCount));
	}

	@Override
	public void parsePublishDate(Book book) throws Exception {
		String descriptionText = getCurrentPage().select("div.product-description p").text();
		
		String details = descriptionText.substring(descriptionText.indexOf("Baskı Yılı: ") + "Baskı Yılı: ".length());
		
		String publishDate = details.substring(0, details.indexOf(" "));
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		
		Date d;
		try {
			d = formatter.parse(publishDate);
		} catch(Exception e) {
			d = null;
		}
		
		book.setPublishDate(d);
	}

	@Override
	public void parseAuthors(Book book) throws Exception {
		List<String> authorsList = getCurrentPage().select("div.author:contains(Yazar:) a.author-text").eachText();
		Set<Author> authors = new HashSet<>();
		
		String firstname, lastname;
		String[] name;
		for(int i = 0; i<authorsList.size(); i++) {
			String s = authorsList.get(i);
			
			if(s.endsWith(",")) s = s.replaceAll(",", "");
			
			name = s.split(" ");
			
			lastname = name[name.length - 1];
			firstname = Arrays.stream(name).limit(name.length - 1).collect(Collectors.joining(" "));
			
			authors.add(new Author(new Name(firstname, lastname)));
		}
		
		book.setAuthors(authors);
	}

	@Override
	public BookSource getSource() {
		return BookSource.IDEFIX;
	}

	@Override
	protected String baseUrl() {
		return BASE_URL;
	}

	@Override
	protected String searchPath(String key) {
		return BASE_URL + SEARCH_PATH + key + "&SortType=commentcount&SortOrder=desc";
	}

	@Override
	protected boolean isValid(String url) {
		return url.startsWith(BASE_URL + "/Kitap");
	}

	@Override
	protected Elements selectPagination(Document doc) {
		Elements elements = doc.select("ul.pager li a");
		
		return elements;
	}

	@Override
	protected Set<BookCard> selectProducts(Document doc) {
		Elements elements = doc.select("div[class='cart-product-box-view']");
		
		Set<BookCard> items = new HashSet<>();
		
		BookCard card;
		for(Element el : elements) {
			card = new BookCard();
			
			card.setSource(getSource().text());
			
			String title = el.select("a").attr("title");
			String url = el.select("a").attr("href");
			String imgUrl = el.select("a img").attr("data-src");
			String price = el.selectFirst("span.price").attr("data-price").replaceAll("[^0-9,]", "").replaceAll(",", ".");
			String authors = el.select("div[class*='pName'] a.who").eachAttr("title").stream().collect(Collectors.joining(", "));
			String publisher = el.select("div[class*='manufacturerName'] a").attr("title");
			
			if(!url.startsWith(BASE_URL)) url = BASE_URL + (url.startsWith("/") ? url : "/" + url);
			
			card.setTitle(title);
			card.setUrl(url);
			card.setImgUrl(imgUrl);
			card.setAuthors(authors);
			card.setPublisher(publisher);
			
			try {
				card.setPrice(Double.valueOf(price));	
			} catch(Exception e) {
				err(e.getMessage() + " [price: "+price+"]");
				
				continue;
			}
			
			items.add(card);
		}
		
		return items;
		
	}

	@Override
	protected Document getPage(String key, int num) throws IOException {
		Document doc = Jsoup.connect(searchPath(key) + "&Page="+num).get();
		
		return doc;
	}
	
}
