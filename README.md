# Bajaj Finserv Health - API Qualifier Assessment

## 📋 Project Overview
This Spring Boot application is developed for the **Bajaj Finserv Health API Qualifier Round**.  
The application automatically executes a complete workflow on startup that includes:

1. **Generate Webhook** → Sends a POST request to generate a webhook URL and access token  
2. **Solve SQL Problem** → Determines the question based on registration number and provides the SQL solution  
3. **Submit Solution** → Sends the final SQL query to the webhook URL using JWT authentication  

---

## 🔧 Technical Stack
- **Framework**: Spring Boot 3.0.6  
- **Language**: Java 17  
- **Build Tool**: Maven  
- **Dependencies**:  
  - Spring Web  
  - Spring Boot DevTools  

---

## 🛠️ Key Features
- ✅ **No Controller Dependencies** → Entire flow executes automatically on startup  
- ✅ **JWT Authentication** → Uses access token for secure API communication  
- ✅ **Dynamic Question Selection** → Automatically determines question based on registration number  
- ✅ **Error Handling** → Comprehensive error handling with detailed logging  
- ✅ **RESTful Communication** → Uses `RestTemplate` for HTTP client operations  
