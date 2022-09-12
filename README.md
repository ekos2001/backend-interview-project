# NinjaOne Backend Interview Project

The project is configured to use an in-memory H2 database that is volatile. If you wish to make it maintain data on application shut down, you can change the spring.database.jdbc-url to point at a file like `jdbc:h2:file:/{your file path here}`

## Starting the Application

Run the `BackendInterviewProjectApplication` class

Use [Postman](https://www.postman.com/) to import collection file [NinjaOne.postman_collection.json](NinjaOne.postman_collection.json) with API documentation.

For authentication default login is "admin" and password is "admin".
## H2 Console 

In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

http://localhost:8080/h2-console

Enter the information for the url, username, and password in the application.yml:

```yml
url: jdbc:h2:mem:localdb
username: sa 
password: 
```

You should be able to see a db console now that has the Sample Repository in it.

Type:

```sql
SELECT * FROM CUSTOMER;
````

Click `Run`, you should see two rows, for ids `1` and `2`

