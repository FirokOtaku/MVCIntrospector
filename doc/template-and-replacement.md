# 调整生成模板

> 默认的模板存放在 `resources` 目录下

根据模板生成内容时, 如下键会被替换:

键|含义|示例替换值
--|--|-----
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

大部分 **替换键** 的 **替换值** 也是根据模板生成的,
但我们不对生成相关 **替换值** 的顺序做任何保证,
在调整这些 **替换键模板** 时请 **不要** 使用除了 `##BEAN_NAME_FULL##`,
`##BEAN_NAME_SHORT##` 和 `##BEAN_PACKAGE##` 之外的 **替换键**.

> 换句话说, 模板里不要包含模板, 可能出问题

生成各 `##XXX_PACKAGE##` 替换值时,
会多执行一次额外替换,
将 `\.entity|bean\.`
替换为 `.mapper.`, `.service.`, `.service_impl.` 或 `.controller.`.

> 假如实体类定义于 `a.b.c.entity.d.e.TestEntity`,  
> 生成 controller 时默认位置为 `a.b.c.controller.d.e`

除去默认的替换键值,
还可以在 `@MVCIntrospective` 中的 `extraParams` 字段创建自定义替换键值.  
如果多个配置中包含相同替换键,
将采用 **就近原则**,
更接近实体定义的替换键值将会被采用.

## 示例

如果

* 在 `a.b` 包上指定了 `##TEST## = "test"`
* 在 `a.b.c` 包上指定了 `##TEST## = "test2"`
* 在 `a.b.c.TestBean` 上指定了 `##TEST## = "test3"`

则

* 生成 `a.b.c.TestBean` 实体时将使用 `##TEST## = "test3"`
* 生成 `a.b.c.Test2Bean` 实体时将使用 `##TEST## = "test2"`
* 生成 `a.b.DemoBean` 实体时将使用 `##TEST## = "test"`
