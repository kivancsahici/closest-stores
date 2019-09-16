package com.jumbo.stores.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

public class ViewsTest {
	@Test(expected = InvocationTargetException.class)
	public void utilityClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<?>[] constructors = Views.class.getDeclaredConstructors();

		Assert.assertEquals(1, constructors.length);

		if (constructors[0].isAccessible() || !Modifier.isPrivate(constructors[0].getModifiers())) {
			Assert.fail("constructor is not private");
		}

		constructors[0].setAccessible(true);
		constructors[0].newInstance();
	}
}
