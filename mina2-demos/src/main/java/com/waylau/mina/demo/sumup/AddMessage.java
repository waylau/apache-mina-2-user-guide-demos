package com.waylau.mina.demo.sumup;

public class AddMessage extends AbstractMessage {
	private static final long serialVersionUID = -940833727168119141L;

	private int value;

	public AddMessage() {
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		// it is a good practice to create toString() method on message classes.
		return getSequence() + ":ADD(" + value + ')';
	}
}
