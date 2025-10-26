# 🤖 Conference Bot

Telegram-бот для управления научными семинарами и конференциями. Позволяет создавать мероприятия, регистрировать участников, отправлять уведомления и управлять расписанием.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-ready-blue.svg)](https://www.docker.com/)

---

## 📋 Содержание

- [Особенности](#-особенности)
- [Технологии](#-технологии)
- [Функциональность](#-функциональность)
- [Установка и запуск](#-установка-и-запуск)
- [Конфигурация](#-конфигурация)
- [Команды бота](#-команды-бота)

---

## ✨ Особенности

- 📅 **Создание и управление семинарами** с категоризацией (Математика, Физика, Другое)
- 👥 **Регистрация участников** с автоматическим управлением списками
- 🔔 **Система уведомлений** с настраиваемым временем 
- 🔐 **Система авторизации** через токены с поддержкой ролей (User, Admin)
- ⚡ **Redis кэширование** для оптимизации производительности
- 🐳 **Docker Compose** для простого развертывания
- 🔄 **Автоматическая очистка** просроченных семинаров
---

## 🛠 Технологии

### Backend
- **Java 17** - язык программирования
- **Spring Boot 3.0.6** - фреймворк для создания приложений
- **Spring Data JPA** - работа с базой данных
- **Hibernate** - ORM для маппинга объектов

### Database
- **PostgreSQL 14** - основная база данных
- **Redis 7** - кэширование данных

### Интеграции
- **Telegram Bots API 6.5.0** - взаимодействие с Telegram
- **Spring Scheduler** - фоновые задачи и уведомления

### Build & Deploy
- **Gradle** - система сборки
- **Docker & Docker Compose** - контейнеризация
- **Lombok** - уменьшение boilerplate кода

### Testing
- **JUnit 5** - unit testing
- **Mockito** - моки и стабы
- **AssertJ** - fluent assertions
---

## 🎯 Функциональность

### Для пользователей

- ✅ Регистрация и авторизация через токены
- ✅ Просмотр доступных семинаров по категориям
- ✅ Присоединение к семинарам
- ✅ Настройка персональных уведомлений
- ✅ Просмотр информации о семинарах
- ✅ Управление своим профилем

### Для организаторов

- ✅ Создание новых семинаров
- ✅ Редактирование информации о семинарах
- ✅ Просмотр списка участников
- ✅ Отправка уведомлений всем участникам
- ✅ Удаление семинаров

### Для администраторов

- ✅ Генерация токенов доступа
- ✅ Управление пользователями
- ✅ Полный доступ к функционалу

---

## 🚀 Установка и запуск

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/BrightGir/Conference-Bot.git
```

2. **Создайте файл `.env`**
```bash
cp .env.example .env
```

3. **Отредактируйте `.env` файл**
```properties
# Telegram Bot
BOT_NAME=your_bot_name
BOT_KEY=your_bot_token_from_botfather
ADMIN_TOKEN=your_secure_admin_token

# Database
DB_NAME=conference_db
DB_USER=postgres
DB_PASSWORD=strong_password_here
DB_PORT=5432
DB_EXTERNAL_PORT=5432
DB_URL=jdbc:postgresql://db:5432/conference_db
DB_DRIVER_CLASS_NAME=org.postgresql.Driver

# Hibernate
HIBERNATE_DDL_AUTO=update
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
DEBUG_SQL=false

# Redis
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_EXTERNAL_PORT=6379
```

4. **Соберите JAR-файл**
```bash
./gradlew clean bootJar
```

5. **Запустите проект**
```bash
docker-compose up -d --build
```

6. **Проверьте статус**
```bash
docker-compose ps
```

## ⚙️ Конфигурация

### Переменные окружения

| Переменная | Описание | Пример |
|------------|----------|--------|
| `BOT_NAME` | Имя бота в Telegram | `@MyConferenceBot` |
| `BOT_KEY` | Токен от BotFather | `123456:ABC-DEF...` |
| `ADMIN_TOKEN` | Токен для админ-доступа | `secure_token_123` |
| `DB_URL` | URL подключения к БД | `jdbc:postgresql://db:5432/conference_db` |
| `DB_USER` | Пользователь БД | `postgres` |
| `DB_PASSWORD` | Пароль БД | `your_password` |
| `REDIS_HOST` | Хост Redis | `redis` |
| `REDIS_PORT` | Порт Redis | `6379` |

---

## 📱 Команды бота

### Основные команды

| Команда | Описание |
|---------|----------|
| `/start` | Начало работы с ботом |
| `🔑 Авторизация` | Ввод токена доступа |
| `📄 Информация` | Информация о профиле |
| `⚗️ Семинары` | Просмотр доступных семинаров |
| `🔔 Текущие семинары` | Семинары, в которых вы участвуете |
| `📅 Присоединиться к семинару` | Регистрация на семинар по ID |
| `🧮 Создать семинар` | Создание нового семинара |
| `🧱 Управление семинарами` | Редактирование своих семинаров |

### Админ команды

| Команда | Описание |
|---------|----------|
| `💫 Панель администратора` | Административная панель |
| `/generatetoken` | Генерация токена доступа |

---

## 📝 Лицензия

MIT License - см. файл [LICENSE](LICENSE.md)


