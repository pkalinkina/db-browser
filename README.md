# db-browser

db-browser allows to retrieve details of Postgresql database:
1. list schemas
2. list tables
3. list column metadata
4. columns preview 
5. table statistics (number of rows/attributes)
6. column statistics for numeric types (min, max, average, median)

when running CoreApplication locally, swagger available at http://localhost:8080/swagger-ui.html#/ 
h2 database used to store connection details

### support another database
to support another relational database: create separate module that implements **core** classes 
and include it into **dist** dependencies 

### road Map
1. [postgresql] add constraint information (information_schema constraint views)
2. [main] add security filtering (users shouldn't have access to all database)
3. [main] extend app to support several databases simultaneously 

### miscellaneous
project relies on lombok library to generate boilerplate code (getters/setters)

for project to be correctly displayed in Intellij Idea, please use [Lombok Idea Plugin](https://plugins.jetbrains.com/plugin/6317-lombok)