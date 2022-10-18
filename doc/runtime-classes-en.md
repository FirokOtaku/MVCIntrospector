# Runtime Helper Classes

After version _17.2.0_ MVCI would generate some classes
under `firok.spring.mvci.runtime` package,  
which contains information about all bean-classes
marked with `@MVCIntrospective` and related structures.


* `Current XXX Names` store full qualified names of related classes  
* `Current XXX Classes` store Class instances of related classes
* `Current XXX s` store all instances of related structures help you
  quickly get all of them rather than read them via Spring Context api

> For example, if `TestBean` has no `Controller` structure,  
> `CurrentControllerNames.NAMES` would not contain its information,  
> and `#getByFullQualifiedBeanName` would return `null`
> when passing full qualified name of `TestBean` as parameter

> By default `Current XXX s` Config classes would not be injected into Spring Context.  
> You need to set `firok.spring.mvci.runtime.enable-xxx-config` to `true`
> in config file (like `application.yml`)
> then use `@ComponentScan` to do that

> After 17.6.0,
> MVCI module no longer contains package `firok.spring.mvci.runtime`.
> All its comments are provides in package `firok.spring.mvci.comment`
> to give support of Java module system

## Examples

```java
// those classes will be generated at compiling phase
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
		// output names of all bean
		for (var beanName : CurrentBeanNames.NAMES)
		{
			System.out.println(beanName);
		}

		// get all mapper instances
		var mappers = currentMappers.getAllInstances();
		for (var mapper : mappers)
		{
			System.out.println(mapper);
		}
	}
}
```
