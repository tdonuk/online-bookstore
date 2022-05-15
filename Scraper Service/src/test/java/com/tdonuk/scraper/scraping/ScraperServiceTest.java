package com.tdonuk.scraper.scraping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.tdonuk.scraper.adapter.KitapyurduAdapter;
import com.tdonuk.scraper.adapter.ScraperAdapter;
import com.tdonuk.scraper.model.Author;
import com.tdonuk.scraper.model.Name;
import com.tdonuk.scraper.service.ScraperService;

public class ScraperServiceTest {
	
	@Test
	void testScrape() throws Exception {

	}
	
	@Test
	public void kitapyurduTest() throws Exception{
		Document doc = Jsoup.connect("https://www.kitapyurdu.com/kitap/yuzuklerin-efendisi-tek-cilt/43196.html&filter_name=lord+of+the+rings").get();
		
		String a = doc.selectFirst("a.rel-cats__link").children().stream().map(el -> el.text()).collect(Collectors.joining(","));
		
		System.out.println(a);
	}
	
	@Test
	public void kitapyurduAuthorName() throws Exception {
		Elements authorElements = Jsoup.connect("https://www.kitapyurdu.com/kitap/fizik-2--fen-ve-muhendislik-icin-amp-elektrik-ve-manyetizmaisik-ve-optik/99571.html&filter_name=serway").get().select("div.pr_producers__manufacturer div.pr_producers__item a");
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
		
		System.out.println(authors);
	}
	
	String url = "https://www.kitapyurdu.com/kitap/yuzuklerin-efendisi-tek-cilt/43196.html&filter_name=lord+of+the+rings";
	
	@Test
	public void kitapYurduRateCount() throws Exception{
		Elements rateCountEl = Jsoup.connect(url).get().select("div[id='rating-mini']");
		
		String rateCountText = rateCountEl.text();
		
		if(rateCountText != null && !rateCountText.equals("")) {
			int rateCount = Integer.valueOf(rateCountText.replaceAll("[^0-9]", ""));
			System.out.println("bulundu");
			System.out.println(rateCountEl.text());
		}
		else {
			System.out.println("bulunamadı");
		}
		
	}
	
	@Test
	public void kitapyurduPageCount() throws Exception{
		String pages = Jsoup.connect(url).get().select("div.attributes table tbody tr:eq(6) td:eq(1)").text();
		
		System.out.println(pages);
	}
	
	@Test
	public void kitapyurduPrice() throws Exception {
		String price = Jsoup.connect(url).get().selectFirst("div.price__item").text();
		
		System.out.println(price);
	}
	
	@Test
	public void kitapyurduPublishDate() throws Exception{
		String date = Jsoup.connect(url).get().select("div.attributes table tbody tr:contains(Yayın) td:eq(1)").text();
		
		System.out.println(date);
	}

}
