# Ludological Emporium 

## Project Description

Ludological Emporium is a full-stack e-commerce video game store built with a Java Spring Boot API, MySQL database, and frontend website. 

This project was developed for Capstone 3 in the Year Up United Application Development track. For this capstone, I worked mainly as the backend developer. The project started with an existing website and API, and the task was to fixed bugs, completed missing controller and service logic, adding authenticated shopping cart features, added profile functionality, and implemented checkout/order creation.

The goal of this project was to practice building and maintaining a layered backend application using controllers, services, repositories, models, authentication, database persistence, and API testing.

<br>

---

## Main Features

- User registration and login
- JWT-based authentication
- Product browsing
- Product filtering by category and price
- Category management
- Shopping cart management
- Checkout and order creation
- User profile management
- Role-based access for admin-only actions

<br> 

---

## Technical Overview

The backend handles the main business logic for products, categories, profiles, shopping carts, and orders. It uses a layered Spring Boot structure to keep the code organized.

The API connects to a MySQL database and uses authentication so each user can manage their own cart and checkout information. The frontend lets users interact with the store through website screens instead of manually calling the API.

<br>

### Backend Architecture

The API is organized into four main layers:

- Controllers handle incoming API requests
- Services contain business logic
- Repositories communicate with the database
- Models represent database entities and application data

<br>

### Main Backend Packages

| Layer | Package | Examples |
| ----- | ------- | -------- |
| Controllers | `org.yearup.controllers` | `ProductsController`, `ShoppingCartController`, `OrdersController` |
| Services | `org.yearup.service` | `ProductService`, `ShoppingCartService`, `OrderService` |
| Repositories | `org.yearup.repository` | `ProductRepository`, `ShoppingCartRepository`, `OrderRepository` |
| Models | `org.yearup.models` | `Product`, `CartItem`, `ShoppingCart`, `Order`, `OrderLineItem` |

<br>

---

### API Endpoints

| Area | Method | Endpoint | Description |
| ---- | ------ | -------- | ----------- |
| Authentication | `POST` | `/register` | Registers a new user |
| Authentication | `POST` | `/login` | Logs in a user and returns a JWT token |
| Products | `GET` | `/products` | Gets all or filtered products |
| Products | `GET` | `/products/{id}` | Gets one product by id |
| Products | `POST` | `/products` | Creates a product, admin only |
| Products | `PUT` | `/products/{id}` | Updates a product, admin only |
| Products | `DELETE` | `/products/{id}` | Deletes a product, admin only |
| Categories | `GET` | `/categories` | Gets all categories |
| Categories | `GET` | `/categories/{id}` | Gets one category by id |
| Categories | `GET` | `/categories/{id}/products` | Gets products by category |
| Categories | `POST` | `/categories` | Creates a category, admin only |
| Categories | `PUT` | `/categories/{id}` | Updates a category, admin only |
| Categories | `DELETE` | `/categories/{id}` | Deletes a category, admin only |
| Shopping Cart | `GET` | `/cart` | Gets the current user's cart |
| Shopping Cart | `POST` | `/cart/products/{productId}` | Adds a product to the cart |
| Shopping Cart | `PUT` | `/cart/products/{productId}` | Updates a product quantity |
| Shopping Cart | `DELETE` | `/cart` | Clears the current user's cart |
| Profile | `GET` | `/profile` | Gets the current user's profile |
| Profile | `PUT` | `/profile` | Updates the current user's profile |
| Orders | `POST` | `/orders` | Creates an order from the current user's cart |

</br>

---

## Application Screenshots

Add screenshots of the application screens in an `images` folder inside the repository.


f
### Products Page

<img width="1695" height="917" alt="image-8" src="https://github.com/user-attachments/assets/f8510018-3aa9-4f06-ac2b-83caa2baf006" />


<br>

### Product Filters



<br>

### Shopping Cart Page

<img width="1679" height="855" alt="image-10" src="https://github.com/user-attachments/assets/d04d2690-727f-440b-967e-d5f40e508265" />



<br>

---


## Testing With Insomnia 

The API was tested using the provided Insomnia test collection. The collection included requests for the main backend features, including authentication, products, categories, shopping cart, profile, and checkout/order creation.


<img width="284" height="162" alt="image-2" src="https://github.com/user-attachments/assets/24c8ea5c-c5f9-4ceb-863e-75f3aea01055" />

<br>

<img width="485" height="685" alt="image-3" src="https://github.com/user-attachments/assets/977908b7-dca3-4054-bdec-11aec17fe07e" />

<br>

<img width="522" height="506" alt="image-4" src="https://github.com/user-attachments/assets/0190a3dd-8605-4d8d-b62e-d12295ef68a9" />

<br>

