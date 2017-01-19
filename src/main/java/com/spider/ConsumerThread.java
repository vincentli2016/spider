package com.spider;

import java.io.FileWriter;
import java.io.IOException;
import au.com.bytecode.opencsv.CSVWriter;

public class ConsumerThread extends Thread {
	private BookQueue bookQueue;
	
    
	public ConsumerThread(BookQueue bookQueue) throws IOException {
		this.bookQueue = bookQueue;
		//fileWriter.append("ID,Title,Rate,Rate Count,Writer,Publisher,Publish Date,Price");
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				int count = 0;
				while (count <= 20) {
					System.out.println(bookQueue.size());
					Book book = bookQueue.getBook();
					if (null != book) {
						writeFile(book);
					}
					count++;
				}
			}
		} catch (InterruptedException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int count;
	
	private synchronized void writeFile(Book book) throws IOException, InterruptedException {
		if(count < 40) {
			FileWriter mFileWriter = new FileWriter("books.csv", true);
		    CSVWriter csvWriter = new CSVWriter(mFileWriter, ',');  
			csvWriter.writeNext(book.toString().split("#"));
	        csvWriter.close();  
		} else {
			wait();
		}
        count++;
	}

}
