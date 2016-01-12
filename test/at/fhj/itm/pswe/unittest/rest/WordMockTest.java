package at.fhj.itm.pswe.unittest.rest;

import org.junit.Test;

import at.fhj.itm.pswe.dao.interfaces.IWord;
import at.fhj.itm.pswe.model.Word;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class WordMockTest {

	@Test
	public void testEditWord(){
		IWord mock=createMock(IWord.class);
		mock.changeWordActive("test",true);
		replay(mock);
		
		Word word=new Word();
		word.setText("test");
		word.setActive(true);
		mock.changeWordActive(word.getText(),word.getActive());
		
		verify(mock);
	}
}
