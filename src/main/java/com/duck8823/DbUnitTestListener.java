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
package com.duck8823;

import com.duck8823.annotation.DataSet;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * {@link DataSet}をテストデータとして利用するリスナークラス
 * Created by maeda on 2016/02/28.
 */
public class DbUnitTestListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		Optional<DataSet> dataSet = Optional.ofNullable(testContext.getTestClass().getAnnotation(DataSet.class));
		before(testContext, dataSet);
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		Optional<DataSet> dataSet = Optional.ofNullable(testContext.getTestMethod().getDeclaredAnnotation(DataSet.class));
		before(testContext, dataSet);
	}

	private void before(TestContext testContext, Optional<DataSet> dataSetOpt) {
		dataSetOpt.ifPresent(dataSet -> {
			try {
				DatabaseConnection connection = new DatabaseConnection(testContext.getApplicationContext().getBean(DataSource.class).getConnection());
				IDataSet iDataSet = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(dataSet.value()));
				DatabaseOperation.CLEAN_INSERT.execute(connection, iDataSet);
			} catch (SQLException | DatabaseUnitException e) {
				throw new IllegalStateException(e);
			}
		});
	}
}
