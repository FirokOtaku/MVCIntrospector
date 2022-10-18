# 运行时辅助类

_17.2.0_ 版本之后的 MVCI 在参与编译时会在 `firok.spring.mvci.runtime` 包下生成数个类,  
操作这些类可以获取本项目中所有标注了 `@MVCIntrospective` 注解并生成了相应结构的实体信息.

* `Current XXX Names` 以字符串方式储存了对应结构的完整限定名  
* `Current XXX Classes` 储存了对应结构的 Class 对象
* `Current XXX s` 是一个包含了各结构的 Spring Config 类

> 如果实体 `TestBean` 没有配置生成 `Controller` 结构,    
> 在 `CurrentControllerNames.NAMES` 数组内就不会包含相应信息,    
> 且使用 `TestBean` 的完整限定名作为参数调用 `#getByFullQualifiedBeanName` 将会返回 `null`

> 默认情况下 `Current XXX s` Config 类不会注入到 Spring Context,  
> 需要在配置文件中 (如 `application.yml`)
> 将 `firok.spring.mvci.runtime.enable-xxx-config` 设为 `true`,  
> 并使用 `@ComponentScan` 手动设置 Spring 组件扫描

> _17.6.0_ 版本开始, 为了兼容 Java 模块化,  
> `firok.spring.mvci` 模块不再直接包含 `firok.spring.mvci.runtime` 包.  
> 这些辅助类的结构和注释都转移到了 `firok.spring.mvci.comment` 包

## 示例

```java
// 这几个类会在编译时自动生成
import firok.spring.mvci.runtime.CurrentBeanNames;
import firok.spring.mvci.runtime.CurrentMappers;

@RestController
public class TestController
{
	@Autowired
	public CurrentMappers currentMappers;

	@RequestMapping("/test")
	public void test()
	{
		// 输出所有实体名称
		for (var beanName : CurrentBeanNames.NAMES)
		{
			System.out.println(beanName);
		}

		// 快速获取所有的 mapper
		var mappers = currentMappers.getAllInstances();
		for (var mapper : mappers)
		{
			System.out.println(mapper);
		}
	}
}
```

[文档首页](home.md)
