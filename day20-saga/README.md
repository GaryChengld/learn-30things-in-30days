# Day 20: Saga pattern and distributed transaction on Microservices

This is the 20th day of my challenge, today I'm going to learn Saga pattern.

## Distributed Transactions

Transactions are an essential part of applications. Without them, it would be impossible to maintain data consistency., A transaction is a logically atomic unit of work which may across to multiple resources (database, message queue etc.,), in SOA or monoliths system, transaction has famous properties call ACID

- Atomicity: It means that either a transaction happens in full or doesn’t happen at all. At any point, if the transaction feels it can’t process, it’ll rollback.
- Consistency: It means that the state of the database remains consistent before a transaction begins and after the transaction ends.
- Isolation: It means that multiple transactions can run in parallel without disrupting the consistency of the database.
- Durability: It means that any changes made in the database actually persist.

The way to do it in a monolith system is the Two-Phase Commit. In a two-phase commit, we have a controlling node which houses most of the logic, and we have a few participating nodes on which the actions would be performed.

**But in Micorservices world, 2PC is no longer an option anymore**

**What’s wrong with distributed transactions in microservices?** 

>The microservice architecture structures an application as a set of loosely coupled services

The problem with distributed transactions is that they’re a time bomb for the optimist… but, without the user-friendly countdown. For those who think mostly about the happy paths in their system, distributed transactions will lie in wait, showing no harm day by day, until a little glitch happens in your network and suddenly all hell breaks loose.

Here’s the thing: a distributed transaction is about having one event trying to commit changes to two or more sources of data. So whenever you find yourself in this situation of wanting to commit data to two places. But failure will happen, if you have distributed transactions, then when those fallacies come home to roost, you’ll end up committing data to only one of your two sources. Depending on how you’ve written your system, that could be a very, very bad situation. 

Consider the following high-level Microservices architecture of an e-commerce system:

<img width="600" src="https://blog.couchbase.com/wp-content/uploads/2018/01/e-commerce-architecture-768x520.png" />

In the example above, one can’t just place an order, charge the customer, update the stock and send it to delivery all in a single ACID transaction. To execute this entire flow consistently, you would be required to create a distributed transaction.

We all know how difficult is to implement anything distributed, and transactions, unfortunately, are not an exception. Dealing with transient states, eventual consistency between services, isolations, and rollbacks are scenarios that should be considered during the design phase.

## Solution

Implement each business transaction that spans multiple services as a saga. A saga is a sequence of local transactions. Each local transaction updates the database and publishes a message or event to trigger the next local transaction in the saga. If a local transaction fails because it violates a business rule then the saga executes a series of compensating transactions that undo the changes that were made by the preceding local transactions.

<img width="600" src="https://microservices.io/i/data/saga.jpg" />

## What is Saga Pattern

