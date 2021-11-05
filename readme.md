# MVC Introspector

基于 Annotation Processing Tool 技术, 工作于编译期,  
为 SpringBoot 生成一整套 MVC CRUD 结构.  
(包含 mapper, service, service impl, controller)

[Readme - English](readme-en.md) (尚未更新至17.1.x)

## 使用方式

* 在项目中引入 MVC Introspector 依赖
* 在 `META-INF/services/` 目录下建立文件 `javax.annotation.processing.Processor`  
  内容为
  ```text
  firok.spring.mvci.MVCIntrospectProcessor
  ```
* 启用开发环境中的 ADT 功能, 这通常需要在 IDE 中进行配置
* 为数据库表创建对应的 JavaBean 实体类, 并在 **合适位置** 标注 `@MVCIntrospective` 注解
* 重新编译并启动项目

### 配置与调整生成

`@MVCIntrospective` 有多个字段可供配置, 各字段配置含义在 [Javadoc 中](/src/main/java/firok/spring/mvci/MVCIntrospective.java) 有详细描述.

大部分字段标注了 `@AvailableValues`注解, 这描述了相关字段可接受的值.  
如果 `@AvailableValues` 中包含 `Constants.CUSTOM`, 则表示此字段可以提供任意自定义值 (一般是自定义模板字符串);  
否则只能为此字段提供预设值之一, 提供其它值会使得代码无法通过编译.

预设值中的 `PREFER_XXX` 表示此值可以被上层配置值覆盖, 否则使用指定值;  
`DEFAULT` 值则表示此字段强制使用 MVCI 给定的默认值 (一般是默认模板).

`@MVCIntrospective` 可以标注到 **实体类** 或 **包** 上.  
生成一个实体时, 会按照从子包到父包的顺序, 依次向上寻找有无 **包级** 配置注解, 最后决定每个配置字段的值.

> 希望你知道 `package-info.java` 这玩意

### 生成模板的替换键

修改 `@MVCIntrospective` 注解中的 `templateXXX` 值即可调整相关的生成模板.

默认的模板存放在 `resources` 目录下.

根据模板生成内容时, 如下键会被替换:  

键|含义|示例替换值
-|-|-
`##BEAN_NAME_FULL##` | 实体完整名 | `TestBean`, `TestEntity`, `BeanTest`, `EntityTest`
`##BEAN_NAME_SHORT##` | 实体简称 | `Test`
`##MAPPER_NAME##` | Mapper 名称 | `TestMapper`
`##SERVICE_NAME##` | Service 名称 | `TestService`
`##SERVICE_IMPL_NAME##` | Service Impl 名称 | `TestServiceImpl`
`##CONTROLLER_NAME##` | Controller 名称 | `TestController`
`##BEAN_PACKAGE##` | 实体位置 | `firok.spring.demo.bean`, `firok.spring.demo.entity`
`##MAPPER_PACKAGE##` | Mapper 位置 | `firok.spring.demo.mapper`
`##SERVICE_PACKAGE##` | Service 位置 | `firok.spring.demo.service`
`##SERVICE_IMPL_PACKAGE##` | Service Impl 位置 | `firok.spring.demo.service.impl`
`##CONTROLLER_PACKAGE##` | Controller 位置 | `firok.spring.demo.controller`

虽然大部分 **替换键** 的 **替换值** 也是根据模板生成的, 但我们不对生成相关 **替换值** 的顺序做任何保证, 在调整这些 **替换键模板** 时请 **不要** 使用除了 `##BEAN_NAME_FULL##`, `##BEAN_NAME_SHORT##` 和 `##BEAN_PACKAGE##` 之外的 **替换键**.

生成各 `##XXX_PACKAGE##` 替换值时, 会多执行一次额外替换, 替换内容是将 `\.entity|bean\.` 替换为 `.mapper.`, `.service.`, `.service_impl.` 或 `.controller.`.

> 假如实体类定义于 `a.b.c.entity.d.e.TestEntity`,  
> 生成 controller 时默认位置为 `a.b.c.controller.d.e`

除去默认的替换键值, 还可以在 `@MVCIntrospective` 中的 `extraParams` 字段创建自定义替换键值. 如果多个配置中包含相同替换键, 将采用 **就近原则**, 更接近实体定义的替换键值将会被采用.

> 假如在 `a.b` 包上指定了 `##TEST## = "test"`,  
> 假如在 `a.b.c` 包上指定了 `##TEST## = "test2"`,  
> 在 `a.b.c.TestBean` 上指定了 `##TEST## = "test3"`,  
> 生成 `a.b.c.TestBean` 实体时将使用 `##TEST## = "test3"`,  
> 生成 `a.b.c.Test2Bean` 实体时将使用 `##TEST## = "test2"`,  
> 生成 `a.b.DemoBean` 实体时将使用 `##TEST## = "test"`

## 注意

**MVCI 本身** 不基于 SpringBoot 和 MybatisPlus,  
但是 **MVCI 默认的结构代码模板** 基于 SpringBoot 和 MybatisPlus.    
所以默认情况下需为编译环境引入相关依赖, 否则项目无法通过编译.

此外, 还需要正确提供数据库驱动等依赖, 否则项目可能无法正常运行.

MVCI 仅于 **Java17 环境** 下通过测试.  
更低 Java 版本中仍可能使用, 但是您需要手动调整 `firok.spring.mvci.MVCIntrospectProcessor` 上的 `@SupportedSourceVersion` 注解值和部分 MVCI 代码.

## 变动记录

### 17.1.1

* 细微代码改动

### 17.1.0

* 调整配置方式, 现在可以以包为级别批量调整生成

### 17.0.0

* 提升JDK支持版本至17

### 1.0.0

* 实现基本功能