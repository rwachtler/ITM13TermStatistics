package at.fhj.itm.pswe.unittest.dao;

import junit.framework.Assert;

import org.junit.Test;

import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.dao.interfaces.IWord;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.rest.WordEndpoint;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class WordMockTest {

	@Test
	public void testChangeWord(){
		IWord mock=createMock(IWord.class);
		mock.changeWordActive("test",true);
		replay(mock);
		
		Word word=new Word();
		word.setText("test");
		word.setActive(true);
		mock.changeWordActive(word.getText(),word.getActive());
		
		verify(mock);
	}
	
	@Test
	public void testFindSingleWord(){
		
		IWord mock=createMock(IWord.class);
		mock.findSingleWordWithAmount("test");
		replay(mock);

		Word word=new Word();
		word.setText("test");
		word.setActive(true);
		mock.findSingleWordWithAmount(word.getText());
		
		verify(mock);
		//Assert.assertEquals(mock.findSingleWordWithAmount("test"), "{'name':test, 'amount': 2,'active':true}");
		
		
	}
}
