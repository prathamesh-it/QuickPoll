# QuickPoll — Full Stack Polling Application

A full stack polling application where users can create polls, vote, comment and like with JWT-based authentication and role-based access control.

---

## 🚀 Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- Spring Security
- JWT (JSON Web Token)
- JPA / Hibernate
- PostgreSQL
- JavaMailSender

**Frontend**
- React.js (Vite)
- Material UI (MUI)
- Axios
- React Router DOM
- Notistack
- js-cookie
- jwt-decode

---

## ✨ Features

- User Signup & Login with JWT authentication
- Create polls with multiple options and expiry date
- Vote on polls (duplicate vote prevention)
- Like polls
- Comment on polls
- View all polls sorted by newest first
- View only your own polls
- Email confirmation on poll creation
- Personalized feed (voted/liked status per user)
- Protected routes with role-based access control

---

## 🏗️ Project Architecture

```
Frontend (React - localhost:5173)
        ↕ HTTP + JWT Token
Backend (Spring Boot - localhost:8080)
        ↕ JPA/Hibernate
Database (PostgreSQL)
```

---

## 📁 Project Structure

```
QuickPoll/
├── backend/                         ← Spring Boot
│   ├── configs/
│   │   ├── JWTAuthenticationFilter
│   │   └── WebSecurityConfiguration
│   ├── controllers/
│   │   ├── auth/AuthController
│   │   └── users/PollController
│   ├── dtos/
│   ├── entity/
│   ├── enums/
│   ├── repositories/
│   ├── services/
│   └── utils/JWTUtil
│
└── frontend/                        ← React
    ├── environment/axiosInstance
    ├── services/
    │   ├── auth/Auth.js
    │   └── user/Poll.js
    ├── utility/Common.js
    └── pages/
        ├── Login.jsx
        ├── Signup.jsx
        ├── Dashboard.jsx
        ├── CreatePoll.jsx
        ├── ViewMyPolls.jsx
        └── ViewPollDetails.jsx
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL

---

### Backend Setup

**1. Clone the repository**
```bash
git clone https://github.com/your-username/quickpoll.git
cd quickpoll/backend
```

**2. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quickpoll
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**3. Create PostgreSQL database**
```sql
CREATE DATABASE quickpoll;
```

**4. Run the backend**
```bash
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8080`

---

### Frontend Setup

**1. Navigate to frontend**
```bash
cd quickpoll/frontend
```

**2. Install dependencies**
```bash
npm install
```

**3. Run the frontend**
```bash
npm run dev
```
Frontend runs on `http://localhost:5173`

---

## 🔐 API Endpoints

### Auth (Public)
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/signup` | Register new user |
| POST | `/api/auth/login` | Login user |

### Poll (Protected — requires JWT)
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/user/poll` | Create new poll |
| GET | `/api/user/polls` | Get all polls |
| GET | `/api/user/my-polls` | Get logged-in user's polls |
| DELETE | `/api/user/poll/{id}` | Delete poll |
| GET | `/api/user/poll/{id}` | Get poll details |
| GET | `/api/user/poll/like/{id}` | Like a poll |
| POST | `/api/user/poll/comment` | Post comment |
| POST | `/api/user/poll/vote` | Cast vote |

---

## 🔑 JWT Authentication Flow

```
User logs in → Backend generates JWT token
                        ↓
             Frontend saves token in cookie
                        ↓
         Every request → Axios interceptor adds
         Authorization: Bearer <token> to header
                        ↓
         Backend JWTAuthenticationFilter validates
         token on every protected request
```

---

## 🛡️ Security

- Passwords hashed using **BCrypt**
- JWT tokens expire after **24 hours**
- All `/api/user/**` routes require valid JWT
- `/api/auth/**` routes are public
- CSRF disabled (JWT based app)
- CORS configured for `localhost:5173`

---

## 📧 Email Feature

After successfully creating a poll, QuickPoll automatically sends a confirmation email to the user with the poll question and posted date.

---

## 🤝 Contributing

Pull requests are welcome. For major changes please open an issue first.

---

## Author
Prathamesh Nistane
