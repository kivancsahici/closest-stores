package com.jumbo.stores.controller;

public class Views {
	private Views() {
		throw new IllegalStateException("Utility class");
	}

	public static class Lazy {
	}

	public static class Eager extends Lazy {
	}
}