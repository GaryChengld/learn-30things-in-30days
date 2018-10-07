# Day 7:  GraphQL: an open source data query and manipulation language

Today i'm going to learn GraphQL and build a GraphQL server with java backend.

### What is [GraphQL](https://graphql.org/)

GraphQL is a query language for your API, and a server-side runtime for executing queries by using a type system you define for your data. GraphQL isn't tied to any specific database or storage engine and is instead backed by your existing code and data.

It's created by Facebook with the purpose of building client applications based on intuitive and flexible syntax, for describing their data requirements and interactions.

### Why GraphQL

One of the primary challenges with traditional REST calls is the inability of the client to request a customized (limited or expanded) set of data. In most cases, once the client requests information from the server, it either gets all or none of the fields.

Another difficulty is working and maintain multiple endpoints. As a platform grows, consequently the number will increase. Therefore, clients often need to ask for data from different endpoints.

When building a GraphQL server, it is only necessary to have one URL for all data fetching and mutating. Thus, a client can request a set of data by sending a query string, describing what they want, to a server.

### Basic terminology

 - Query: is a read-only operation requested to a GraphQL server
 - Mutation: is a read-write operation requested to a GraphQL server
 - Resolver: is responsible for mapping the operation and the code running on the backend for handle the request.
 - Type: A Type defines the shape of response data that can be returned from the GraphQL server.
 - Input: like a Type, but defines the shape of input data that is sent to a GraphQL server.
 - Scalar: is a primitive Type, such as a String, Int, Boolean, Float, etc
 - Interface: An Interface will store the names of the fields and their arguments, so GraphQL objects can inherit from it, assuring the use of specific fields.
 - Schema: In GraphQL, the Schema manages queries and mutations, defining what is allowed to be executed in the GraphQL server.
 
 