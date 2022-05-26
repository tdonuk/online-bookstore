package com.tdonuk.scraper.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.tdonuk.scraper.model.Author;
import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.Name;
import com.tdonuk.scraper.model.entity.Book;
import com.tdonuk.scraper.util.NumericUtils;

public class DRAdapter extends ScraperAdapter{
	private static final String BASE_URL = "https://www.dr.com.tr";
	private static final String SEARCH_PATH = "/search?q=";

	@Override
	public void parseTitle(Book book) throws Exception {
		String title = getCurrentPage().selectFirst("div.prd-name h1").text();
		
		book.setTitle(title);
	}

	@Override
	public void parseIsbn(Book book) throws Exception {
		String isbn = getCurrentPage().select("div.product-property ul li:contains(Barkod) span").text();
		
		if(null == isbn || "".equals(isbn)) throw new Exception("not able to parse isbn");
		
		book.setIsbn(isbn);
	}

	@Override
	public void parseDescription(Book book) throws Exception {
		String description = getCurrentPage().select("div[class='product-description-header section-cover mb-40 fs-3_5'] p").eachText().stream().collect(Collectors.joining(" ")).trim();

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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parsePageCount(Book book) throws Exception {
		String descriptionText = getCurrentPage().select("div[class='product-description-header section-cover mb-40 fs-3_5'] p").text();
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseAuthors(Book book) throws Exception {
		List<String> authorsList = getCurrentPage().select("div.author:contains(Yazar:) a").eachText();
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
	protected String baseUrl() {
		return BASE_URL;
	}

	@Override
	protected String searchPath(String key) {
		return BASE_URL + SEARCH_PATH + key;
	}

	@Override
	protected boolean isValid(String url) {
		return true;
	}

	@Override
	protected Elements selectPagination(Document doc) {
		return doc.select("ul[class='pager pager-list dr-flex pagination js-facet-list-pagination flex-wrap'] li a");
	}

	@Override
	protected Set<BookCard> selectProducts(Document doc) {
		Elements elements = doc.select("div[class='prd-main-wrapper']");
		
		Set<BookCard> items = new HashSet<>();
		
		BookCard card;
		for(Element el : elements) {
			card = new BookCard();
			
			card.setSource(getSource());
			
			String title = el.select("a.prd-name").attr("title");
			String url = el.select("a.prd-name").attr("href");
			String imgUrl = el.select("div.product-img a img").attr("data-src");
			String price = el.selectFirst("div.prd-price").attr("data-price").replaceAll("[^0-9,]", "").replaceAll(",", ".");
			String authors = el.select("a[class*='who']").eachAttr("title").stream().collect(Collectors.joining(", "));
			String publisher = el.select("a.prd-publisher").attr("title");
			
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
		return Jsoup.connect(BASE_URL + SEARCH_PATH + key + "&Page="+num).userAgent(userAgent()).get();
	}

	@Override
	public BookSource getSource() {
		return BookSource.DR;
	}

	@Override
	protected Book handleJson(JsonNode node) throws Exception {
		Book book = parseDefault();
		
		if(book == null) {
			book = new Book();
		}
		
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

}
