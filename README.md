# jChat
Многофункциональный мессенджер

*Стартовый проект чата на Java для NetCracker*

Статус реализации функционала на текущий момент:

Функционал               | Статус             |
------------------------ | ------------------ |
Выбор режима приложения  | :white_check_mark: |
Выбор режима хранения    | :white_check_mark: |
Управление через консоль | :white_check_mark: |
Управление через API     | :x:                |
Виртуальная БД в памяти  | :white_check_mark: |
Файловая БД              | :white_check_mark: |
SQL                      | :x:                |
Логирование в консоль    | :white_check_mark: |
Логирование в файл       | :white_check_mark: |
Пользовательский клиент  | :x:                |

Предполагается что одно и тоже приложение способно работать в 2 режимах: консольном и серверном.
На начальном этапе приложение хранит всё в виртуальной базе данных, в памяти, однако, в дальнейшем предполагается изменить способ хранения с возможностью опционального выбора.

## Консольный режим

Процесс работы приложения и его поддержки команд:
![jchat demo](https://github.com/iLeonidze/jChat/blob/63e61da5229011eed201a9771526a82fa0b5c678/jChat%20Demo.gif)

Доступные методы для работы с приложением

Название        | Реализация         | Права доступа | Описание работы
--------------- | ------------------ | ------------- | ---
help            | :white_check_mark: |   | Выводит список доступных команд вроде этого
exit            | :white_check_mark: |   | Завершает приложение
user            |
user register   | :white_check_mark: |   | Регистрирует анонима
user login      | :white_check_mark: |   | Авторизует анонима
user logout     | :white_check_mark: | 0 | Деавторизует пользователя
user authed     | :white_check_mark: | 0 | Сообщает о текущей сессии авторизации
user info       | :white_check_mark: | 0 | Выводит информацию о текущем или указанном пользователе
*user remove*   | :heavy_exclamation_mark: | 2 | Удаляет пользователя
*user ban*      | :heavy_exclamation_mark: | 1+ | Блокирует пользователя
*user unban*    | :heavy_exclamation_mark: | 1+ | Разблокирует пользователя
user isbanned   | :white_check_mark: | 0 | Возвращает статус бана
*user op*       | :heavy_exclamation_mark: | 2 | Изменяет уровень доступа
user contacts   | :x:                | 0 | Выводит список всех знакомых
user chats      | :white_check_mark: | 0 | Выводит список чатов, в которых пользователь участвует
user edit       | :x:                | 0 | Редактирует информацию о пользователе
chats           |
chats create    | :white_check_mark: | 0+ | Создает новый чат
chats remove    | :white_check_mark: | 0+ | Удаляет указанный чат
chats info      | :white_check_mark: | 0+ | Выводит общую информацию о чате
chats rename    | :white_check_mark: | 0+ | Переименовывает чат
chats members   | :white_check_mark: | 0+ | Выводит список всех участников чата
chats invite    | :white_check_mark: | 0+ | Добавляет пользователя в чат
chats leave     | :white_check_mark: | 0+ | Выходит из состава участников или исключает участника из чата
chats messages  | :white_check_mark: | 0+ | Выводит n-е количество сообщений чата
message         |
message send    | :white_check_mark: | 0+ | Отправляет сообщение в чат
message info    | :white_check_mark: | 0+ | Выводит подробную информацию о сообщении
message forward | :white_check_mark: | 0+ | Пересылает сообщение из одного чата в другой
message delete  | :white_check_mark: | 0+ | Удаляет сообщение из чата
message edit    | :white_check_mark: | 0+ | Редактирует сообщение в чате
