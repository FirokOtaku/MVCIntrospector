# 注意

## 关于 Java 版本

MVCI 17.x 仅于 **Java17 环境** 下通过测试.  
更低 Java 版本中仍可能使用, 但是您需要手动调整 `firok.spring.mvci.MVCIntrospectProcessor` 上的 `@SupportedSourceVersion` 注解值和部分 MVCI 代码.

## 关于代码生成模板

**MVCI 本身** 的依赖非常有限,
仅引入了少数几个 Spring 组件用于提供增强功能.  
但是 **MVCI 默认的结构代码模板** 基于 SpringBoot 和 MybatisPlus,
所以默认情况下需在编译环境引入相关依赖, 否则项目无法通过编译.

[文档首页](home.md)

## 关于运行时辅助类

如果 IDE 开发环境支持 **增量编译**, `firok.spring.mvci.runtime` 包下辅助类内的数据可能不完整.

如果出现此情况, 请触发 **全量编译** 以补全数据.

如 IDEA 下可选择 [build] -> [Rebuild Project] 强制全量编译.=
