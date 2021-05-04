package org.playuniverse.brickforce.maprepository.storage.utils;

public final class Ref<O> {

	private O reference;

	public Ref() {}

	public Ref(O reference) {
		this.reference = reference;
	}

	public O get() {
		return reference;
	}

	public boolean has() {
		return reference != null;
	}

	public Ref<O> set(O reference) {
		this.reference = reference;
		return this;
	}

}
