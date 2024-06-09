# PangolinToolkit

JavaFx写的一个工具

## compile

```
mvn idea:idea
mvn clean compile install -U -D maven.test.skip=true

```

## package to *.zip

```
cd pangolin-tool
mvn clean javafx:jlink -U -D maven.test.skip=true
```

## package to *.exe

```
jpackage --type app-image -n PangolinToolkit -m "com.a.b.c.d.pangolin.tool/com.a.b.c.d.pangolin.tool.MainApplication" --icon "./src/main/resources/icon/pangolin_32.ico" --runtime-image "./target/PangolinToolkit" --dest "./target/build-package" --java-options "-Xms64m -Xmx512m"
```

## add module-info.java

###  

``` 1、lz4-java-1.8.0.jar
cd lib
# 生成module-info.java
jdeps --ignore-missing-deps --generate-module-info . lz4-java-1.8.0.jar

# 生成module-info.class
javac --patch-module org.lz4.java=lz4-java-1.8.0.jar org.lz4.java/module-info.java

# 给lz4-java-1.8.0.jar添加module-info.class
jar uf lz4-java-1.8.0.jar -C org.lz4.java module-info.class
```

### 2、snappy-java-1.1.10.5.jar

```
cd lib
# 生成module-info.java
jdeps --ignore-missing-deps --generate-module-info . snappy-java-1.1.10.5.jar

# 生成module-info.class
javac --patch-module snappy.java=snappy-java-1.1.10.5.jar snappy.java/module-info.java

# 给lz4-java-1.8.0.jar添加module-info.class
jar uf snappy-java-1.1.10.5.jar -C snappy.java module-info.class
```