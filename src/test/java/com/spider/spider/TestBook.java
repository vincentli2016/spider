package com.spider.spider;

import static org.junit.Assert.*;

import org.junit.Test;

import com.spider.Book;

/**
 * Unit test for simple App.
 */
public class TestBook 
    
{
    @Test
    public void testBook() {
    	Book book = new Book();
    	book.setId(1);
    	book.setTitle("Test");
    	assertNotNull(book.toString());
    }
}
