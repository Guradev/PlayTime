# ⏱️ PlayTime Plugin

[![Java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://adoptium.net)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![PAPI Support](https://img.shields.io/badge/PlaceholderAPI-Supported-blue.svg)](https://www.spigotmc.org/resources/placeholderapi.6245/)

A lightweight and efficient Spigot **Minecraft plugin** to track player playtime with **PlaceholderAPI**, **MySQL**, **SQLite**, and more coming soon!

> ⚡ Built with performance and clean design in mind by **Gura1**.

---

## 📦 Features

- 🕒 Track player **playtime** in real-time
- 🧩 **PlaceholderAPI** support for dynamic, customizable placeholders
- 💾 **MySQL** and **SQLite** support
- 🧠 Async database saving & in-memory caching for max performance
- 🧰 Command system for admins
- 🌱 Designed for future extensibility (more database types, GUI, leaderboard, etc.)

---

## 🔧 Setup Guide
1. 📁 Drop the plugin `.jar` into your server’s `/plugins/` folder.
2. 🌀 Start your server to generate the config.
3. ✍️ Edit `config.yml` to choose your database (MySQL or SQLite).
4. 🔁 Restart your server.
5. 🧩 Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) if you want to use placeholders.

---

## ⚙️ Config Example (`config.yml`)

```yaml
database:
  type: MYSQL # Options: MYSQL or SQLITE

  mysql:
    host: localhost
    port: 3306
    database-name: playtime
    username: root
    password: example
```

## 💬 Commands
Here are the commands available along their required permission. OP's get every permission!
- /playtime get <player> View your own playtime or another player's playtime	playtime.view
- /playtime set/delete <player> <seconds>	Set a player’s total playtime in seconds or delete their playtime completely playtime.admin

🎯 The following commands for players are planned:
- /playtime top
- /playtime reset
- /playtime gui
- /playtime weekly/monthly

---

# 🧩 Placeholders (via PlaceholderAPI)

## 🔢 Short Unit Placeholders

| Placeholder          | Example Output |
|----------------------|----------------|
| `%playtime_seconds%` | `45s`          |
| `%playtime_minutes%` | `12m`          |
| `%playtime_hours%`   | `3h`           |
| `%playtime_days%`    | `0d`           |

---

## 📖 Full Text Placeholders

| Placeholder                  | Example Output |
|------------------------------|----------------|
| `%playtime_seconds_full%`    | `45 seconds`   |
| `%playtime_minutes_full%`    | `12 minutes`   |
| `%playtime_hours_full%`      | `3 hours`      |
| `%playtime_days_full%`       | `0 days`       |

---

## 🔁 Smart/Dynamic Format

| Placeholder         | Example Output                     |
|---------------------|-------------------------------------|
| `%playtime_dynamic%` | `2h 3m`, `5m 23s`, or `1d 2h` (auto adjusts) |
| `%playtime_default%` | Same as dynamic                   |

---

## 🛢️ Database Support

| Type     | Description                                                    |
|----------|----------------------------------------------------------------|
| ✅ MySQL | External, scalable database — best for large or networked servers |
| ✅ SQLite | File-based, zero setup — ideal for single-server setups       |
| 🔜 MongoDB | Coming soon!                                                  |

---

## 📜 MIT License

```text
MIT License

Copyright (c) 2025 Gura1

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
