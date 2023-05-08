Заметки, помогшие выполнить задачи.

----

### Задача 3
Вынести чувствительную информацию (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки почты) в отдельный проперти файл. Значения этих проперти должны считываться при старте сервера из переменных окружения машины.

[https://www.baeldung.com/properties-with-spring](Properties with Spring and Spring Boot) -> 4.7. Importing Additional Configuration Files

[https://www.baeldung.com/spring-boot-properties-env-variables](Using Environment Variables in Spring Boot’s Properties Files) -> 2. Use Environment Variables in the application.properties File

Последствия выполнения задачи:

Для запуска локально в IntelliJ IDEA необходимо добавить переменные окружения.
Run/Debug Configuration -> Environment variables:
```
JR_DB_URL=jdbc:postgresql://localhost:5432/jira;JR_DB_PASSWORD=JiraRush;JR_DB_USERNAME=jira;JR_GITHUB_CLIENT_ID=3d0d8738e65881fff266;JR_GITHUB_CLIENT_SECRET=0f97031ce6178b7dfb67a6af587f37e222a16120;JR_GITLAB_CLIENT_ID=b8520a3266089063c0d8261cce36971defa513f5ffd9f9b7a3d16728fc83a494;JR_GITLAB_CLIENT_SECRET=e72c65320cf9d6495984a37b0f9cc03ec46be0bb6f071feaebbfe75168117004;JR_GOOGLE_CLIENT_ID=329113642700-f8if6pu68j2repq3ef6umd5jgiliup60.apps.googleusercontent.com;JR_GOOGLE_CLIENT_SECRET=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;JR_MAIL_PASSWORD=zdfzsrqvgimldzyj;JR_MAIL_USERNAME=jira4jr@gmail.com;JR_MAIL_HOST=smtp.gmail.com;JR_MAIL_PORT=587
```
----