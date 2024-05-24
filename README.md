# SpringHttpLoggerStarter


Spring Boot Starter для логирования HTTP-запросов.
Позволяет логировать различные уровни запросов (INFO, DEBUG, ERROR)
с детализацией до метода запроса, URI и заголовков, 
а также учитывает длительность выполнения метода в секундах. 

Добавление стартера в проект:
* Для начала необходимо добавить стартер в локальный репозиторий Maven
, для этого удобно использовать плагин Maven в IDEA,
при помощи которого мы можем опубликовать наш стартер:
``` mvn install```
* Далее, в основном проекте, необходимо явно указать зависимость в вашем ```pom.xml``` файле:
````      <dependency>
  <groupId>com.schoolt1</groupId>
  <artifactId>SpringHttpLoggerStarter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  </dependency>
````
* Добавьте параметры логирования в ```application.yml``` своего основного приложения, 
аналогично примеру;
```http:
  logging:
    enabled: true
    level: error
```
В данном случае логгирование запросов запущено по умолчанию, а текущий уровень логирования - ```error```
На данный момент стартер также поддерживает уровни логгирования - ```info, debug```

**Пример успешной работы стартера (лог основного приложения) :**

```2024-05-24T11:49:16.173+03:00 ERROR 551371 --- [nio-8080-exec-3] c.s.s.utils.LoggerUtils : Исходящий ответ: status={200}, headers=[Content-Type, Content-Length, Date, Keep-Alive, Connection], duration={3}ms ```
```4T11:49:16.171+03:00 ERROR 551371 --- [nio-8080-exec-3] c.s.s.utils.LoggerUtils : Входящий запрос: method=GET, uri=/api/getTestExample, headers=[host, connection, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform, upgrade-insecure-requests, user-agent, accept, sec-fetch-site, sec-fetch-mode, sec-fetch-user, sec-fetch-dest, accept-encoding, accept-language]```


