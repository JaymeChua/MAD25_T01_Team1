# MAD25_T01_Team1

##Disclaimer
This is a student assignment project for the Kotlin App
Development module at Ngee Ann Polytechnic. Developed for
educational purposes

## 1. Introduction
**Ngee Ann Foodies** is a native Android application designed specifically for the Ngee Ann Polytechnic community. It serves as a comprehensive digital guide to campus dining, allowing students and staff to browse canteens (such as Makan Place, Food Club, and Munch), view individual stall menus, and discover food options based on cuisine or popularity. Built entirely with **Kotlin** and **Jetpack Compose**, the app offers a modern, reactive user interface powered by a local **Room Database** for offline accessibility.

## 2. Motivation & Objective

### Motivation
With a large campus and multiple food courts, students often face "decision fatigue" during lunch hours. Many stick to the same few stalls simply because they are unaware of the variety available elsewhere on campus. Additionally, physical menu boards can only be seen when standing directly in front of a stall, making it difficult to browse options from a distance.

### Objective
The primary objective of this application is to digitize the campus dining experience.
* **For Users:** To streamline the decision-making process by providing a centralized platform where users can view stall ratings, cuisines, and "Popular Food" highlights at a glance.
* **For Development:** To demonstrate proficiency in Mobile Application Development (MAD) by implementing key Android architectural components, including Room persistence, Coroutines for asynchronous operations, and declarative UI design.

## 3. App Category
* **Primary Category:** Food & Drink
* **Secondary Category:** Education / Productivity

## 4. Declaration of Generative AI Usage

I declare that I used Generative AI tools to assist in the development of this assignment. The tool was used in the following specific capacities:

* **Debugging & Troubleshooting:** To identify and resolve layout issues in Jetpack Compose (e.g., fixing padding/whitespace in `Card` components) and to debug Room Database queries.
* **Documentation:** To assist in drafting and formatting sections of this README file (Introduction, Motivation, and App Categories).

## 5. Task(s) and feature(s) allocation of each member in the team for Stage 1

### **Lucas**
* **Assigned Role:** Review Page Implementation with Review Adding Features.
* **Additional Contributions:**
    * **Core Architecture:** Took initiative to implement the Room Data and Dao for Stall Entities, the central navigation logic linking all pages (Canteen → Stall → Menu → Review) and ensuring that favouriting a stall in stall page updates the favourite page and room database.
    * **UI Development:** Built the **Homepage** and **Menu Page** to ensure a complete user flow, connecting the work of other team members.   

### **Ethan**
* **Canteen Directory:** Developed the screen displaying the list of all available canteens (Makan Place, Food Club, etc.).
* **Stall Directory:** Implemented the feature to list specific stalls filtered by the selected canteen.

### **Kaijie**
* **Favourites Feature:** Developed the "Favourite Stalls" screen, allowing users to quickly access their preferred dining locations.

### **Jayme**
* **User Onboarding:** Designed and implemented the **Login Page**, handling the UI for user entry into the application.