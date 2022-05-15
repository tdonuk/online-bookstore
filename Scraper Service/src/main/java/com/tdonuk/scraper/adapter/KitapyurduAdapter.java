package com.tdonuk.scraper.adapter;

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

import com.tdonuk.scraper.model.Author;
import com.tdonuk.scraper.model.BookSource;
import com.tdonuk.scraper.model.Name;
import com.tdonuk.scraper.model.entity.Book;
import com.tdonuk.scraper.util.NumericUtils;
import com.tdonuk.scraper.util.UserAgentGenerator;

public class KitapyurduAdapter extends ScraperAdapter{
	public static final String BASE_URL = "https://www.kitapyurdu.com";
	public static final String SEARCH_PATH = "/index.php?route=product/search&filter_name=";
	
	
	
	private static Random random = new Random();
	
	public KitapyurduAdapter(Document currentDocument) {
		super(currentDocument);
	}
	

	public KitapyurduAdapter() {

	}


	@Override
	public List<Book> parse(Set<String> urls) throws Exception {
		List<Book> books = new ArrayList<>();
		Book book;
		System.out.println("Scraping "+getSource().text()+ " for " + urls.size() + " products.");
		int index = 0;
		for(String url : urls) {
			index++;
			
			if(!url.startsWith(BASE_URL+"/kitap")) { // it's not a book
				System.out.println("Skipping ["+url+"] for wrong product");
				continue;
			}
			
			try {
				System.out.println("Parsing: " + url);
				System.out.println("user agent: " + userAgent());
				setCurrentPage(Jsoup.connect(url).userAgent(userAgent()).get());

				book = parseDefault();
				book.setSource(getSource());
				
				books.add(book);
				
				if(books.size() > 5) {
					System.out.println("reached maximum result size. completing the process..");
					break;
				};
				
				System.out.println("status: " + (index) + "/"+urls.size());
				
				Thread.sleep(random().nextInt(100));
			} catch(Exception e) {
				System.err.println("HATA: " + e.getMessage());
				continue;
			}

		}
		
		return books;
	}
	

	@Override
	public void parseTitle(Book book) throws Exception {
		String bookTitle = getCurrentPage().selectFirst("div.pr_header h1.pr_header__heading").ownText();
		
		book.setTitle(bookTitle);
	}

	@Override
	public void parseIsbn(Book book) throws Exception {
		String isbn = getCurrentPage().select("div.attributes table tbody tr:contains(ISBN) td:eq(1)").text();
		
		book.setIsbn(isbn);
	}

	@Override
	public void parseDescription(Book book) throws Exception {
		String description = getCurrentPage().selectFirst("div#description_text span.info__text").ownText();
		
		book.setDescription(description);
	}

	@Override
	public void parsePrice(Book book) throws Exception {
		String price = getCurrentPage().selectFirst("div.price__item").text();
		
		price = price.replaceAll("[.]", "").replaceAll(",", ".").replaceAll("[^0-9.]", "");
		
		book.setPrice(Double.valueOf(price));
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
	public Set<String> search(String key) throws Exception{
		String url = BASE_URL + SEARCH_PATH + key;
		
		Document doc = Jsoup.connect(url).get();
		if(doc != null) {
			System.out.println("Doc is parsed");
			
			long pages = doc.select("div.pagination div.links *").stream().filter(e -> NumericUtils.isInteger(e.text())).count();
			
			int currentPage = 1;
			List<String> urls = new ArrayList<>();
			do {
				int i = random.nextInt(200);
				System.out.println("Waiting for "+((double) i/1000)+" seconds...");
				Thread.sleep(i);
				
				doc = Jsoup.connect(url + "&page="+currentPage).get();
				
				Elements elements = doc.select("div[id='product-table'] div.product-cr div.name a");
				
				elements.eachAttr("href").stream().forEach(u -> {
					if(!u.startsWith(BASE_URL)) {
						u.replace(u, BASE_URL+"/"+u);
					}
				});
				
				urls.addAll(elements.eachAttr("href"));
				
				currentPage++;
			}
			while(currentPage <= pages);
			
			System.out.println("Found elements: " + urls.size() + " ("+ pages +" pages)");
			
			return urls.stream().collect(Collectors.toUnmodifiableSet());
		} else {
			System.out.println("Doc is not parsed");
		}
		
		return Set.of();
	}


	@Override
	public BookSource getSource() {
		return BookSource.KITAPYURDU;
	}
	
}
