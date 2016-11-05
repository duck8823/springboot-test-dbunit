# springboot-test-dbunit
[![Build Status](https://travis-ci.org/duck8823/springboot-test-dbunit.svg?branch=master)](https://travis-ci.org/duck8823/springboot-test-dbunit)
[![Coverage Status](http://coveralls.io/repos/github/duck8823/springboot-test-dbunit/badge.svg?branch=master)](https://coveralls.io/github/duck8823/springboot-test-dbunit?branch=master)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)  
springbootでDbUnitのデータセットを使う.  
  

## 基本的な使い方
### Maven
pom.xml
```xml
<repository>
	<id>duck8823.com</id>
	<name>duck8823.com</name>
	<url>http://www.duck8823.com/maven</url>
</repository>
...
<dependency>
	<groupId>com.duck8823</groupId>
	<artifactId>springboot-test-dbunit</artifactId>
	<version>0.0.6</version>
	<scope>test</scope>
</dependency>
```

### テストデータの用意  
data.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<dataset>
	<Person id="1" name="test_1" />
	<Person id="2" name="test_2" />
	<Person id="3" name="test_3" />
	<Person id="4" name="test_4" />
	<Person id="5" name="test_5" />
</dataset>
```
  
NULLのインサート
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<dataset>
	<Person id="1" name="test_1" />
	<Person id="2" name="{null}" />
</dataset>
```
デフォルトでは `{null}` を置換する.  
  
  
### アノテーション  
テストクラスに以下のアノテーションを付ける.  
```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HogeApplication.class)
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestListener.class
})
@Transactional
public class HogeTest {
    ...
}
```
  
クラスまたはメソッドに`@DataSet`アノテーションを付ける.  
```java
@DataSet("data.xml")
@Test
public void test() {
    ...
}
```
  
  
### マッチャーの利用
データベースの値とxmlファイルで指定したデータを比較検証できます.
#### AssertJスタイル
パスを指定
```java
import static com.duck8823.matcher.DataSetAssertions.assertThat;
...
@Test
public void test() {
    assertThat(dataSource).dataSetOf("expected.xml");
}
```
xml内の文字列をオブジェクトに置換できる
```java
import static com.duck8823.matcher.DataSetAssertions.assertThat;
...
@Test
public void test() {
    assertThat(dataSource).dataSetOf("expected.xml", Collections.singletonMap("{null}", null));
}
```

#### IDataSetMatcher
パスを指定
```java
import static com.duck8823.mathcer.IDataSetMatcher.dataSetOf;
import static org.junit.Assert.assertThat;
...
@Test
public void test() {
    IDataSet actual = new DatabaseConnection(dataSource.getConnection()).createDataSet();
    assertThat(actual, dataSetOf("expected.xml"));
}
```
  
IDataSetを指定
```java
import static com.duck8823.mathcer.IDataSetMatcher.dataSetOf;
import static org.junit.Assert.assertThat;
...
@Test
public void test() {
    IDataSet actual = new DatabaseConnection(dataSource.getConnection()).createDataSet();
    IDataSet expected = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("expected.xml"))
    assertThat(actual, dataSetOf(expected));
}
```
  
#### ITableMatcher
ITableを指定
```java
import static com.duck8823.mathcer.ITableMatcher.tableOf;
import static org.junit.Assert.assertThat;
...
@Test
public void test() {
    ITable actual = new DatabaseConnection(dataSource.getConnection()).createDataSet().getTable("hoge");
    ITable expected = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("expected.xml")).getTable("hoge");
    assertThat(actual, tableOf(expected));
}
```
## License
* MIT  
    * see LICENCE.md
