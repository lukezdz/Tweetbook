#Tweetbook
Project is a demonstration of Jakarta EE technology.

[![Java v11][shield-java]](https://adoptopenjdk.net/)
[![Jakarta EE v8][shield-jakarta]](https://jakarta.ee/specifications/platform/8/)

##Building
In order to build, use:
```bash
mvn clean package
```

##Running
In order to run, use:
```bash
mvn -P liberty liberty:dev
```

The project can be accessed on `http://localhost:9080/Tweetbook`