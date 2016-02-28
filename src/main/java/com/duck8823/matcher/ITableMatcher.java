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
import org.dbunit.dataset.ITable;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

/**
 * {@link ITable}を検証するMatcher
 * Created by maeda on 2016/02/28.
 */
public class ITableMatcher extends TypeSafeMatcher<ITable> {

	public static Matcher<ITable> tableOf(ITable expected){
		return new ITableMatcher(expected);
	}

	private final ITable expected;
	Optional<String> message = Optional.empty();

	ITableMatcher(ITable expected) {
		this.expected = expected;
	}

	@Override
	protected boolean matchesSafely(ITable actual) {
		try {
			Assertion.assertEquals(expected, actual);
		} catch (DatabaseUnitException e) {
			throw new DatabaseUnitRuntimeException(e);
		} catch (AssertionError e){
			message = Optional.ofNullable(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
		message.ifPresent(description::appendText);
	}
}
