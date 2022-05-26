package com.tdonuk.scraper.scraping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.HammingDistance;
import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
	
	
	
	
	String idefixSearch = "https://www.idefix.com/search/?Q=y%C3%BCz%C3%BCklerin%20efendisi&ShowNotForSale=True";
	String idefixUrl = "https://www.idefix.com/Kitap/Yuzuklerin-Efendisi-Iii-Kralin-Donusu/Edebiyat/Roman/Fantastik/urunno=0000000070734";
	
	@Test
	public void idefixSearch() throws Exception {
		String url = Jsoup.connect(idefixSearch).get().selectFirst("div.product-info div.box-title a").attr("href");
		
		System.out.println(url);
	}
	
	@Test
	public void idefixDescription() throws Exception {
		// String description = Jsoup.connect(idefixUrl).get().select("div.product-description p").eachText().stream().sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList()).get(0);
		String description = Jsoup.connect(idefixUrl).get().select("div.product-description p").text();
		
		System.out.println(description);
	}

	@Test
	public void idefixPageCount() throws Exception {
		
		String descriptionText = Jsoup.connect(idefixUrl).get().select("div.product-description p").text();
		
		String details = descriptionText.substring(descriptionText.indexOf("Sayfa Sayısı: ") + "Sayfa Sayısı: ".length());
		
		String pageCount = details.substring(0, details.indexOf(" "));
		
		System.out.println(Integer.valueOf(pageCount));
	}
	
	@Test
	public void idefixLanguage() throws IOException {
		String descriptionText = Jsoup.connect(idefixUrl).get().select("div.product-description p").text();
		
		String details = descriptionText.substring(descriptionText.indexOf("Dili: ") + "Dili: ".length());
		
		String lang = details.substring(0, details.indexOf(" "));
		
		System.out.println(lang);
	}
	
	@Test
	public void idefixScriptData() throws Exception{
		String data = Jsoup.connect(idefixUrl).get().selectFirst("script[type='application/ld+json']").data();
		
		JsonNode node = new JsonMapper().readTree(data);
		
		System.out.println(data);
		
		float rating = Float.parseFloat(node.get("aggregateRating").get("ratingValue").asText().replaceAll(",", "."));
		
		System.out.println(rating);
	}
	
	String idefixSerach = "https://www.idefix.com/search/?Q=y%C3%BCz%C3%BCklerin%20efendisi&ShowNotForSale=True";
	
	@Test
	public void idefixSearchPage() throws Exception {
		String price = Jsoup.connect(idefixSerach).get().selectFirst("span.price").attr("data-price").replaceAll("[^0-9,]", "").replaceAll(",", ".");
		
		System.out.println(price);
	}
	
	
	String drUrl = "https://www.dr.com.tr/kitap/yuzuklerin-efendisi-tek-cilt-ozel-basim/edebiyat/roman/fantastik/urunno=0000000113094";
	String drSearch = "https://www.dr.com.tr/search?q=y%C3%BCz%C3%BCklerin%20efendisi&redirect=search";
	
	@Test
	public void drImageUrl() throws Exception{
		String imageUrl = Jsoup.connect(drUrl).get().select("div[class='item slick-slide slick-current slick-active'] img").attr("src");
		
		System.out.println(imageUrl);
	}
	
	@Test
	public void drJsonScript() throws Exception{
		String data = Jsoup.connect(drUrl).get().selectFirst("script[type='application/ld+json']").data();
		
		System.out.println(data);
	}
	

	@Test
	public void drProducts() throws Exception{
		List<String> texts = Jsoup.connect(drUrl).get().select("div[class='prd-main-wrapper']").eachText();
		
		System.out.println(texts);
	}
	
	@Test
	public void drAuthors() throws Exception{
		String authors = Jsoup.connect(drSearch).get().select("a[class*='who']").eachAttr("title").stream().collect(Collectors.joining(", "));
		
		System.out.println(authors);
	}
	
	@Test
	public void aaaa() throws Exception{
		String a = "\r\n"
				+ "        {\r\n"
				+ "        \"@context\": \"http://schema.org\",\r\n"
				+ "        \"@type\": \"Book\",\r\n"
				+ "        \"bookFormat\": \"http://schema.org/Paperback\",\r\n"
				+ "        \"datePublished\": \"2001\",\r\n"
				+ "            \"image\":[\r\n"
				+ "                    \"https://i.dr.com.tr/cache/600x600-0/originals/0000000113094-1.jpg\",\r\n"
				+ "                    \"https://i.dr.com.tr/cache/600x600-0/originals/0000000113094-2.jpg\"\r\n"
				+ "            ],\r\n"
				+ "        \"inLanguage\": \"Turkish\",\r\n"
				+ "        \"isbn\": \"9789753423472\",\r\n"
				+ "        \"name\": \"Yüzüklerin Efendisi - Tek Cilt Özel Basım\",\r\n"
				+ "        \"description\":\"Dünya ikiye bölünmüştür, denir Tolkien'ın yapıtı söz konusu olduğunda: Yüzüklerin Efendisi'ni okumuş olanlar ve okuyacak olanlar. 1997 ile birlikte, çok sayıda Türkiyeli okur da 'okumuş olanlar' safına geçme fırsatı buldu. Kitabın Türkçe basımı Yüzüklerin Efendisi'ne duyulan ilginin evrenselliğini kanıtladı.\r\n"
				+ "Yapıtın bu başarısını taçlandırmak için üç kısmı bir araya getiren bu özel, tek cilt edisyonu sunuyoruz. Hem hâlâ okumamış, 'okuyacak olanlar' için, hem de bu güzel kitabın kütüphanenizde gelecek kuşaklara devrolacak kadar kalıcı olması için...\r\n"
				+ "Yüzüklerin Efendisi yirminci yüzyılın en çok okunan yüz kitabı arasında en başta geliyor; bilimkurgu, fantazi, polisiye, best-seller ya da ana akım demeden, tüm edebiyat türleri arasında tartışmasız bir önderliğe sahip. Bir açıdan bakarsanız bir fantazi romanı, başka bir açıdan baktığınızda, insanlık durumu, sorumluluk, iktidar ve savaş üzerine bir roman. bir yolculuk, bir büyüme öyküsü; fedakarlık ve dostluk üzerine, hırs ve ihanet üzerine bir roman.\r\n"
				+ " \r\n"
				+ "(Tanıtım Bülteninden)\r\n"
				+ " \r\n"
				+ " )\",\r\n"
				+ "        \"numberOfPages\": \"1026\",\r\n"
				+ "        \"author\":\r\n"
				+ "        {\r\n"
				+ "        \"@type\":\"Person\",\r\n"
				+ "        \"name\":\"J. R. R. Tolkien\"\r\n"
				+ "        },\r\n"
				+ "        \"publisher\":\r\n"
				+ "        {\r\n"
				+ "        \"@type\":\"Organization\",\r\n"
				+ "        \"name\":\"Metis Yayıncılık\"\r\n"
				+ "        },\r\n"
				+ "        \"offers\": {\r\n"
				+ "        \"@type\": \"Offer\",\r\n"
				+ "        \"availability\": \"http://schema.org/InStock\",\r\n"
				+ "        \"itemCondition\": \"https://schema.org/NewCondition\",\r\n"
				+ "        \"price\": \"163.20\",\r\n"
				+ "        \"priceCurrency\": \"TL\"\r\n"
				+ "        }\r\n"
				+ "            ,\r\n"
				+ "            \"aggregateRating\": {\r\n"
				+ "            \"@type\": \"AggregateRating\",\r\n"
				+ "            \"ratingValue\": \"4\",\r\n"
				+ "            \"reviewCount\": \"241\",\r\n"
				+ "            \"worstRating\" : \"0\",\r\n"
				+ "            \"bestRating\" : \"5\"\r\n"
				+ "              }\r\n"
				+ "                    ,\r\n"
				+ "             \"review\": [\r\n"
				+ "                    {\r\n"
				+ "                    \"@type\": \"Review\",\r\n"
				+ "                    \"author\": \"R&#252;meysa\",\r\n"
				+ "                    \"datePublished\": \"26.4.2022 12:53:21\",\r\n"
				+ "                    \"name\": \"&#199;ok iyi\",\r\n"
				+ "                    \"reviewBody\":\"Hediye almıştım, çok güzel. Filmleri mükemmel, kitabı daha mükemmel :))\"\r\n"
				+ "                      },\r\n"
				+ "                    {\r\n"
				+ "                    \"@type\": \"Review\",\r\n"
				+ "                    \"author\": \"Senem\",\r\n"
				+ "                    \"datePublished\": \"19.11.2021 23:01:12\",\r\n"
				+ "                    \"name\": \"&#199;ok iyi\",\r\n"
				+ "                    \"reviewBody\":\"Urun cok cabuk geldi ve tam beklediğim gibi)\"\r\n"
				+ "                      },\r\n"
				+ "                    {\r\n"
				+ "                    \"@type\": \"Review\",\r\n"
				+ "                    \"author\": \"Dilara \",\r\n"
				+ "                    \"datePublished\": \"21.4.2021 06:33:23\",\r\n"
				+ "                    \"name\": \"Tavsiye Edilir\",\r\n"
				+ "                    \"reviewBody\":\"ürün beklediğimden çok daha hızlı elime ulaştı ve sorunsuzdu.filmde bulunmayan daha detaylı güzel bir evren var, mutlaka okunmalı. içerisinde ortadoğu posteriyle birlikte geldi.)\"\r\n"
				+ "                      }\r\n"
				+ "            ]}\r\n"
				+ "\r\n"
				+ "        }\r\n"
				+ "\r\n"
				+ "    ";
		
		System.out.println(a.lines().collect(Collectors.toList()).get(14));
		System.out.println(a.charAt(334));
	}
	
	@Test
	public void stringDiff() {		

		double distance = new JaroWinklerDistance().apply("Yüzüklerin Efendisi 1: Yüzük Kardeşliği", "Yüzük Kardeşliği Yüzüklerin Efendisi 1");
		
		System.out.println(distance);
		

	}
}
