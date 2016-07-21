/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shunsuke Maeda
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.duck8823.matcher;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.assertion.FailureHandler;
import org.dbunit.dataset.IDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * {@link IDataSetMatcher}のテスト
 * Created by maeda on 2016/07/21.
 */
@PrepareForTest({Assertion.class})
@RunWith(PowerMockRunner.class)
public class IDataSetMatcherUnitTest {

	@Test
	public void Assertion_assertEqualsでDatabaseUnitExceptionが発生した場合にDatabaseUnitRuntimeExceptionをthrowする() throws Exception {
		IDataSet dataSet = mock(IDataSet.class);
		mockStatic(Assertion.class);
		//noinspection unchecked
		when(Assertion.class, "assertEquals", dataSet, dataSet).thenThrow(DatabaseUnitException.class);
		IDataSetMatcher matcher = new IDataSetMatcher(dataSet);
		try {
			matcher.matchesSafely(dataSet);
			fail();
		} catch (DatabaseUnitRuntimeException ignore){
		}
	}
}
