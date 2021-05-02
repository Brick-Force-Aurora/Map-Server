package org.playuniverse.brickforce.maprepository.model.data;

public class Int {

	private int value;

	public Int() {
		value = 0;
	}

	public Int(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

	public Int add(int value) {
		this.value += value;
		return this;
	}

	public Int sub(int value) {
		this.value -= value;
		return this;
	}

	public Int set(int value) {
		this.value = value;
		return this;
	}

	public Int multiply(int value) {
		this.value *= value;
		return this;
	}

	public Int divide(int value) {
		this.value /= value;
		return this;
	}

	public Int inc() {
		value++;
		return this;
	}
	
	public int incGet() {
		return value++;
	}

	public Int dec() {
		value--;
		return this;
	}
	
	public int decGet() {
		return value--;
	}

	public Int reset() {
		value = 0;
		return this;
	}

}
