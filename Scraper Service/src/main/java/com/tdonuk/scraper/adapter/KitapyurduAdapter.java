package com.tdonuk.scraper.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
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
import com.tdonuk.scraper.util.UserAgentGenerator;

public class KitapyurduAdapter extends ScraperAdapter{
	public static final String BASE_URL = "https://www.kitapyurdu.com";
	public static final String SEARCH_PATH = "/index.php?route=product/search&filter_name=";
	
	protected static Random random = new Random();

	@Override
	public void parseTitle(Book book) throws Exception {
		String bookTitle = getCurrentPage().selectFirst("div.pr_header h1.pr_header__heading").ownText();
		
		book.setTitle(bookTitle);
	}

	@Override
	public void parseIsbn(Book book) throws Exception {
		String isbn = getCurrentPage().select("div.attributes table tbody tr:contains(ISBN) td:eq(1)").text();
		
		if("".equals(isbn) || null == isbn || isbn.length() < 7) throw new Exception("Not able to parse isbn");
		
		book.setIsbn(isbn);
	}

	@Override
	public void parseDescription(Book book) throws Exception {
		String description = getCurrentPage().selectFirst("div#description_text span.info__text").ownText();
		
		book.setDescription(description);
	}

	@Override
	public void parsePrice(Book book) throws Exception {
		Element priceEl = getCurrentPage().selectFirst("div[class='pr_price__content pr_sell-price']");
		
		if(priceEl != null) {
			String price = priceEl.text();
			
			price = price.replaceAll("[.]", "").replaceAll(",", ".").replaceAll("[^0-9.]", "");
			
			book.setPrice(Double.valueOf(price));
		}
		else throw new Exception("Not able to parse price");
	}

	@Override
	public void parseRating(Book book) throws Exception {
		Elements selectedStars =  getCurrentPage().select("ul.pr_rating-stars li i[class$='selected']");
		
		int selected = selectedStars.size(); // example: 5 stars of total, selected only 4 so the rating should be 4 if there was 10 stars of total, the rating could be 2
		
		book.setRating(selected);
		
	}

	@Override
	public void parseRateCount(Book book) throws Exception {
		String rateCountText = getCurrentPage().select("div[class='fl mg-r-10 mg-b-5'] span:eq(2)").text();
		
		if(rateCountText != null && !rateCountText.equals("")) {
			int rateCount = Integer.valueOf(rateCountText.replaceAll("[^0-9]", ""));
			book.setRateCount(rateCount);
		}
		book.setRateCount(0);
	}

	@Override
	public void parseImageUrl(Book book) throws Exception {
		String imageUrl = getCurrentPage().select("a.js-jbox-book-cover img").attr("src");
		
		book.setImageUrl(imageUrl);
	}

	@Override
	public void parseBookUrl(Book book) throws Exception {
		String url = getCurrentPage().location();
		
		book.setUrl(url);
	}

	@Override
	public void parseTags(Book book) throws Exception {
		String tags = getCurrentPage().selectFirst("a.rel-cats__link").children().stream().map(el -> el.text()).collect(Collectors.joining(","));
		
		Set<String> tagsSet = new HashSet<>();
		
		for(String tag : tags.split(",")) {
			tagsSet.add(tag);
		}
		
		book.setTags(tagsSet);
	}
	

	@Override
	public void parseLanguage(Book book) throws Exception {
		String lang = getCurrentPage().select("div.attributes table tbody tr:contains(dil) td:eq(1)").text();
		
		book.setLanguage(lang);
	}


	@Override
	public void parsePageCount(Book book) throws Exception {
		String pages = getCurrentPage().select("div.attributes table tbody tr:contains(Sayfa Sayısı) td:eq(1)").text();
		
		book.setPageCount(Integer.valueOf(pages));
	}


	@Override
	public void parsePublishDate(Book book) throws Exception {
		String publishDate = getCurrentPage().select("div.attributes table tbody tr:contains(Yayın) td:eq(1)").text();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		
		Date date = formatter.parse(publishDate);
		
		book.setPublishDate(date);
	}


	@Override
	public void parseAuthors(Book book) throws Exception {
		Elements authorElements = getCurrentPage().select("div.pr_producers__manufacturer div.pr_producers__item a");
		List<String> authorNames = authorElements.eachText();
		
		Set<Author> authors = new HashSet<>();
		
		authorNames.forEach(name -> {
			String[] nameDetails = name.split(" ");
			
			String firstname, lastname;
			lastname = nameDetails[nameDetails.length - 1];
			firstname = Arrays.stream(nameDetails).limit(nameDetails.length - 1).collect(Collectors.joining(" "));
			
			Author author = new Author(new Name(firstname, lastname));
			authors.add(author);
		});
		
		book.setAuthors(authors);
	}


	@Override
	public BookSource getSource() {
		return BookSource.KITAPYURDU;
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
		return url.startsWith(BASE_URL + "/kitap");
	}
	
	@Override
	protected Elements selectPagination(Document doc) {
		Elements elements = doc.select("div.pagination div.links *");
		
		return elements;
	}
	
	@Override
	protected Set<BookCard> selectProducts(Document doc) {
		Elements elements = doc.select("div[id='product-table'] div.product-cr");
		
		Set<BookCard> items = new HashSet<>();
		
		BookCard card;
		for(Element el : elements) {
			card = new BookCard();
			
			card.setSource(getSource().text());
			
			String title = el.select("div.name a").attr("title");
			String url = el.select("div.name a").attr("href");
			String imgUrl = el.select("div.cover a img").attr("src");
			String price = el.selectFirst("div.price:contains(Kitapyurdu Fiyatı) span.value").text().replaceAll("[^0-9,]", "").replaceAll(",", ".");
			
			if(!url.startsWith(BASE_URL)) url = BASE_URL + (url.startsWith("/") ? url : "/" + url);
			
			if(!url.startsWith(BASE_URL + "/kitap")) continue;
			
			card.setTitle(title);
			card.setUrl(url);
			card.setImgUrl(imgUrl);
			
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
		Document doc = Jsoup.connect(searchPath(key) + "&page="+num).get();
		
		return doc;
	}


	@Override
	protected Book handleJson(JsonNode node) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
