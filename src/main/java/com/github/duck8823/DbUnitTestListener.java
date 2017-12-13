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
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * {@link DataSet}をテストデータとして利用するリスナークラス
 * Created by maeda on 2016/02/28.
 */
public class DbUnitTestListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		before(testContext, testContext.getTestClass().getAnnotation(DataSet.class));
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		before(testContext, testContext.getTestMethod().getDeclaredAnnotation(DataSet.class));
	}

	private void before(TestContext testContext, DataSet dataSet) throws DatabaseUnitException, SQLException {
		if(dataSet != null){
			DatabaseConnection connection = new DatabaseConnection(testContext.getApplicationContext().getBean(DataSource.class).getConnection());
			ReplacementDataSet iDataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(dataSet.value())));
			iDataSet.addReplacementObject(dataSet.replaceNull(), null);
			DatabaseOperation.CLEAN_INSERT.execute(connection, iDataSet);
		}
	}
}
