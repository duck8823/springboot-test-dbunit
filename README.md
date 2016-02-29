# springboot-test-dbunit
springbootでDbUnitのデータセットを使う.  
  

## 基本的な使い方
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
#### IDataSetMatcher
パスを指定
```java
import static com.duck8823.IDataSetMatcher.dataSetOf;
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
import static com.duck8823.IDataSetMatcher.dataSetOf;
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
import static com.duck8823.ITableMatcher.tableOf;
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