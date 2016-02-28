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
テストクラスに以下のアノテーション  
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
  
クラスまたはメソッドに`@DataSet`アノテーションを付ける  
```java
@DataSet("data.xml")
```
  
## License
* MIT  
    * see LICENCE.md