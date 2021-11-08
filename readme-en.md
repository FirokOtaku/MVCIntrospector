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

`@MVCIntrospective` has multiple configurable fields. Each of them has detailed description at [Javadoc](/src/main/java/firok/spring/mvci/MVCIntrospective.java).

Most of those fields are annotated with `@AvailableValues` annotation, which  describes the acceptable values of the relevant fields. A field can take any custom value (usually for a template string), if `Constants.CUSTOM` is listed in a `@AvailableValues`. Or any other non-listed values would cause compile error.

`PREFER_XXX` values means that those values could be overrided by upper configs. `DEFAULT` means the default value (usually a default template string) would be taken.

`@MVCIntrospective` can be annotated on `class` or `package`.  
When generating for a bean class, we would search from children packages to parent packages to find any `@MVCIntrospective`. Then decide each value of fields.

> Hope you know what is `package-info.java`

### Replacing-key-value-pair for templates

You could adjust templates by editing `templateXXX` fields in `@MVCIntrospective`.

> default templates are stored in `resources` folder

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

Most of **replacing-value** of **replacing-key** are generated from templates. However, we do not guarantee the order in which the **replacing-values** are generated.

An extra replacement would be taken when generating `XXX_PACKAGE##`, which would replace the `\.entity|bean\.` to `.mapper.`, `.service.`, `.service_impl.` or `.controller.`.

> For a bean class `a.b.c.entity.d.e.TestEntity`,  
> package for controller should be `a.b.c.controller.d.e`

Except default replacing-key-value-pairs, you could create custom ones by editing `extraParams` of `@MVCIntrospective`. If multiple configs contain the same **replacing-key**, the **replacing-value** closer to bean class definition would be adopted.

> `a.b` → `##TEST## = "test"`  
> `a.b.c` → `##TEST## = "test2"`,  
> `a.b.c.TestBean` → `##TEST## = "test3"`,  
> `a.b.c.TestBean` ← `##TEST## = "test3"`,  
> `a.b.c.Test2Bean` ← `##TEST## = "test2"`,  
> `a.b.DemoBean` ← `##TEST## = "test"`

## Something else

**MVCI itself** is not based on SpringBoot and MybatisPlus. But **the default templates used by MVCI** is based on SpringBoot and MybatisPlus.  
By default, you should import them as dependencies, or the project will not pass the compilation.

In addition, you need to properly import dependencies such as database drivers, or your project may not work properly.

MVCI has only passed testing under **Java17**. It may work in lower version of Java. But you may need to edit the `@SupportedSourceVersion` value of `firok.spring.mvci.MVCIntrospectProcessor` and some code of MVCI.

## Changelog

### 17.1.1

* minor code improvements

### 17.1.0

* now we could adjust generation on package level

### 17.0.0

* increased supported JDK version to 17

### 1.0.0

* implemented base functions
