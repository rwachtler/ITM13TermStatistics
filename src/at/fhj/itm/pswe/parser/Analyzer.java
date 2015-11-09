package at.fhj.itm.pswe.parser;


import java.util.HashMap;
//Analyzer ana=new Analyzer("Hallo Test Welt Test Test Hallo test");

public class Analyzer {

	private String input;

	public Analyzer(String result){
		this.input=result;
	}

	public HashMap<String,Integer> countword(){
		HashMap<String,Integer> wordmap=new HashMap<String,Integer>();
		String[] inputWords=input.split(" ");
		for(int i=0; i<inputWords.length;i++){
			if(wordmap.containsKey(inputWords[i])){
				wordmap.put(inputWords[i],(Integer) wordmap.get(inputWords[i])+1);
			}else{
				wordmap.put(inputWords[i], 1);
			}
		}

		return wordmap;
	}

}
