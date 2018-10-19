# Day 18: Tracing spring-boot Microservices with Zipkin

Today I'm going to understand the microservices distributed tracing using Zipkin and Spring cloud sleuth framework.

## What is Zipkin

[Zipkin](https://zipkin.io/) is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in microservice architectures. It manages both the collection and lookup of this data.

Internally it has 4 modules –

1. Collector – Once any component sends the trace data arrives to Zipkin collector daemon, it is validated, stored, and indexed for lookups by the Zipkin collector.
2. Storage – This module store and index the lookup data in backend. Cassandra, ElasticSearch and MySQL are supported.
3. Search – This module provides a simple JSON API for finding and retrieving traces stored in backend. The primary consumer of this API is the Web UI.
4. Web UI – A very nice UI interface for viewing traces. 

## Install Ziokin

For windows installation, just download the latest Zipkin server from [maven repository](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec) and run the executable jar file using below command
>java -jar zipkin-server-2.11.7-exec.jar

Once Zipkin is started, it shows below in the command prompt

<img width="880" src="https://user-images.githubusercontent.com/3359299/47195773-623cb400-d32b-11e8-980f-de2e7c1303e4.PNG" />

And Web UI page can be displayed at http://localhost:9411/zipkin/.

<img width="880" src="https://user-images.githubusercontent.com/3359299/47195993-79c86c80-d32c-11e8-8f74-c1ef533f584d.PNG" />