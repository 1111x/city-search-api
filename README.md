City Search API
===============

Clone locally, then run the Maven test using `mvn test`.
To run, execute `mvn exec:java`. This will start a web server using a Jersey/Grizzly container on port 8080. The Main class is `com.saintsoftware.Main` under the src/main/java directory.

```
russell@ThinkPad-T61 ~/CloudStation/git/xxx/city-search-api $ mvn exec:java
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building city-search-api 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] >>> exec-maven-plugin:1.2.1:java (default-cli) @ city-search-api >>>
[INFO] 
[INFO] <<< exec-maven-plugin:1.2.1:java (default-cli) @ city-search-api <<<
[INFO] 
[INFO] --- exec-maven-plugin:1.2.1:java (default-cli) @ city-search-api ---
Oct 22, 2014 1:22:27 PM org.glassfish.grizzly.http.server.NetworkListener start
INFO: Started listener bound to [localhost:8080]
Oct 22, 2014 1:22:27 PM org.glassfish.grizzly.http.server.HttpServer start
INFO: [HttpServer] Started.
13:22:27.102 [com.saintsoftware.Main.main()] DEBUG com.saintsoftware.Main - Jersey app started with WADL available at http://localhost:8080/application.wadl
Hit enter to stop it...
```

The API should be accessible at [http://localhost:8080/suggestions](http://localhost:8080/suggestions). A search for cities starting with X should return 1 result, using the URL [http://localhost:8080/suggestions?q=x&latitude=45.4685742&longitude=-73.62078629999999](http://localhost:8080/suggestions?q=x&latitude=45.4685742&longitude=-73.62078629999999). The coordinates are for Montreal.
```
{
"suggestions": [
    {
      "distance": 1059.7121556645113,
      "name": "Xenia, OH, USA",
      "score": 0.9471175130662952,
      "longitude": -83.92965,
      "latitude": 39.68478
      }
  ]
}
```

The config.properties defaults to using a Mongo datastore. Other data source implementations can be implemented using the DataSourceInterface. There are stubs for CSVDataSource, MySQLDataSource, and PostgresDataSource. Maven tests will fail unless a data source is setup.
```
dataSourceImpl=com.saintsoftware.dataimpl.MongoDataSource
#dataSourceImpl=com.saintsoftware.dataimpl.MySQLDataSource
#dataSourceImpl=com.saintsoftware.dataimpl.PostgresDataSource
#dataSourceImpl=com.saintsoftware.dataimpl.CSVDataSource
```

MongoDataSource can be setup locally by importing the file cities_canada-usa.tsv. Database should be called "busbud", with collection "cities".

