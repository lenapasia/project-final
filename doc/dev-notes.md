Заметки, помогшие выполнить задачи.

----

### Задача 3
_Вынести чувствительную информацию (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки почты) в отдельный проперти файл. Значения этих проперти должны считываться при старте сервера из переменных окружения машины._

[https://www.baeldung.com/properties-with-spring](Properties with Spring and Spring Boot) -> 4.7. Importing Additional Configuration Files

[https://www.baeldung.com/spring-boot-properties-env-variables](Using Environment Variables in Spring Boot’s Properties Files) -> 2. Use Environment Variables in the application.properties File

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
<br>Альтернативный вариант решения проблемы: вручную обновить в базе данных checksum'ы "поломашвихся" changeset'ов. Тогда базу данных пересоздавать будет не нужно и все данные в ней сохранятся.

---

### Задача 8

_Добавить автоматический подсчет времени сколько задача находилась в работе и тестировании._

[Period and Duration in Java](https://www.baeldung.com/java-period-duration)