# PangolinToolkit

JavaFx写的一个工具

## compile

```
mvn idea:idea
mvn clean compile install -U -D maven.test.skip=true

```

## package to *.zip

> 模块化,暂时用不了

```
cd pangolin-tool
mvn clean javafx:jlink -U -D maven.test.skip=true
```

## 生成exe文件

> 非模块化，结果文件有点大

```
cd pangolin-tool
# 通过spring-boot-maven-plugin生成一个独立的可执行文件 pangolin-tool-final.jar
mvn clean package -U -D maven.test.skip=true

# 将pangolin-tool-final.jar打包成 PangolinToolkit.exe
jpackage --type app-image  -n PangolinToolkit -i ./target --main-jar pangolin-tool-final.jar --icon "./src/main/resources/icon/pangolin_32.ico" --java-options "-Xms100m -Xmx500m"

# 将pangolin-tool-final.jar打包成安装包
jpackage --name PangolinToolkit --input ./target --main-jar pangolin-tool-final.jar --icon "./src/main/resources/icon/pangolin_32.ico" --win-menu --win-shortcut --win-dir-chooser --app-version 1.0.0 --copyright "Copyright @2024 PangolinToolkit" --vendor "Pangolin Java Toolkit" --description "一个JavaFX开发的工具"
```