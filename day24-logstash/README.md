Day 24: Logstash - Collect, Parse, Transform Logs

In day 5 I learned setuo and using Elasticsearch, today I'm going to learn Log Analysis using Elasticsearch, Logstash and Kibana.

## ELK Stack

The ELK Stack is a collection of open-source products — Elasticsearch,Logstash, Kibana and Filebeat — all developed, managed and maintained by Elastic.

- Elasticsearch - a NoSQL database that is based on the Lucene search engine. 
- Logstash - a log pipeline tool that accepts inputs from various sources, executes different transformations, and exports the data to various targets.
- Kibana - a visualization layer that works on top of Elasticsearch. 
- Filebeat - Installed on client servers that will send their logs to Logstash, Filebeat serves as a log shipping agent that utilizes the lumberjack networking protocol to communicate with Logstash
 
## Install and start Kibana on windows

- Download Kibana from [Kibana downloadpage](https://www.elastic.co/downloads/kibana)
- Unzip Kibana windows package to local directory.
- Open config/kibana.yml in an editor, and set elasticsearch.url to point at Elasticsearch instance.
>elasticsearch.url: "http://localhost:9200"
- Start Elasticsearch
    1. Open a command prompt
    2. Change current directory to ElasticSearch installation directory
    3. Execute follow commands
    ```
    cd bin
    ElasticSearch.bat
    ```
    Open URL http://localhost:9200 in browser, it shows json in browser
    ```json
    {
      "name" : "7hjIEAW",
      "cluster_name" : "elasticsearch",
      "cluster_uuid" : "Uq9HEysvTcGjxW_EeowmIg",
      "version" : {
        "number" : "6.4.2",
        "build_flavor" : "default",
        "build_type" : "zip",
        "build_hash" : "04711c2",
        "build_date" : "2018-09-26T13:34:09.098244Z",
        "build_snapshot" : false,
        "lucene_version" : "7.4.0",
        "minimum_wire_compatibility_version" : "5.6.0",
        "minimum_index_compatibility_version" : "5.0.0"
      },
      "tagline" : "You Know, for Search"
    }
    ```
- Start Kibana
  1. Open another command prompt
  2. Change current directory to Kibana installation directory
  3. Execute follow commands
  ```
  cd bin
  kibana.bat
  ```
  After Kibana started, it shows follow message in command prompt
  
  <img width="880" src="https://user-images.githubusercontent.com/3359299/47475695-b42d8000-d7ea-11e8-9925-fa1fde401d1b.PNG" />
  
  Open URL http://localhost:5601 in browser, it goes to following Kibana main page
   
  <img width="880" src="https://user-images.githubusercontent.com/3359299/47475719-ce675e00-d7ea-11e8-84ad-54d4187582de.PNG" />
  
## Install Logstash on windows    

- Download Logstash from [download page](https://www.elastic.co/downloads/logstash) 
- Unzip Kibana windows package to local directory.

## Config Logstash input 

Create a logstash-tomcat.conf for tomcat access log

```
input {
  file {
    path => "C:\Development\apache-tomcat-8.0.21\logs\localhost_access_log.txt"
    start_position => "beginning"
  }
}

filter {
  if [path] =~ "access" {
    mutate { replace => { "type" => "apache_access" } }
    grok {
      match => { "message" => "%{COMBINEDAPACHELOG}" }
    }
  }
  date {
    match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
  }
}

output {
  elasticsearch {
    hosts => ["localhost:9200"]
  }
  stdout { codec => rubydebug }
}
```

Then start logstash by command

>bin\logstash.bat -f config\logstash-tomcat.conf

Now we should see apache log data in Elasticsearch! Logstash opened and read the specified input file, processing each event it encountered. Any additional lines logged to this file will also be captured, processed by Logstash as events, and stored in Elasticsearch. As an added bonus, they are stashed with the field "type" set to "apache_access" (this is done by the type ⇒ "apache_access" line in the input configuration).

