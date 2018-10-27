# Day 27: Implement CQRS using Axon framework

## What is CQRS

CQRS means Command Query Responsibility Segregation. The main idea behind CQS is: “A method should either change state of an object, or return a result, but not both. In other words, asking the question should not change the answer. More formally, methods should return a value only if they are referentially transparent and hence possess no side effects.” (Wikipedia) Because of this we can divide a methods into two sets:
                                                     
- Commands - change the state of an object or entire system (sometimes called as modifiers or mutators).
- Queries - return results and do not change the state of an object.

<img width="880" src="https://martinfowler.com/bliki/images/cqrs/cqrs.png" />


## Axon framework

