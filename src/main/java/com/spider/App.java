package com.spider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import au.com.bytecode.opencsv.CSVWriter;
/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		File file = new File("books.csv");
		if(file.exists())
			file.delete();
		
		String[] strs = {"ID","Title","Rate","Rate Count","Writer","Publisher","Publish","Date","Price"};  
		FileWriter mFileWriter = new FileWriter("books.csv", true);
	    CSVWriter csvWriter = new CSVWriter(mFileWriter, ',');  
	    csvWriter.writeNext(strs);
	    csvWriter.close();
		
		BookQueue bookQueue = new BookQueue();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		for(int i = 0; i < 10; i++) {
			ProducerThread producer = new ProducerThread(bookQueue, i*20);
			producer.setName("P"+i);
			ConsumerThread consumer = new ConsumerThread(bookQueue);
			consumer.setName("C"+i);
			
			executor.execute(producer);
			executor.execute(consumer);
		}
		
		
	}
}
