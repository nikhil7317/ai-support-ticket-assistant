# AI Support Ticket Assistant

## Requirements
Java 21, Maven, MySQL 8, Node.js 20+, and an OpenAI API key.

## Setup
1. In MySQL run: `CREATE DATABASE ai_support_db;`
2. Edit `backend/src/main/resources/application.properties` and set your MySQL password.
3. In PowerShell run `setx OPENAI_API_KEY "your_api_key_here"` and open a new terminal.
4. Backend: `cd backend`, then `mvn spring-boot:run`
5. Frontend: open another terminal, `cd frontend`, `npm install`, `npm run dev`
6. Visit http://localhost:5173

The OpenAI key must remain on the backend. Never put it in React code or commit it to Git.
OpenAI API usage may incur charges. AI suggestions may be incorrect; review them before use.
This is a learning starter, not production-ready; add authentication, authorization, rate limiting, tests, and monitoring before production.
