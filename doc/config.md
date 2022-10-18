# 配置生成

所有配置项都作为 `@MVCIntrospective` 注解的字段列出并提供了详细的注释.

大部分字段标注了 `@AvailableValues` 注解, 这描述了相关字段可接受的值.  
如果 `@AvailableValues` 中包含 `Constants.CUSTOM`, 则表示此字段可以提供任意自定义值 (一般是自定义模板字符串);  
否则只能为此字段提供预设值之一, 提供其它值会使得代码无法通过编译.

预设值中的 `PREFER_XXX` 表示此值可以被上层配置值覆盖, 否则使用指定值;  
`DEFAULT` 值则表示此字段强制使用 MVCI 给定的默认值 (一般是默认模板).

`@MVCIntrospective` 注解也可以 [标注在包上](#不为某个包下的实体类生成-controller),
以此快速调整某包内所有实体类的生成配置.  
生成一个实体时, 会按照从子包到父包的顺序, 
依次向上寻找有无 **包级** 配置注解, 最后决定每个配置字段的值.

> 希望你知道 `package-info.java` 这玩意

## 配置项

* 生成结构时的基础包路径:  
  `basePackage`
* 实体类名称:  
  `beanNameShort`, `beanNameFull`
* 是否生成某个结构:  
  `generate XXX`
* 生成某结构时使用的名称模板:  
  `template XXX Name`
* 生成某结构时使用的内容模板:  
  `template XXX Content`
* 生成某结构时使用的包路径模板:  
  `template XXX Package`
* 自定义模板替换内容:  
  `extraParams`

## 示例

### 不为指定类生成 service impl

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

### 不为某个包下的实体类生成 controller

bean/package-info.java

```java
@MVCIntrospective(
	generateController = Constants.FALSE
)
package firok.demo.spring.bean;

import firok.spring.mvci.Constants;
import firok.spring.mvci.MVCIntrospective;
```

### 调整为某实体类生成结构时使用的名称

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

为 `UserBean` 生成结构的名称将变为:  
`UserInfoMapper`, `UserInfoService`...

### 调整生成 controller 模板

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

此时生成的 `UserController` 便只包含一个 `testMethod`,  
其返回值是 `"test-data"`.
