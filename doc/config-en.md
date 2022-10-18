# Configuration

All configurable items are listed as fields of `@MVCIntrospective`.

Most of those fields are annotated with `@AvailableValues` annotation,
which  describes the acceptable values of the relevant fields.  
A field can take any custom value (usually for a template string),
if `Constants.CUSTOM` is listed in a `@AvailableValues`.  
Or any other non-listed values would cause compile error.

`PREFER_XXX` values means that those values
could be overrided by upper configs;  
`DEFAULT` means the default value
(usually a default template string) would be taken.

`@MVCIntrospective` can be annotated on `class` or `package` ([see below](#do-not-generate-controller-for-all-bean-in-a-package)).  
When generating for a bean class,
we would search from children packages
to parent packages to find any `@MVCIntrospective`.  
Then decide each value of fields.

> Hope you know what is `package-info.java`

## Configurable items

* base package path when generating structures:    
  `basePackage`
* name of bean:  
  `beanNameShort`, `beanNameFull`
* whether generate some structures:  
  `generate XXX`
* templates used to generate name of structures:  
  `template XXX Name`
* templates used to generate content of structures:  
  `template XXX Content`
* templates used to generate package path of structures:  
  `template XXX Package`
* customized replacements for templates:  
  `extraParams`

## Examples

### do not generate service impl for a bean

bean/UserBean.java

```java
import lombok.Data;
import firok.spring.mvci.Constants;
import firok.spring.mvci.MVCIntrospective;

@Data
@MVCIntrospective(
	generateServiceImpl = Constants.FALSE
)
public class UserBean
{
	String username;
	String password;
}
```

### do not generate controller for all bean in a package

bean/package-info.java

```java
@MVCIntrospective(
	generateController = Constants.FALSE
)
package firok.demo.spring.bean;

import firok.spring.mvci.Constants;
import firok.spring.mvci.MVCIntrospective;
```

### modify names of structures

bean/UserBean.java

```java
import firok.spring.mvci.MVCIntrospective;
import lombok.Data;

@Data
@MVCIntrospective(
	beanNameShort = "UserInfo",
	beanNameFull = "UserInfoBean"
)
public class UserBean
{
  String username;
  String password;
}
```

Structure names for `UserBean` will become:  
`UserInfoMapper`, `UserInfoService`...

### modify template used to generate controller

bean/UserBeanTemplate.java

```java
public class UserBeanTemplate
{
	public static final String TEMPLATE = """
            package ##CONTROLLER_PACKAGE##;

            import org.springframework.web.bind.annotation.*;

            import ##BEAN_PACKAGE##.##BEAN_NAME_FULL##;
            import ##MAPPER_PACKAGE##.##MAPPER_NAME##;

            @RestController
            @RequestMapping("/##BEAN_NAME_FULL##")
            public class ##CONTROLLER_NAME## {
                @RequestMapping("/test")
                public String testMethod()
                {
                    return "##TEST_METHOD_DATA##";
                }
            }
            """;
}
```

bean/UserBean.java

```java
import firok.spring.mvci.MVCIntrospective;
import firok.spring.mvci.Param;
import lombok.Data;

@Data
@MVCIntrospective(
    templateControllerContent = UserBeanTemplate.TEMPLATE,
    extraParams = {
            @Param(
                    key = "##TEST_METHOD_DATA##",
                    value = "test-data"
            )
    }
)
public class UserBean
{
  String username;
  String password;
}
```

Then the `UserController` will only have one method `testMethod`  
with return value `"test-data"`.
