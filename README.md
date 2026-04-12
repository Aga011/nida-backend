# Nida.edu - Microservices Backend

Azərbaycan şagirdlərinin universitetə qəbul imtahanına hazırlığı üçün hazırlanmış mikroservis arxitekturalı təhsil platforması.

## Texnologiyalar

- **Java 17** + **Spring Boot 4.0.5**
- **PostgreSQL** — hər servis üçün ayrıca database
- **Apache Kafka** — servislərarası event streaming
- **RabbitMQ** — bildiriş sistemi
- **JWT** — autentifikasiya
- **Spring Cloud Gateway** — API Gateway
- **Docker & Docker Compose** — bütün infrastruktur
- **ngrok** — local development üçün tunnel

## Servisler və Portlar

| Servis | Port | Database |
|--------|------|----------|
| ApiGateway | 8080 | — |
| UserService | 8081 | users_db |
| TestService | 8082 | test_db |
| StatisticsService | 8083 | statistics_db |
| LearningPlanService | 8084 | learning_db |
| NotificationService | 8085 | notification_db |
| PermissionService | 8086 | permission_db |
| PaymentService | 8087 | payment_db |

## Rollar

- **STUDENT** — şagird
- **TEACHER** — müəllim
- **PARENT** — valideyn
- **ADMIN** — admin

## Sistem Arxitekturası

Frontend → ApiGateway (8080) → Microservices
                                      ↓
                              Kafka / RabbitMQ

**Kafka axını:**
TestService → test-results topic → StatisticsService
                                 → LearningPlanService → RabbitMQ → NotificationService

## Tələblər

- Docker Desktop

## Qurulum

### 1. Layihəni klonla

`ash
git clone https://github.com/Aga011/nida-backend.git
cd nida-backend
`

### 2. .env faylı yarat

Aşağıdakı dəyişənləri özünə uyğun doldurun:

`env
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
DB_URL_USER=jdbc:postgresql://postgres:5432/users_db
DB_URL_TEST=jdbc:postgresql://postgres:5432/test_db
DB_URL_LEARNING=jdbc:postgresql://postgres:5432/learning_db
DB_URL_NOTIFICATION=jdbc:postgresql://postgres:5432/notification_db
DB_URL_STATISTICS=jdbc:postgresql://postgres:5432/statistics_db
DB_URL_PAYMENT=jdbc:postgresql://postgres:5432/payment_db
DB_URL_PERMISSION=jdbc:postgresql://postgres:5432/permission_db
JWT_SECRET=yoursecret
MAIL_USERNAME=youremail
MAIL_PASSWORD=yourpassword
KAFKA_URL=kafka:9092
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
USER_SERVICE_URL=http://user-service:8081
TEST_SERVICE_URL=http://test-service:8082
STATISTICS_SERVICE_URL=http://statistics-service:8083
LEARNING_SERVICE_URL=http://learning-service:8084
PAYMENT_SERVICE_URL=http://payment-service:8087
`

### 3. Hamısını bir əmrlə işə sal

`ash
docker-compose up --build
`

Bütün servisler, database-lər, Kafka və RabbitMQ avtomatik qalxır.

### 4. ngrok ilə tunnel aç

`ash
ngrok http 8080
`

Çıxan URL-i frontend-də API_BASE_URL kimi istifadə edin.

**Qeyd:** Frontend sorğularında bu header əlavə edin:
`javascript
headers: {
  "ngrok-skip-browser-warning": "true"
}
`

## Əsas API Endpointləri

### Auth
POST /api/auth/register        — Qeydiyyat
POST /api/auth/login           — Giriş
GET  /api/auth/verify          — Email təsdiq
POST /api/auth/forgot-password — Şifrə sıfırlama
POST /api/auth/reset-password  — Yeni şifrə

### User
GET  /api/user/profile      — Profil
PUT  /api/user/profile      — Profil yenilə
GET  /api/user/unique/{id}  — ID ilə axtarış

### Test
POST /api/test/assessment/start          — Qiymətləndirmə testi başlat
GET  /api/test/assessment/questions/{id} — Qiymətləndirmə sualları
POST /api/test/start                     — Mövzu testi başlat
GET  /api/test/questions/{sessionId}     — Sualları gətir
POST /api/test/submit/{sessionId}        — Cavab göndər
POST /api/test/finish/{sessionId}        — Testi bitir
GET  /api/test/spaced                    — Spaced repetition sualları

### Exam
POST /api/exam/create               — İmtahan yarat (TEACHER)
POST /api/exam/{id}/pay             — Ödəniş et (TEACHER)
POST /api/exam/{id}/activate        — Aktivləşdir (TEACHER)
POST /api/exam/{id}/start           — Başla (STUDENT)
POST /api/exam/session/{id}/finish  — Bitir (STUDENT)
POST /api/exam/session/{id}/comment — Şərh əlavə et (TEACHER)
GET  /api/exam/{id}/results         — Nəticələr (TEACHER)
GET  /api/exam/{id}/my-result       — Öz nəticəm (STUDENT)

### Statistics
GET /api/statistics/me                   — Öz statistikam
GET /api/statistics/student/{email}      — Şagird statistikası (TEACHER)
GET /api/statistics/child/{email}        — Uşaq statistikası (PARENT)
GET /api/statistics/parent/child/{email} — Gecikməli nəticə (PARENT)

### Learning Plan
POST /api/learning/create        — Plan yarat
GET  /api/learning/my?stage=...  — Planıma bax
PUT  /api/learning/complete/{id} — Mövzunu tamamla

### Notification
POST /api/notification/send       — Mesaj göndər (TEACHER)
GET  /api/notification/my         — Bildirişlərim
GET  /api/notification/unread     — Oxunmamışlar
PUT  /api/notification/read/{id}  — Oxundu işarələ
PUT  /api/notification/read/all   — Hamısını oxundu işarələ
GET  /api/notification/group/{id} — Qrup mesajları

### Permission
GET  /api/permission/search/{uniqueId} — Şagird axtarışı
POST /api/permission/teacher/send      — Fənn icazəsi (TEACHER)
POST /api/permission/parent/send       — Uşaq izlə (PARENT)
PUT  /api/permission/respond/{id}      — Cavab ver
PUT  /api/permission/revoke/{id}       — Ləğv et
GET  /api/permission/incoming          — Gələn sorğular
GET  /api/permission/sent              — Göndərilən sorğular
POST /api/permission/group/create      — Qrup yarat (TEACHER)
POST /api/permission/group/{id}/invite — Dəvət et
PUT  /api/permission/group/{id}/respond — Dəvəti cavabla
GET  /api/permission/group/my          — Qruplarım

### Payment
GET  /api/payment/balance      — Balans (PARENT/TEACHER)
POST /api/payment/topup        — Balans artır
POST /api/payment/pay          — Ödəniş et
POST /api/payment/pay/multiple — Çoxlu uşaq ödənişi
GET  /api/payment/history      — Ödəniş tarixi

## Autentifikasiya

Bütün endpointlər (auth xaricində) JWT token tələb edir:
Authorization: Bearer <token>

Token login/register zamanı qaytarılır.

## Qeydlər

- Şagird qeydiyyatdan sonra avtomatik olaraq assessment testi başlayır
- Email təsdiqləmə məcburidir — təsdiqsiz login olmur
- Müəllim sinaq imtahanı üçün ödəniş etməlidir
- Valideyn uşağının nəticəsini 48-72 saat gecikməklə görür
- Spaced repetition — yanlış suallar 7 gün sonra yenidən çıxır
