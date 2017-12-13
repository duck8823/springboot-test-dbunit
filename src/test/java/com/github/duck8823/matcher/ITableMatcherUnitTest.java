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

import com.duck8823.DbUnitTestListener;
import com.duck8823.TestApplication;
import com.duck8823.annotation.DataSet;
import com.duck8823.dao.PersonDao;
import com.duck8823.model.Person;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static com.duck8823.matcher.ITableMatcher.tableOf;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * {@link ITableMatcher}のテスト
 * Created by maeda on 2016/07/21.
 */
@PrepareForTest({Assertion.class})
@RunWith(PowerMockRunner.class)
public class ITableMatcherUnitTest {

	@Test
	public void DatabaseOperation_CLEAN_INSERT_executeでDatabaseUnitExceptionが発生した場合にDatabaseUnitRuntimeExceptionをthrowする() throws Exception {
		ITable table = mock(ITable.class);
		mockStatic(Assertion.class);
		//noinspection unchecked
		when(Assertion.class, "assertEquals", table, table).thenThrow(DatabaseUnitException.class);
		ITableMatcher matcher = new ITableMatcher(table);
		try {
			matcher.matchesSafely(table);
			fail();
		} catch (DatabaseUnitRuntimeException ignore){
		}
	}
}
