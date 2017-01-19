package com.spider.spider;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.spider.Book;
import com.spider.BookQueue;

/**
 * Unit test for simple App.
 */
public class TestBookQueue 
    
{
    @Test
    public void testBook() throws InterruptedException {
    	BookQueue bq = new BookQueue();
    	Book book = new Book();
    	book.setId(1);
    	book.setTitle("Test");
    	List<Book> books = new ArrayList<Book>();
    	books.add(book);
    	bq.addBook(books);
    	assertNotNull(books);
    }
}
