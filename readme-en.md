# MVC Introspector

MVCI is based on Annotation Processing Tool,  
can generate whole set of MVC CRUD code for SpringBoot during compiling.  
(mapper, service, service impl, controller)

## How to use

* Import MVCI as dependency
* Create a file named `javax.annotation.processing.Processor` in directory `META-INF/services/`  
  Content is:  
  ```text
  firok.spring.mvci.MVCIntrospectProcessor
  ```
* Enable ADT in your develop environment
* Create JavaBean(s) marked with `@BeanIntrospective` for database table(s)
* Re-build and start project

### How to customize generation templates

Just edit `xxxTemplate` value in `@MVCIntrospective`.

> Resetting any of those values to `firok.spring.mvci.Constants.DISABLE` (`"##DISABLE##"`) will disable generating that code

> If you do not want to write a long string template for many times, you could define that value as a `public static final String` field and use it in annotation.

Default templates are stored in `firok.spring.mvci.internal.DefaultXXXTemplate`.

When customizing templates, keys below will be replaced:

Key | Meaning | Example
-|-|-
`##BEAN_NAME_FULL##` | full entity name | `TestBean`, `TestEntity`, `BeanTest`, `EntityTest`
`##BEAN_NAME_SHORT##` | short entity name | `Test`
`##MAPPER_NAME##` | mapper name | `TestMapper`
`##SERVICE_NAME##` | service name | `TestService`
`##SERVICE_IMPL_NAME##` | service impl name | `TestServiceImpl`
`##CONTROLLER_NAME##` | controller name | `TestController`
`##BEAN_PACKAGE##` | entity package | `firok.spring.demo.bean`, `firok.spring.demo.entity`
`##MAPPER_PACKAGE##` | mapper package | `firok.spring.demo.mapper`
`##SERVICE_PACKAGE##` | service package | `firok.spring.demo.service`
`##SERVICE_IMPL_PACKAGE##` | service impl package | `firok.spring.demo.service.impl`
`##CONTROLLER_PACKAGE##` | controller package | `firok.spring.demo.controller`

### How to customize generation names

Editing `xxxPackage` of `@MVCIntrospective` would change the package to generate; Editing `xxxName` would change the name to generate.

## Something else

**MVCI itself** is not based on SpringBoot and MybatisPlus. But **the default templates used by MVCI** is based on SpringBoot and MybatisPlus.  
By default, you should import them as dependencies, or the project will not pass the compilation.

A usable maven dependency is:  

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.4.3.2</version>
</dependency>
```

In addition, you need to properly import dependencies such as database drivers, or your project may not work properly.

MVCI has only passed testing under **Java16**. It may work in lower version of Java. But you may need to edit the `@SupportedSourceVersion` value of `firok.spring.mvci.MVCIntrospectProcessor` and some code of MVCI.

