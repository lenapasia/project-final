Заметки для помощи в выполнеии задач.

----

### Задача 3
_Вынести чувствительную информацию (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки почты) в отдельный проперти файл. Значения этих проперти должны считываться при старте сервера из переменных окружения машины._

[Properties with Spring and Spring Boot](https://www.baeldung.com/properties-with-spring) -> 4.7. Importing Additional Configuration Files

[Using Environment Variables in Spring Boot’s Properties Files](https://www.baeldung.com/spring-boot-properties-env-variables) -> 2. Use Environment Variables in the application.properties File

**Последствия выполнения задачи:**<br>
Для запуска локально в IntelliJ IDEA необходимо добавить переменные окружения.
Run/Debug Configuration -> Environment variables:
```
JR_DB_PASSWORD=JiraRush;JR_DB_USERNAME=jira;JR_GITHUB_CLIENT_ID=3d0d8738e65881fff266;JR_GITHUB_CLIENT_SECRET=0f97031ce6178b7dfb67a6af587f37e222a16120;JR_GITLAB_CLIENT_ID=b8520a3266089063c0d8261cce36971defa513f5ffd9f9b7a3d16728fc83a494;JR_GITLAB_CLIENT_SECRET=e72c65320cf9d6495984a37b0f9cc03ec46be0bb6f071feaebbfe75168117004;JR_GOOGLE_CLIENT_ID=329113642700-f8if6pu68j2repq3ef6umd5jgiliup60.apps.googleusercontent.com;JR_GOOGLE_CLIENT_SECRET=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;JR_MAIL_PASSWORD=zdfzsrqvgimldzyj;JR_MAIL_USERNAME=jira4jr@gmail.com;JR_MAIL_HOST=smtp.gmail.com;JR_MAIL_PORT=587
```
----

### Задача 4

_Переделать тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL. Для этого нужно определить 2 бина, и выборка какой из них использовать должно определяться активным профилем Spring._

* Создание и конфигурация соответствующих bean'ов:<br> 
[Configuring Separate Spring DataSource for Tests](https://www.baeldung.com/spring-testing-separate-data-source) -> 5. Using Spring Profiles
<br><br>
Дополнительные материалы (необзательно к прочтению):<br>
[Configuring a DataSource Programmatically in Spring Boot](https://www.baeldung.com/spring-boot-configure-data-source-programmatic) -> 3. Configuring a DataSource Programmatically
<br>
[Profiles in Spring to register beans conditionally](https://jstobigdata.com/spring/profiles-in-spring-to-register-beans-conditionally/)
<br><br>

* Существующие changelog.sql и test.sql имеют ошибики при работе с H2 database, для их исправления необходимо:
1) изолировать в кавычки зарезервированные служебные слова
2) исправить синтаксис сброса значения столбца с автоинкрементом:
```
ALTER TABLE %s ALTER COLUMN id RESTART WITH 1
```

**Последствия выполнения задачи:**<br>
Необходимо вручную пересоздать существуюущую базу данных в PostgreSQL (удалить базу данных "jira" и затем создать её пустой). Т.к. изменения (вынужденные исправления) в уже существующем функционале changelog.sql приведут к изменению контрольных сумм (checksum) уже примененых к базе данных liquibase-изменений (changeset), что в свою очередь вызовет ошибку на старте spring boot приложения.
```
DROP DATABASE IF EXISTS jira;
CREATE DATABASE jira;
```
[How to delete a database in pgadmin](https://stackoverflow.com/a/64889251)
<br>Альтернативный вариант решения проблемы: вручную обновить в базе данных checksum'ы "поломашвихся" changeset'ов (таблица "databasechangelog"). Тогда базу данных пересоздавать будет не нужно и все данные в ней сохранятся.

---

### Задача 7

_Добавить возможность подписываться на задачи, которые не назначены на текущего пользователя. (Рассылку уведомлений/письма о смене статуса задачи делать не нужно). Фронт делать необязательно._

- Как определить на кого назначена задача?  
  В таблице user_belong (entity UserBelong) есть связь между задачей и пользователем. Запись в этой таблице с соответствующими значениями в колонках object_id, object_type, user_id означает, что задача назначена на пользователя.

- Как можно подписаться на задачу?  
  Создать отдельную таблицу task_subscription. В эту таблицу сохранять подписку пользователя на задачу. Отношения в таблице many-to-many: users to tasks.

[JPA Many to Many example with Hibernate in Spring Boot](https://www.bezkoder.com/jpa-many-to-many/)

---

### Задача 8

_Добавить автоматический подсчет времени сколько задача находилась в работе и тестировании._

[Period and Duration in Java](https://www.baeldung.com/java-period-duration)

---

### Задача 9

_Написать Dockerfile для основного сервера_

Порядок действий для выполнения задачи

##### 1. Локально запустить jar-файл
Проверить, что jar файл нашего Spring-приложения локально запускается и работает без ошибок:  
а) установить переменные окружения, необходимые для работы нашего Spring-приложения  
б) запустить jar нашего Spring-приложения с активным профилем "prod"  
Если всё работает без ошибок, то далее настраиваем Dockerfile.

##### 2. Написать Dockerfile:
В рамках этой задачи, только создаем Dockerfile, но не запускаем его (запуск будем делать ниже).

1) Создание и запуск Dockerfile для Spring Boot приложения:
[Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker/)

2) При запуске docker образра необходимо указать пременные окружения, для этого можно использовать несколько подходов:
- передать в качестве параметров команды run: [Set environment variables (-e, --env, --env-file)](https://docs.docker.com/engine/reference/commandline/run/#env)
- или задать переменные окружения непосредственно в Dockerfile'е: [Docker совет №21: Использование переменных окружения](https://ealebed.github.io/posts/2018/docker-совет-21-использование-переменных-окружения/)

3) Установить активный профиль Spring приложению в Docker'е: [Starting Spring Boot Application in Docker With Profile](https://www.baeldung.com/spring-boot-docker-start-with-profile)

##### 3. Измененить адрес подключения к базе данных

Для того, чтобы запустить приложение в Docker'е необходимо изменить настройки подключения к базе данных. Потому что из Docker-образа по адресу "localhost" база будет не доступна. Поэтому надо найти по какому адресу будет доступна база данных Spring-приложению, запущенному в Docker'е.

Находим адрес для подключения к БД:
[How to connect locally hosted MySQL database with the docker container](https://stackoverflow.com/a/44544841)

Заменяем адрес подключения к базе данных в application.yaml и пересобираем jar.

##### 4. Запустить Docker-контейнер

1) Создаем образ (docker image):
>docker build -t ru.javarush/jira .

2) Запускаем контейнер на основе образа (создаем и запускаем docker container на основе docker image):
>docker run --name javarush-jira -p 8080:8080 --env-file docker-env.list ru.javarush/jira

_Примечание_  
После запуска приложения в Docker-контейнере вы можете столкнутся (но не обязательно) с ошибкой: само приложение запускается без ошибок, но при любом обращении к frontend'у возникают ошибки. Это связано с тем, что не до конца всё настроено (а вот что именно - предлагаю разобраться самостоятельно; подсказка - обратите внимание на файловую структуру нашего Spring-приложения).
