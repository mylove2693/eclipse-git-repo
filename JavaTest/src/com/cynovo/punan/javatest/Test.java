package com.cynovo.punan.javatest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

	public static final String FILE_PATH = "/home/punan/Desktop/test.txt";
	
	private static String test = "This is a test message\n";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			File file = new File(FILE_PATH);
			FileWriter fw = new FileWriter(file, true);
			fw.write(test);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
