# 🎮 Game Results Server

**HTTP-сервер на Java 11**, який дозволяє:
- зберігати результати користувачів по рівнях,
- отримувати топ-результати по користувачу чи рівню,
- працює **в памʼяті** (без БД),
- підтримує **багатопоточність**.

---

## 🧰 Вимоги

- Java 11+
- Maven 3.6.0+
- Bash

---

## ⚙️ Збірка та запуск

### 🔨 Збірка

Для Linux/macOS:
```bash
chmod +x build.sh
./build.sh
```

Для Windows:
```bat
build.bat
```

### ▶️ Запуск

Для Linux/macOS:
```bash
chmod +x run.sh
./run.sh
```

Для Windows:
```bat
run.bat
```

Сервер буде працювати на:  
🔗 `http://localhost:8080`

Щоб змінити порт, встановіть змінну середовища `PORT` перед запуском:
```bash
export PORT=8081
./run.sh
```

---

## 📡 API-ендпоінти

| Метод | URL                      | Опис |
|-------|---------------------------|------|
| `PUT` | `/api/setinfo`          | Зберегти результат (JSON: `{"user_id": int, "level_id": int, "result": int}`) |
| `GET` | `/api/userinfo/{userId}` | Отримати топ-20 результатів користувача (JSON-масив: `[{"userId": int, "levelId": int, "result": int}, ...]`) |
| `GET` | `/api/levelinfo/{levelId}` | Отримати топ-20 результатів рівня (JSON-масив: `[{"userId": int, "levelId": int, "result": int}, ...]`) |

---

## 📥 Приклади запитів

### ➕ Додати результат

```bash
curl -X PUT http://localhost:8080/api/setinfo \
-H "Content-Type: application/json" \
-d '{"user_id":1,"level_id":3,"result":15}'
```

**Відповідь**: HTTP 200 (при успіху) або 400 (при некоректних даних, наприклад, `user_id <= 0`).

### 📊 Отримати топ користувача

```bash
curl http://localhost:8080/api/userinfo/1
```

**Приклад відповіді**:
```json
[
    {"userId": 1, "levelId": 1, "result": 55},
    {"userId": 1, "levelId": 3, "result": 15},
    {"userId": 1, "levelId": 2, "result": 8}
]
```

### 📈 Отримати топ по рівню

```bash
curl http://localhost:8080/api/levelinfo/3
```

**Приклад відповіді**:
```json
[
    {"userId": 2, "levelId": 3, "result": 22},
    {"userId": 1, "levelId": 3, "result": 15}
]
```

---

## 📁 Файли проєкту

- `LeaderboardApplication.java` — точка входу (`main`).
- `LeaderboardController.java` — REST-контролер для обробки HTTP-запитів.
- `GameResultsService.java` — бізнес-логіка для роботи з результатами.
- `InMemoryGameResultRepository.java` — зберігання результатів у пам’яті з потокобезпекою.
- `GameResult.java` — модель результату гри.
- `SetInfoRequest.java` — DTO для валідації вхідних даних PUT-запиту.
- `GlobalExceptionHandler.java` — глобальна обробка помилок через `@ControllerAdvice`.
- `GameResultsServiceTest.java` — модульні тести для сервісу.
- `LeaderboardControllerTest.java` — інтеграційні тести для контролера.
- `application.properties` — конфігурація Spring.
- `logback-spring.xml` — конфігурація логування.

---

## 🧠 Особливості

- Зберігає лише **топ-20 результатів** для кожного користувача/рівня.
- **Сортування**:
  - Для `userId`: за `result` (спадання), потім за `levelId` (зростання).
  - Для `levelId`: за `result` (спадання), потім за `userId` (зростання).
- **Валідація**: Вхідні дані перевіряються через `@Valid` (`user_id > 0`, `level_id > 0`, `result >= 0`).
- Дані зберігаються **в оперативній памʼяті**.
- **Thread-safe**: Використовуються `ConcurrentHashMap`, `TreeSet` і `ReentrantReadWriteLock`.

---

## ✅ Тестування

```bash
mvn test
```

---

## 📝 TODO

- [x] Підключити `@ControllerAdvice` для глобальної обробки помилок.
- [x] REST DTO-валидація через `@Valid`.
- [x] Додати тести для `InMemoryGameResultRepository` і `GameResultComparators`.
- [ ] Оптимізувати потокобезпеку з гранульованими замками.
- [ ] Додати Swagger / OpenAPI для документації API.
- [ ] Створити Docker-файл для контейнеризації.

---

## 👤 Автор

**Maxim Sheremet**  
🔗 [github.com/maksymSheremet](https://github.com/maksymSheremet)

---
