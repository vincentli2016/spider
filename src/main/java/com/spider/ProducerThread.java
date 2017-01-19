package com.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProducerThread extends Thread {
	private BookQueue bookQueue;
	private int bookCount;
	
	public ProducerThread(BookQueue bookQueue, int bookCount) {
		this.bookQueue = bookQueue;
		this.bookCount = bookCount;
		System.out.println(bookCount);
	}
	
	public List<Book> crawl() throws IOException {
		List<Book> books = new ArrayList<Book>();
		
		String url = "https://book.douban.com/tag/编程";
		Connection conn = Jsoup.connect(url);
		String[] params = new String[] { "start", "type" };
		String[] values = new String[] { Integer.toString(bookCount), "S" };

		for (int i = 0; i < params.length; i++) {
			conn.data(params[i], values[i]);
		}

		Document doc = conn.timeout(100000).get();
		Elements results = doc.getElementsByClass("subject-list");

		for (Element result : results) {
			Elements booklist = result.getElementsByTag("li");
			
			for (Element book : booklist) {
				Book dbBook = new Book();
				
				//Get Book name
				Elements h2s = book.getElementsByTag("h2");
				Element h2 = h2s.get(0);
				Elements links = h2.getElementsByTag("a");
				for(Element link : links) {
					String title = link.attr("title");
					dbBook.setTitle(title);
				}
				
				//Get book rate
				Elements bookRate = book.getElementsByClass("rating_nums");
				for(Element rate : bookRate) {
					dbBook.setRate(Double.valueOf(rate.text()));
				}
				//Get rate count
				Elements rateCount = book.getElementsByClass("pl");
				for(Element count : rateCount) {
					dbBook.setRateCount(Integer.valueOf(count.text().substring(1, count.text().indexOf("人"))));
				}
				Elements pubs = book.getElementsByClass("pub");
				for(Element pub : pubs) {
					String[] bookBenefactors = pub.text().split("/");
					if(bookBenefactors.length == 5) {//books with translator
						//Get writer
						dbBook.setWriter(bookBenefactors[0]);
						//translator
						dbBook.setTranslator(bookBenefactors[1]);
						//Get publisher
						dbBook.setPublisher(bookBenefactors[2]);
						//Get publish date
						dbBook.setPublishDate(bookBenefactors[3]);
						//Get price
						dbBook.setPrice(bookBenefactors[4]);
					} else if(bookBenefactors.length == 4) {//original books
						//Get writer
						dbBook.setWriter(bookBenefactors[0]);
						//Get publisher
						dbBook.setPublisher(bookBenefactors[1]);
						//Get publish date
						dbBook.setPublishDate(bookBenefactors[2]);
						//Get price
						dbBook.setPrice(bookBenefactors[3]);
					}
				}
				
				if(dbBook.getRateCount() >= 1000) {
					books.add(dbBook);
				}
			}
		}
		return books;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				List<Book> books = null;
				try {
					books = crawl();
					if(books.size() > 0) {
						bookQueue.addBook(books);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {

		}
	}

}
