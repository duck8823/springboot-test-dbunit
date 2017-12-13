package com.duck8823.matcher;

import com.duck8823.DbUnitTestListener;
import com.duck8823.TestApplication;
import com.duck8823.annotation.DataSet;
import com.duck8823.dao.PersonDao;
import com.duck8823.model.Person;
import org.apache.commons.collections.map.SingletonMap;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Collections;
import java.util.List;

import static com.duck8823.matcher.DataSetAssertions.assertThat;
import static com.duck8823.matcher.IDataSetMatcher.dataSetOf;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.junit.Assert.fail;

/**
 * {@link DataSetAssert}のテスト
 * Created by maeda on 7/14/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestListener.class
})
@Transactional
public class DataSetAssertTest {

	@Autowired
	private PersonDao personDao;

	@Autowired
	private DataSource dataSource;

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void IDataSetで正しい予測データセットを指定したときに成功する() throws Exception {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, "hoge");
		personDao.add(person);

		IDataSet actual = new DatabaseConnection(dataSource.getConnection()).createDataSet();
		IDataSet expect = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/META-INF/dbtest/expected.xml"));

		Assert.assertThat(actual, dataSetOf(expect));
	}

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void dataSourceで正しい予測データセットを指定したときに成功する() throws Exception {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, "hoge");
		personDao.add(person);

		assertThat(dataSource).dataSetOf("/META-INF/dbtest/expected.xml");
	}

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void IDataSetで異なる予測データセットを指定したときに失敗する() throws SQLException, DatabaseUnitException {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, "hoge");
		personDao.add(person);

		IDataSet actual = new DatabaseConnection(dataSource.getConnection()).createDataSet();
		try {
			assertThat(actual).dataSetOf("/META-INF/dbtest/failed_expected.xml");
			throw new Exception();
		} catch (AssertionError e){
			//ok
		} catch (Exception e) {
			fail();
		}
	}

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void dataSourceで異なる予測データセットを指定したときに失敗する() throws SQLException, DatabaseUnitException {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, "hoge");
		personDao.add(person);

		try {
			assertThat(dataSource).dataSetOf("/META-INF/dbtest/failed_expected.xml");
			throw new Exception();
		} catch (AssertionError e){
			//ok
		} catch (Exception e) {
			fail();
		}
	}

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void dataSourceで正しい予測データセットを置換して指定したときに成功する() throws Exception {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, null);
		personDao.add(person);

		assertThat(dataSource).dataSetOf("/META-INF/dbtest/replaced_expected.xml", Collections.singletonMap("{replacement}", null));
	}

	@DataSet("/META-INF/dbtest/data.xml")
	@Test
	public void dataSourceで異なる予測データセットを置換して指定したときに失敗する() throws Exception {
		List<Person> list = personDao.list();
		Assert.assertThat(new BigDecimal(list.size()), comparesEqualTo(new BigDecimal(5L)));

		Person person = new Person(6L, "{replacement}");
		personDao.add(person);

		try {
			assertThat(dataSource).dataSetOf("/META-INF/dbtest/failed_expected.xml", Collections.singletonMap("{replacement}", "{replaced}"));
			throw new Exception();
		} catch (AssertionError e){
			//ok
		} catch (Exception e) {
			fail();
		}
	}
}
