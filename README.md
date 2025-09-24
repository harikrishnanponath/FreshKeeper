# 🥬 FreshKeeper

FreshKeeper is an Android app built with **Kotlin** and **Jetpack Compose** to help you manage groceries, track expiry dates, and reduce food waste.

## ✨ Features
- 🛒 Add, edit, and delete grocery items  
- 📅 Track expiry dates with color-coded indicators  
- 🔔 Notifications for upcoming expiry  
- 🏷️ Categorize groceries with quantity & units  
- 🎨 Modern UI with Material 3 & dark theme support  

## 🛠️ Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose, Material 3  
- **Architecture:** MVVM (ViewModel + StateFlow)  
- **Database:** Room  
- **Navigation:** Jetpack Navigation Compose  

## 📂 Project Structure
app/
├─ data/ # Room database, DAO, entities
├─ repository/ # Repository layer
├─ viewmodel/ # ViewModels
├─ view/ # Composables (UI)
└─ utils/ # Helpers, formatters, validators

## 🚀 Getting Started
1. Clone the repo:
   ```bash
   git clone https://github.com/harikrishnanponath/FreshKeeper.git
