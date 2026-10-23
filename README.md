# QueryNest — Chat with Your Documents

QueryNest is a web-based document chat application built using Java, Spring Boot, Spring AI, Google Gemini API, and MySQL. It allows users to upload documents, ask questions about their content, and receive AI-generated answers. The application provides a simple chat interface with document management, conversation history, and light/dark mode support.

## Features

- Upload documents in PDF, TXT, DOC, DOCX, RTF, HTML, Markdown, and CSV formats.
- Extract text from uploaded documents.
- Ask questions about document content.
- Receive AI-generated answers powered by Google Gemini.
- View uploaded documents in a sidebar.
- Switch between multiple uploaded documents.
- View conversation history for each document.
- Start a new conversation.
- Use a responsive interface on desktop and mobile devices.
- Toggle between light and dark modes.
- Save the selected theme preference in browser storage.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 3.5.x | Backend application framework |
| Spring MVC | Web controllers and request handling |
| Spring AI | Integration with generative AI |
| Google Gemini API | AI-generated responses |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| MySQL | Relational database |
| Apache Tika | Document text extraction |
| HTML5 | Web page structure |
| CSS3 | Styling and responsive design |
| JavaScript | Client-side interactions |
| Maven | Dependency management and build |
| MySQL Workbench | Database management |

## Database Setup

QueryNest uses MySQL to store document information and conversation messages.

### Step 1: Open MySQL Workbench

Open MySQL Workbench and connect to your local MySQL server, typically at `127.0.0.1:3306`.

### Step 2: Create the Database

Execute the following SQL statement:

```sql
CREATE DATABASE IF NOT EXISTS querynest_db;
```

### Step 3: Create the Application Database User

```sql
CREATE USER IF NOT EXISTS 'querynest_user'@'localhost'
IDENTIFIED BY 'Password@123';

GRANT ALL PRIVILEGES ON querynest_db.*
TO 'querynest_user'@'localhost';

FLUSH PRIVILEGES;
```

Replace `Password@123` with your own strong password.

### Step 4: Verify the Database

Run:

```sql
SHOW DATABASES;
```

## Application Configuration

Open the following file:

`src/main/resources/application.properties`

Configure the application as follows:

```properties
spring.application.name=QueryNest

server.port=8080

spring.datasource.url=jdbc:mysql://127.0.0.1:3306/querynest_db
spring.datasource.username=querynest_user
spring.datasource.password=Password@123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.open-in-view=false

spring.ai.google.genai.api-key={YOUR_GEMINI_API_KEY}
spring.ai.google.genai.chat.options.model=gemini-2.5-flash

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Gemini API Configuration

QueryNest uses the Google Gemini API to generate answers based on uploaded documents.

### Obtain a Gemini API Key

Visit [Google AI Studio](https://aistudio.google.com/) and create an API key.

## Start the application

Run:

```powershell
.\mvnw.cmd spring-boot:run
```

## Open QueryNest

Open your browser and visit:

http://localhost:8080

## How to Use QueryNest

1. Open the QueryNest web application.
2. Upload a document using the file picker or drag-and-drop area.
3. Select a document from the sidebar.
4. Enter a question related to the document.
5. Submit the question and wait for Gemini to generate an answer.