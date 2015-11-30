package at.fhj.itm.pswe.pagecrawler.wordanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReaderFilterWords {
	private BufferedReader br;
	public List<String> readWords(){
		List<String> words=new ArrayList<String>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			br = new BufferedReader(new FileReader(new File(classLoader.getResource("/bindewoerter.txt").getFile())));
			String line = null;
			while((line = br.readLine()) != null) {          
				String[] word=line.split("\n");
				System.out.println("Binde: "+word[0]);
				words.add(word[0]);

			}
		} catch(FileNotFoundException ef) {
			ef.printStackTrace();
		} catch(IOException ei) {
			ei.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return words;
	}


}
