package com.chappie.engine.utils;

public class Pair<T, A> {
	
	private T first;
	private A second;
	
	public Pair(T first, A second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public A getSecond() {
		return second;
	}

	public void setSecond(A second) {
		this.second = second;
	}
	
}
