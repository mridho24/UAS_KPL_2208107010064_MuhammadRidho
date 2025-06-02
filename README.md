<p align="center">
  <img src="image\WhatsApp_Image_2025-04-13_at_20.44.43_5af0ef37-removebg-preview 2.png" alt="Your App Logo" width="200"/>
</p>

# Mobile App Testing Automation with Appium

## 📱 Overview

This project contains automated test scripts for mobile application testing using Appium. The test suite is built with Java and implements a robust framework for testing Android applications.

## 🛠 Tech Stack

- **Java** (JDK 16)
- **Appium** (v7.5.1) - Mobile app automation framework
- **Selenium WebDriver** (v3.141.59) - Web browser automation
- **TestNG** (v7.5) - Test execution framework
- **Maven** - Dependency management
- **WebDriverManager** (v5.8.0) - Automated driver management
- **SLF4J** (v2.0.13) - Logging framework

## 📋 Prerequisites

Before running the tests, ensure you have the following installed:

- Java JDK 16 or higher
- Maven
- Android SDK
- Appium Server
- Android Emulator or physical device
- Node.js (for Appium)

## 🔧 Setup Instructions

1. Clone this repository:

   ```bash
   git clone [your-repository-url]
   ```

2. Install dependencies:

   ```bash
   mvn clean install
   ```

3. Start the Appium server:

   ```bash
   appium
   ```

4. Connect an Android device or start an emulator

## 📱 Supported Applications

The test suite currently supports the following APKs:

- RentSmart Debug APK
- API Demos Debug APK
- App Debug APK

## 🚀 Running Tests

To run the test suite:

```bash
mvn test
```

To run specific test classes:

```bash
mvn test -Dtest=TestClassName
```

## 📁 Project Structure

ApppiumTest/
├── src/
│ ├── appiumtest/ # Test cases and page objects
│ ├── rentsmart-debug.apk # RentSmart application APK
│ ├── app-debug.apk # Debug APK
│ └── ApiDemos-debug.apk # API Demos application APK
├── pom.xml # Maven configuration
└── README.md # Project documentation

## 🔍 Test Features

- Mobile element interaction
- Gesture handling (swipe, tap)
- Form filling and validation
- Navigation testing
- API integration testing
- Cross-device testing capabilities

## 📝 Logging

The project uses SLF4J for logging test execution details and debugging information. Logs are output to the console for easy monitoring of test execution.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
