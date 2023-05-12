## [REST API](http://localhost:8080/doc)

## Концепция:
- Spring Modulith
  - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
  - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
  - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```
- Есть 2 общие таблицы, на которых не fk
  - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
  - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем проверять

## Аналоги
- https://java-source.net/open-source/issue-trackers

## Тестирование
- https://habr.com/ru/articles/259055/

## Заметки разработчика

### Запуск приложения
1. Должны быть установлены следующие переменные окружения:
- JR_DB_USERNAME - имя пользователя для доступа к БД
- JR_DB_PASSWORD - пароль пользователя для доступа к БД
- JR_GITHUB_CLIENT_ID - идентификатор клиента для GitHub приложения
- JR_GITHUB_CLIENT_SECRET - секрет клиента для GitHub приложения
- JR_GOOGLE_CLIENT_ID - идентификатор клиента для Google проекта 
- JR_GOOGLE_CLIENT_SECRET - секрет клиента для Google проекта
- JR_GITLAB_CLIENT_ID - идентификатор клиента для GitLab приложения
- JR_GITLAB_CLIENT_SECRET - секрет клиента для GitLab приложения
- JR_MAIL_USERNAME - имя пользователя для доступа к почтовому smtp серверу
- JR_MAIL_PASSWORD - пароль пользователя для доступа к почтовому smtp серверу
- JR_MAIL_HOST - адрес для доступа к почтовому smtp серверу
- JR_MAIL_PORT - порт для доступа к почтовому smtp серверу

### Помощь в выполнении задач
[Заметки](doc/dev-notes.md)

### Запуск Docker-контейнера
1) Создать образ (Docker image):
>docker build -t ru.javarush/jira .

2) Запустить контейнер (Docker container):
>docker run --name javarush-jira -p 8080:8080 --env-file docker-env.list ru.javarush/jira

### Список выполненных задач:
- [x] Задача 1. Разобраться со структурой проекта (onboarding).
- [x] Задача 2. Удалить социальные сети: vk, yandex.
- [x] Задача 3. Вынести чувствительную информацию (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки почты) в отдельный проперти файл. Значения этих проперти должны считываться при старте сервера из переменных окружения машины.
- [x] Задача 4. Переделать тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL. Для этого нужно определить 2 бина, и выборка какой из них использовать должно определяться активным профилем Spring.
- [x] Задача 5. Написать тесты для всех публичных методов контроллера ProfileRestController
- [x] Задача 6. Добавить новый функционал: добавления тегов к задаче. Фронт делать необязательно. [Сделано без фронта]
- [x] Задача 8. Добавить автоматический подсчет времени сколько задача находилась в работе и тестировании. Написать 2 метода на уровни сервиса, который параметром принимает задачу и возвращают затраченное время:
   Сколько задача находилась в работе (ready минус in progress ).
   Сколько задача находилась на тестирование (done минус ready).
- [x] Задача 9. Написать Dockerfile для основного сервера
   