<img width="444" height="516" alt="image-5" src="https://github.com/user-attachments/assets/73e5a166-2dec-4caa-8652-53561211f25f" />

<br>

<img width="460" height="558" alt="image-6" src="https://github.com/user-attachments/assets/3a247f70-ffea-4758-ac8b-6bd33d61f4b1" />

<br>

After running the tests in Insomnia, all provided tests passed successfully.

<br>

<img width="497" height="393" alt="image-7" src="https://github.com/user-attachments/assets/5e11c17b-2449-4dde-8212-91dedfdd984d" />


<br>

---

## Interesting Code: JWT Authentication and Role-Based Access

One interesting part of the code was learning how JWT authentication works with protected API endpoints.

A JWT token is used after login to prove that the user is authenticated. Just because someone is authenticated doesn't mean it's the same as authorization. A valid token only proves who the user is. The backend still needs to check what that user is allowed to access.

In a real world security issues, an application might block access only on the frontend, but the backend API still returns protected data. That is not secure because users can bypass the frontend and call the API directly through tools like Insomnia, Postman, or browser developer tools.

In my project, I implemented endpoints using Spring Security annotations such as `@PreAuthorize` to ensure access is checked on the backend, not just the frontend.

Example:

```java
@PutMapping("{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Category> updateCategory(
    @PathVariable int id, 
    @RequestBody Category category) {

    var updatedCategory = categoryService.update(id, category);

    if (updatedCategory == null) return ResponseEntity.notFound().build();

    return ResponseEntity.ok(updatedCategory); 
}
```

This code makes sure that only an authenticated user with the `ADMIN` role can update an categories. The controller also uses the logged-in user's identity from the token instead of trusting a user ID sent from the frontend.

I learned that while the frontend can hide buttons, pages, or menu options, the backend is still responsible for protecting the actual data and actions.


<br> 

---


## Database Setup

1. Open the API project in IntelliJ IDEA

2. Locate the database script inside the project:

```text
database/create_database_videogamestore.sql
```

3. Open MySQL Workbench

4. Open and run the full SQL script to create the database and add the starter data

5. Confirm that the database was created successfully and contains the required tables

Common tables used in this project include:

```text
users
profiles
categories
products
shopping_cart
orders
order_line_items
```

The starter database includes sample users:

| Username | Password |
|---|---|
| user | password |
| admin | password |
| george | password |

<br>

---

## API Setup

1. Open the API project in IntelliJ IDEA

2. Confirm the project SDK is set to:

```text
Java 17
```

3. Open the Spring Boot configuration file, usually:

```text
application.properties
```

4. Confirm the MySQL connection settings match your local database

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
spring.datasource.username=<insert_username>
spring.datasource.password=<insert_password>
```

5. Run the Spring Boot application from IntelliJ IDEA

6. Confirm the API starts on:

```text
http://localhost:8080
```


Example API endpoints:

```text
POST http://localhost:8080/register
POST http://localhost:8080/login
GET  http://localhost:8080/products
GET  http://localhost:8080/categories
GET  http://localhost:8080/cart
POST http://localhost:8080/orders
```

<br>

---

## Website Setup

1. Open the frontend website folder in VS Code or IntelliJ IDEA

2. Make sure the backend API is running first

```text
http://localhost:8080
```

3. Open the main frontend file, usually:

```text
index.html
```

4. If using VS Code, right-click `index.html` and select:

```text
Open with Live Server
```

5. The website should open in your browser using a local Live Server URL

6. Confirm the website loads correctly and can connect to the backend API running at:

```text
http://localhost:8080
```


Note: The website uses JavaScript to call the backend API. Controllers that are used by the website should include `@CrossOrigin` so the frontend can communicate with the API.



<br>

---


## Technologies Used

### Back End

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Maven

### Front End

- HTML
- CSS
- JavaScript


### Tools

- IntelliJ IDEA
- MySQL Workbench
- Insomnia
- Git
- GitHub



<br> 

---


## Notes

- This project uses a Spring Boot API with a separate frontend website.
- The backend API is the main focus of the capstone work.
- Insomnia was used to test authenticated and unauthenticated API requests.
- ChatGPT was used to help format the README and make small frontend updates for demonstration purposes. Since the main work was backend-focused, quantity controls and a checkout cart view were added so optional backend features could be shown through the website instead of only through Insomnia.


<br> 

---



## Acknowledgements

I want to thank my instructor, Gregor, for being such a great instructor and mentor throughout this program. This program challenged me in a lot of ways, but your encouragement helped me keep going and trust myself more as a developer. Thank you for everything you taught me and for helping me grow both technically and personally! 

Special shoutout to a dear friend of mine who took the time to teach me and help me understand things when I was really stuck. I really appreciate the patience, support, and encouragement you gave me along the way! 
