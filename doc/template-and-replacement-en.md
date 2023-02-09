# Modify templates

Default templates (of mapper, service, serviceImpl and controller)
are based on MybatisPlus. If needed, you could
change values of `@MVCIntrospective` fields (`templateMapperContent`, `templateServiceContent`,
`templateServiceImplContent`, `templateControllerContent`) to replace default templates.

> default templates are stored in `resources` folder

When customizing templates,
keys below will be replaced:

Key | Meaning | Example
--|--|-----
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

Most of **replacing-value** of **replacing-key** are generated from templates.
However, we do not guarantee the order in which the **replacing-values** are generated.

> In other words,
> a template should not include other templates,
> which may cause problems

An extra replacement would be taken when generating `XXX_PACKAGE##`,
which would replace the `\.entity|bean\.` to `.mapper.`, `.service.`, `.service_impl.` or `.controller.`.

> For a bean class `a.b.c.entity.d.e.TestEntity`,  
> package for controller should be `a.b.c.controller.d.e`

Except default replacing-key-value-pairs,
you could create custom ones
by editing `extraParams` of `@MVCIntrospective`.
If multiple configs contain the same **replacing-key**,
the **replacing-value** closer to bean class definition would be adopted.


## Examples

If

* `a.b` → `##TEST## = "test"`
* `a.b.c` → `##TEST## = "test2"`
* `a.b.c.TestBean` → `##TEST## = "test3"`

Then

* `a.b.c.TestBean` ← `##TEST## = "test3"`
* `a.b.c.Test2Bean` ← `##TEST## = "test2"`
* `a.b.DemoBean` ← `##TEST## = "test"`

[Home](home-en.md)
