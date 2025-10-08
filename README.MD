# ❤️ Heart Manager (Spigot Plugin)

Heart Manager is a Spigot plugin that dynamically adjusts player health based on gameplay conditions. It includes custom crafting recipes, heart withdrawal, revival system, and full configuration support.

## ⚙️ Features

- **Custom heart and revive items** with configurable recipes
- **Automatic ban and revive system** on player death
- **Configurable messages and commands** for full customization
- **GUI for reviving dead players**
- **"Gamble" mechanic** to risk or gain hearts

## 🧩 Configuration

All settings, messages, recipes, and max heart limits are stored in `config.yml`. The plugin loads and saves the configuration automatically.

## 🛠️ Commands

| Command      | Description                          |
|--------------|--------------------------------------|
| `/hearts`    | Manage heart-related actions         |
| `/withdraw`  | Withdraw a heart item               |

*Custom commands can be defined in the config file.*

## 📦 Installation

1. Drop the `.jar` file into your server’s `plugins` folder.
2. Start the server to generate the `config.yml` file.
3. Edit `config.yml` to customize settings as desired.
4. Reload or restart the server to apply changes.

## 🔧 Requirements

- **Spigot / Paper**: Version 1.20 or higher
- **Java**: Version 17 or higher