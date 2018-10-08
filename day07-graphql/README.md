# Day 7:  GraphQL: an open source data query and manipulation language

Today i'm going to learn GraphQL and build a GraphQL server with java backend.

### What is [GraphQL](https://graphql.org/)

GraphQL is a query language for your API, and a server-side runtime for executing queries by using a type system you define for your data. GraphQL isn't tied to any specific database or storage engine and is instead backed by your existing code and data.

It's created by Facebook with the purpose of building client applications based on intuitive and flexible syntax, for describing their data requirements and interactions.

### Why GraphQL

- Fetch only the needed fields
- Pass arguments to fields
- Data virtualization for multiple data sources

### Defining the schema

- programmatically - where type definitions are assembled manually in code
- Schema Definition Language (SDL) **_Recommended_** - where the schema is generated from a textual language-independent description. 

### Basic terminology

 - Query: is a read-only operation requested to a GraphQL server
 - Mutation: is a read-write operation requested to a GraphQL server
 - Resolver: is responsible for mapping the operation and the code running on the backend for handle the request.
 - Type: A Type defines the shape of response data that can be returned from the GraphQL server.
 - Input: like a Type, but defines the shape of input data that is sent to a GraphQL server.
 - Scalar: is a primitive Type, such as a String, Int, Boolean, Float, etc
 - Interface: An Interface will store the names of the fields and their arguments, so GraphQL objects can inherit from it, assuring the use of specific fields.
 - Schema: In GraphQL, the Schema manages queries and mutations, defining what is allowed to be executed in the GraphQL server.
 
 
Now I will convert yesterday's example to GraphQL instead Restful services (still keep insert/update/delete)

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/46589851-6ab80380-ca7c-11e8-8a66-7a0791c01db3.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

Add GraphQL related dependencies to maven pom file
```xml
<dependency>
    <groupId>com.graphql-java</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>3.6.0</version>
</dependency>
<dependency>
    <groupId>com.graphql-java</groupId>
    <artifactId>graphql-java-tools</artifactId>
    <version>3.2.0</version>
</dependency>
```

#### Schema

Create schema file movie.graphql
```
schema {
  query: Query
}

type Query {
  all: [Movie]
  byId(id: String): Movie
  byTitle(title: String): [Movie]
}

type Movie {
  id: ID!
  title: String
  year: Int
  director: String
  writer: String
  stars: String
  description: String
}
```

#### GraphQLService

Create GraphQLService spring bean, and load schema after bean created

```java
@Service
public class GraphQLService {
    @Autowired
    private MovieRepository movieRepository;

    @Value("classpath:/graphql/movie.graphql")
    private Resource resource;

    private GraphQL graphQL;

    @PostConstruct
    private void loadSchema() throws IOException {
        File schemaFile = resource.getFile();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }
```

Then in same class, define RuntimeWiring to map queries in schema to java code

```java
    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("all", findAllDataFetcher)
                        .dataFetcher("byId", findByIdDataFetcher)
                        .dataFetcher("byTitle", findByTitleDataFetcher))
                .build();
    }
```

#### DataFetchers

Implement DataFetchers which retrieve data from data source

**FindAllDataFetcher.java**

```java
@Component
public class FindAllDataFetcher implements DataFetcher<List<Movie>> {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> get(DataFetchingEnvironment dataFetchingEnvironment) {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }
}
```

**FindByIdDataFetcher.java**

```java
@Component
public class FindByIdDataFetcher implements DataFetcher<Movie> {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Movie get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        return movieRepository.findById(id).get();
    }
}
```

**FindByTitleDataFetcher.java**
```java
@Component
public class FindByTitleDataFetcher implements DataFetcher<List<Movie>> {
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String title = dataFetchingEnvironment.getArgument("title");
        return movieRepository.findByTitle(title);
    }
}
```

#### Change Rest Controller class

**MovieController.java**

```java
@RestController
@RequestMapping(value = "/v1/movies")
public class MovieController {
    @Autowired
    private GraphQLService graphQLService;

    @PostMapping
    public Mono<ResponseEntity<Object>> query(@RequestBody String query) {
            return Mono.create(emitter -> {
                ExecutionResult executeResult = graphQLService.getGraphQL().execute(query);
                emitter.success(new ResponseEntity<>(executeResult, HttpStatus.OK));
            }); 
    }
}
```

#### Build application

```
mvn clean install
```

#### Start GraphQL server

```
mvn spring-boot:run
```

#### Results 

I still use postman to test REST requests. All GraphQL request are sent in **POST** method and text type as body.

<img width="880" src="https://user-images.githubusercontent.com/3359299/46612119-a59e5380-cadd-11e8-970b-93b89e819f84.PNG" />

All GraphQL queies are share same endpoint at http://localhost:9080/v1/movies

**Find all movies**

List id and title only

Http Request Body

```
{
  all {
    id
    title
  }
}
```

Response

```json
{
    "errors": [],
    "data": {
        "all": [
            {
                "id": "tt0086190",
                "title": "Star Wars: Episode VI - Return of the Jedi"
            },
            {
                "id": "tt0080684",
                "title": "Star Wars: Episode V - The Empire Strikes Back"
            }
        ]
    },
    "extensions": null
}
```

Now I want show more movie data to include stars and description

Http request body

```
{
  all {
    id
    title
    stars
    description
  }
}
```

Response

```json
{
    "errors": [],
    "data": {
        "all": [
            {
                "id": "tt0086190",
                "title": "Star Wars: Episode VI - Return of the Jedi",
                "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                "description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
            },
            {
                "id": "tt0080684",
                "title": "Star Wars: Episode V - The Empire Strikes Back",
                "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                "description": "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader."
            }
        ]
    },
    "extensions": null
}
```

**Find movie by id**

Http request Body

```
{
  byId(id: "tt0086190") {
    id
    title
    stars
    description
  }
}
```

Response

```json
{
    "errors": [],
    "data": {
        "byId": {
            "id": "tt0086190",
            "title": "Star Wars: Episode VI - Return of the Jedi",
            "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
            "description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
        }
    },
    "extensions": null
}
```

**Find movie by title**

Http request body

```
{
  byTitle(title: "Star Wars") {
    id
    title
  }
}
```

Response

```json
{
    "errors": [],
    "data": {
        "byTitle": [
            {
                "id": "tt0086190",
                "title": "Star Wars: Episode VI - Return of the Jedi"
            },
            {
                "id": "tt0080684",
                "title": "Star Wars: Episode V - The Empire Strikes Back"
            }
        ]
    },
    "extensions": null
}

```

That's all for today, you can find the complete source code under [this folder](.).
 