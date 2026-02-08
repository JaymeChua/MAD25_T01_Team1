# MAD25_T01_Team1

## Disclaimer
This is a student assignment project for the Kotlin App
Development module at Ngee Ann Polytechnic. Developed for
educational purposes


# Ngee Ann Foodies: Smart Dining for NP
<img width="500" height="500" alt="Image" src="https://github.com/user-attachments/assets/11919a34-7dd4-4b68-9534-97409575a976" />

**Ngee Ann Foodies** is a Kotlin-based mobile application specifically designed to enhance the dining experience for students and staff at **Ngee Ann Polytechnic**. By providing a centralized, interactive platform, the app addresses the common "lunchtime dilemma" through transparent menus and a community-driven review system.

---

## 🚀 Deployment Status
The application is currently in **Stage 2 of development**.
* **Stage 1 (Completed):** Focused on core navigation, local data persistence using Room, and basic directory functionality.
* **Stage 2 (In Progress):** Shifting toward cloud integration with Firebase, advanced user management, and AI-powered automation.

---

## 👥 Team Members
| Name | Student ID |
| :--- | :--- |
| Lucas | S10255784A |
| Jayme | S10257237E |
| Kaijie | S10262480B |
| Ethan | S10258606A |

---

## 🌟 Motivation / Objective
In the current campus environment, students and staff face several dining **pain points**:
* **The "Information Black Box"**: Students have no way of knowing what food is available or the cost without physically traveling to the canteen.
* **Wasted Break Time**: Users cannot gauge crowd levels remotely, often leading to 15-minute wait times that cut into actual eating time.
* **Budget Frustrations**: Students with strict daily allowances are forced to "do laps" around canteens to find meals within their budget.
* **Inconsistent Quality**: Without a centralized review system, users are often uncertain about trying new stalls unless they are personally recommended.

---

## 📚 App Category
**Primary Category: Food & Drink**
This application focuses on interactive campus dining discovery and meal planning. It combines utility with social elements to help users explore their campus food options efficiently.

---

## 👥 Roles & Contributions

### **Stage 1: Foundation & Local Architecture**
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

### **Stage 2: Advanced Features & Cloud Migration**
### **Lucas**
* **Admin Management System:** Implementing a full CRUD (Create, Read, Update, Delete) system for managing users, stalls, and dishes.

* **AI Menu Scanner**: Developing an automated scanner for dishes that auto-generates names and prices while auto-cropping images from menu photos.

### **Jayme**
* **Profile Personalization:** Updating the Profile Page to incorporate more personalized user data and preferences.

### **Kaijie**
* **Cloud Migration:** Transitioning the existing local Room database architecture to Firebase to enable real-time cloud synchronization across devices.

### **Ethan**
* **Account Recovery:** Implementing the "Forget Password" function to provide a secure and user-friendly account recovery process.

---

## 👤 User Personas

### Sarah, The Social Explorer
* **Demographics**: Year 1 Mass Comm Student, 18 years old.
* **Goal**: Find "hidden gems" and warn others about stalls with dropped quality.
* **Pain Point**: Analysis paralysis; overwhelmed by too many choices at Makan Place.
* **Quote**: *"Terrified of eating normal food while everyone else is enjoying a hidden gem."*

### Ken, The Rush-Hour Senior
* **Demographics**: Year 3 Engineering Student, 21 years old.
* **Goal**: Quickly identify a "safe" 3+ star option in the nearest canteen.
* **Pain Point**: Hates walking to a stall only to find a long queue and decreased quality.
* **Quote**: *"I don't want lunch to be another problem to solve."*

---
## Design and WireFrame

<img width="1388" height="1237" alt="Image" src="https://github.com/user-attachments/assets/987b1811-2e82-4b01-b830-4e25e8dcb3de" />

To view the live, interactive wireframes and design annotations, use the link below:

View Full Wireframes on Figma:
https://www.figma.com/design/iKOGducQkBP7NfNJxZTNxi/Np-Foodie?node-id=0-1&p=f&t=StMKZKBsGxdDnQXT-0
---

## 🔄 User Flows


### Flow 1: Leaving a Review (Happy Path)
1. **Navigate**: Sarah navigates to the canteen tab.
2. **Select**: Chooses 'Munch' and then selects the 'Korean' stall.
3. **Action**: Clicks 'Add Review' and fills out the star rating and feedback.
4. **Result**: System updates the database and the review is rendered in the UI.



### Flow 2: Quick Menu Access (Alternate Flow)
1. **Direct Access**: Ken selects the "Favorite" tab from the navigation bar.
2. **Select**: He clicks on "Chicken Rice" from his saved list.
3. **Result**: System directs him straight to the specific stall's menu.

---

## 📊 Market Research & User Feedback
Primary research with **33 NP students** proves the necessity of this application:
* **81.8%** list "Don't know what is on the menu" as a top frustration.
  <img width="1310" height="570" alt="Image" src="https://github.com/user-attachments/assets/aa40b11c-a434-449d-93fa-b030cce71f5d" />
* **42.4%** have settled for food they didn't want just to avoid walking to another canteen.
  <img width="1308" height="564" alt="Image" src="https://github.com/user-attachments/assets/66065a22-16a4-4712-9f08-8906d8fd52d0" />
* **60.6%** stated they would definitely try a new stall if they saw a "4.5 Star" rating.
  <img width="406" height="361" alt="Image" src="https://github.com/user-attachments/assets/cb6513b0-0072-41f1-bb93-284518628bdd" />

---

## 🔍 Competitor Analysis
### 1.NUSmart Dining (Official App)

Features:
Pre-order & pre-pay
Queue-skipping / pick-up

Strengths:
Deep transactional integration: payment, wallet, and queue management.

Weakness:
Primary goal = order and pay fast, not explore the campus food scene.
Reviews, favourites, and discovery are minimal

### 2.NUS Hostel Dining

Features:
QR code meal redemption
Admin Integration

Strengths:
Strong alignment with university housing operations and financial rules.

Weakness:
Not useful for non-hostel students, visitors or staff who eat at other campus food courts.

### 3.Boonli (School Ops)

Features:
Online ordering
Payment processing

Strengths:
Very solid back office, reporting and cash flow features for schools.

Weakness:
Parent-Centric: Designed for admins and parents, not students
Overkill for a single poly/uni campus.

### 4.MealViewer (Digital Menus)

Features:
Real-time menus
Favourites & notifications

Strengths:
Strong alignment with university housing operations and financial rules.

Weakness:
It's just a digital menu board. No interactive reviews

## 🛠️ Installation & Setup (Android Studio)
1. **Clone the repository**: `git clone [repository-url]`
2. **Open in Android Studio**: Select `Open an Existing Project` and navigate to the directory.
3. **Sync Project**: Allow Gradle to sync and download dependencies.
4. **Get API Key**: Get the API key and set it in the local properties folder under GEMINI_API_KEY= .This is needed for AI menu scanner for admin.
5. **Run**: Select an emulator device(API 30+) and click the **Run** button.