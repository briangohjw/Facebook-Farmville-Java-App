## Connecting to the Database

The default port is 3306, username is root and password is empty.

If your machine has different port or credentials, please change the values in the ConnectionFactory class located in:

src\main\java\daos\ConnectionFactory.java

```bash
jdbc:mysql://localhost:3306/oop?useSSL=false&user=root&serverTimezone=UTC
```