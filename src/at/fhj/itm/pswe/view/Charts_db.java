package at.fhj.itm.pswe.view;

import java.util.List;
import at.fhj.itm.pswe.database.DbConnection;

public class Charts_db {
	private DbConnection db;
	
	public Charts_db(){
		db=new DbConnection();
	}
	
	public void linechartOverPeriod(){
		//the word which should be counted on each date between the start and enddate
		List<Integer> amountList=db.countWordOverPeriod("spoke", "19.11.2015", "20.11.2015");
		for(Integer i : amountList ){
			System.out.println(i);
		}
	}
	
	public void barchartfrequentWordofSide(){
		//one of the domains and the amount how much words the result should contain
		db.findFrequentWordsOfSide("http://pswengi.bamb.at", 10);	
	}
	
	public void barchartOneWordAllSides(){
		//the word which should be counted on all sides
		db.findOneWordAllSides("spoke");
	}

}
