# 安装

* 在项目中引入依赖.  
  目前可以使用如下方式:
  * `clone repo` 并 `mvn install`
  * 从 [GitHub releases](https://github.com/FirokOtaku/MVCIntrospector/releases) 下载编译好的 .jar;
  * 使用 GitHub Maven Packages
    ```xml
    <repositories>
      <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/FirokOtaku/MVCIntrospector</url>
      </repository>
    </repositories>
    
    <dependencies>
      <dependency>
        <groupId>firok.spring</groupId>
        <artifactId>mvci</artifactId>
        <version>{VERSION}</version>
      </dependency>
    </dependencies>
    ```

> 正常使用 GitHub Maven Packages 需 [配置验证](https://docs.github.com/cn/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

* 在 `META-INF/services/` 目录下建立文件 `javax.annotation.processing.Processor`  
  内容为
  ```text
  firok.spring.mvci.MVCIntrospectProcessor
  ```
* 启用开发环境中的 APT 功能, 这通常需要在 IDE 中进行配置

安装并启用完成后,  
为数据库表创建对应的 JavaBean 实体类,  
并在 **合适位置** (见下节) 标注 `@MVCIntrospective` 注解,  
重新编译并启动项目即可看到结果.

[配置生成](config.md)

[文档首页](home.md)
