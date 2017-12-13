package com.duck8823.matcher;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * {@link DataSetAssertions}のテスト
 * Created by maeda on 7/21/2016.
 */
public class DataSetAssertionsTest {

	@Test
	public void コンストラクタのテスト() {
		try {
			new DataSetAssertions();
		} catch (Exception ignore) {
			fail();
		}
	}
}
