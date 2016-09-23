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

import net.arnx.jsonic.JSON;
import org.assertj.core.api.AbstractAssert;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * データセットと比較する.
 * Created by maeda on 7/13/2016.
 */
public class DataSetAssert extends AbstractAssert<DataSetAssert, IDataSet> {

	DataSetAssert(IDataSet actual) {
		super(actual, DataSetAssert.class);
	}

	/**
	 * リソースの内容と一致しているか検証する
	 * @param name リソース
	 * @return DataSetAssert
	 */
	public DataSetAssert dataSetOf(String name) throws DataSetException {
		if (!IDataSetMatcher.dataSetOf(name).matches(this.actual)){
			this.failWithMessage("内容が一致しません.\n(実際の値=\"%s\")\n（期待値=\"%s\"）",
					toJsonString(this.actual),
					toJsonString(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(name)))
			);
		}
		return this;
	}

	/**
	 * リソースの内容と一致しているか検証する
	 * @param name リソース
	 * @param replacement 置き換えるキー：値ペアのMap
	 * @return DataSetAssert
	 */
	public DataSetAssert dataSetOf(String name, Map<String, Object> replacement) throws DataSetException {
		ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(name)));
		replacement.keySet().forEach(key -> dataSet.addReplacementObject(key, replacement.get(key)));
		if (!new IDataSetMatcher(dataSet).matches(this.actual)){
			this.failWithMessage("内容が一致しません.\n(実際の値=\"%s\")\n（期待値=\"%s\"）",
					toJsonString(this.actual),
					toJsonString(dataSet)
			);
		}
		return this;
	}

	private String toJsonString(IDataSet iDataSet) throws DataSetException {
		Map<String, List<Map<String, Object>>> dataSet = new TreeMap<>();
		for(String tableName : iDataSet.getTableNames()) {
			List<Map<String, Object>> table = new ArrayList<>();
			ITable itable = iDataSet.getTable(tableName);
			List<String> columnNames = Arrays.stream(itable.getTableMetaData().getColumns()).map(Column::getColumnName).collect(Collectors.toList());
			for(int i = 0; i < itable.getRowCount(); i++) {
				Map<String, Object> record = new TreeMap<>();
				for(String columnName : columnNames) {
					Object value = itable.getValue(i, columnName);
					record.put(columnName.toUpperCase(), value);
				}
				table.add(record);
			}
			dataSet.put(tableName.toUpperCase(), table);
		}
		return JSON.encode(dataSet, false);
	}
}
