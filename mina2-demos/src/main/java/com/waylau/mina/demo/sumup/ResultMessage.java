package com.waylau.mina.demo.sumup;

public class ResultMessage extends AbstractMessage {
	private static final long serialVersionUID = 7371210248110219946L;

	private boolean ok;

	private int value;

	public ResultMessage() {
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (ok) {
			return getSequence() + ":RESULT(" + value + ')';
		} else {
			return getSequence() + ":RESULT(ERROR)";
		}
	}
}
