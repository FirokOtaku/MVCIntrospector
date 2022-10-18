# 注意

### Java version

MVCI has only passed testing under **Java17**.
It may work in lower version of Java.
But you may need to edit
the `@SupportedSourceVersion` value
of `firok.spring.mvci.MVCIntrospectProcessor`
and some code of MVCI by yourself.

### Code template

**MVCI itself** has very limited amount of dependencies.

Only a few Spring components have been referenced
to provide enhancements.

But **the default templates used by MVCI**
is based on SpringBoot and MybatisPlus and so on.

By default, you should import them as dependencies,
or the project will not pass the compilation.
