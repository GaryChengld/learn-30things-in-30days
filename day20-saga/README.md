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