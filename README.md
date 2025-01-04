# Overview

This project is a RESTful API built using Spring that provides functionalities for a simple To-Do list application. Users can register, log in, and manage their tasks, including adding, viewing, updating, and deleting tasks. Additionally, users can create tasks with attached files.

# Features

* **User Registration**
  * New users can sign up by providing a username, email, and password.
* **User Authorization**
  * Users can log in to the application using their credentials.
* **Task Management**
  * Users can:
    * Add new tasks
    * View all tasks
    * Update existing tasks
    * Delete tasks
* **File Attachments**
  * Users can create tasks that include file attachments.

# API Endpoints
## User Authentication

* **Register User**
  * POST /api/auth/signup
    * **Request Body:**

    `{
    "username": "peter1",
    "email": "abc@gmail.com",
    "password": "peter23"
    }`

* **Login User**
  * POST /api/auth/login
    * **Request Body:**

  `{
  "username": "peter",
  "password": "peter23"
  }`
  
* **Get Users**
  * GET /api/users

* **Refresh Token**
  * POST /api/auth/refresh-token
    * **Request Body:**

  `{
  "refreshToken": "your_refresh_token_here"
  }`

## Task Management

* **Get All Tasks**
  * GET /api/tasks

* **Get Task by ID**
  * GET /api/tasks/{taskId}

* **Add Task**
  * POST /api/tasks
    * **Request Body:**

  `{
  "title": "Improve UI",
  "description": "Please ask teamlead about details"
  }`
  
* **Update Task**
  * PUT /api/tasks/{taskId}
  * **Request Body:** Task details to update
* **Delete Task**
  * DELETE /api/tasks/{taskId}
  * 
## To-Do List Management
* **Get All TODO Lists**
  * GET /api/todolist
* **Get TODO List by ID**
  * GET /api/todolist/{todolistId}
* **Create TODO List**
* POST /api/todolist

## File Management
* **Upload File With Tasks**
  * POST /api/tasks/upload
    * Form Data: File to be uploaded

# Postman Collection
You can import the following Postman collection to test the API endpoints:
`project-root/TODO list.postman_collection.json`

## Conclusion

This REST API provides a functional base for a To-Do list application. Feel free to extend the functionality and enhance the user experience. For further questions or contributions, please contact the repository owner.