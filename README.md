# job4j_cars
## Project on course job4j.ru
### Description
* In project implement cars shop.
* This application is car marketplace with posts about sale cars from owners.
* There is User authorisation by Session. User has own timezone. DateTime is kept in GMT+0 timezone.
* Owner can publish Post with Car. Only owner can edit post and car information.
* All users see Posts. There are filters.
* Owner of Post can change current Price. There are Price History for Post.

### Author
Viacheslav Osipov  
[slavaosipov1199@gmail.com](mailto:slavaosipov1199@gmail.com)  
[LinkedIn](https://www.linkedin.com/in/viacheslav-osipov-67806ab3/)

### Technologies
Spring Boot, Hibernate, PostgreSQL, H2, Liquibase, Lombok, Thymeleaf, Bootstrap, Lombok, Slf4j, Mapstruct, Mockito

### Code coverage: 70%

### Patterns
Model View Controller(MVC), Data Transfer Object (DTO)

### Environment
Java 17, Maven 3.9.6, PostgreSql 16

### How to run
* Create database 'cars'
* Check credentials: db\scripts\liquibase.properties, db\scripts\liquibase_test.properties
* Enter to 'job4j_cars\' directory
* mvn package
* java -jar target\job4j_cars-1.0-SNAPSHOT.jar
### There are demo data:
* Copy image files from 'demo\images\' to 'files'.
* Run SQL script on Database 'demo\script_demo_data.sql'
* You can enter with login/password 'john@gmail.com'/'john' or 'kate@gmail.com'/'kate'

### Database Diagram
![ER Diagram](screenshots/er-diagram.jpg)

### Screenshots of the interface
![Registration page](screenshots/0.jpg)
![Login page](screenshots/1.jpg)
![Post List page](screenshots/2.jpg)
![Add Post page](screenshots/3.jpg)
![Car list page](screenshots/4.jpg)
![Edit Car page](screenshots/5.jpg)
![Post View page](screenshots/6.jpg)
![Post View Owner page](screenshots/7.jpg)
