# Installation

* Import MVCI as dependency.  
  Following methods are available:
  1. `clone repo` and `mvn install`
  2. Download pre-built .jar from [GitHub releases](https://github.com/FirokOtaku/MVCIntrospector/releases)
  3. Use GitHub Maven Packages
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

> You need to [config authentication](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) to use GitHub Maven Packages

* create file `javax.annotation.processing.Processor` at folder `META-INF/services/` with following content:
  ```text
  firok.spring.mvci.MVCIntrospectProcessor
  ```

After that, enable APT in your develop environment.
Create JavaBean(s) marked with `@BeanIntrospective` for database table(s)
and re-build project.
Then you could see the effects.

[Configuration](config-en.md)

[Home](home-en.md)
