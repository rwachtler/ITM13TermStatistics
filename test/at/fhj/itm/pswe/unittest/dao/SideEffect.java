package at.fhj.itm.pswe.unittest.dao;

import org.easymock.IAnswer;

abstract public class SideEffect implements IAnswer<Void> {
	public Void answer() throws Throwable {
		effect();
		// Void is an uninstantiable placeholder class
		return null;
	}

	abstract public void effect() throws Throwable;
}
