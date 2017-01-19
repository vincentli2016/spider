package com.spider;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookQueue {
	private Queue<Book> bookbuffer;
	private int bookCount;
	
	public BookQueue() {
		bookbuffer = new ConcurrentLinkedQueue<Book>();
	}
	//size
	public synchronized int size() {
		return this.bookbuffer.size();
	}

	//producer call this method for add data
	public synchronized void addBook(List<Book> books) throws InterruptedException {
		if(null != books) {
			for(Book book : books) {
				book.setId(bookCount++);
				if(this.size() < 40) {
					bookbuffer.add(book);
				} else {
					wait();
				}
			}
			notifyAll();
		} else {
			wait();
		}
	}
	
	//consumer call this method for pop data
	public synchronized Book getBook() throws InterruptedException {
		Book book = null;
		if(bookbuffer.size() <= 0) {
			wait();
		} else {
			book = bookbuffer.poll();
		}
		return book;
	}
}
