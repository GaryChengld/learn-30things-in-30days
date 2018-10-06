# Day 5:  ElasticSearch - A Lucene based REST search engine

Today is the fifth day of my 30 days challenge, so far I'm enjoy and feel comfortable of it.

I did learn [Apache Solr](http://http://lucene.apache.org/solr/) (another open-source search platform based on Lucene) 3 years ago, believe it can help me understand EasticSearch easier.

## What is ElasticSearch

[Elasticsearch](https://www.elastic.co/)  is an open-source, RESTful, distributed search and analytics engine built on Apache Lucene. Since it's released, Elasticsearch has quickly become the most popular search engine, and is commonly used for log analytics, full-text search, security intelligence, business analytics, and operational intelligence use cases.

## Install ElasticSearch on my windows 10

>ElasticSearch is written in java, have java/jre be installed in your windows system

ElasticSearch can be downloaded from [https://www.elastic.co/downloads/elasticsearch](https://www.elastic.co/downloads/elasticsearch)

Download latest (my version is 6.4.2) windows zip package to local file system and extracted the zip file to C:\Development\elasticsearch-6.4.2

## Start ElasticSearch
1. Open a command prompt
2. Change current directory to ElasticSearch installation folder
3. Execute follow commands
```
cd bin
ElasticSearch.bat
```
It displays following message in command prompt
```
): Version 6.4.2 (Build 660eefe6f2ea55) Copyright (c) 2018 Elasticsearch BV
[2018-10-05T22:03:44,818][DEBUG][o.e.a.ActionModule       ] Using REST wrapper from plugin org.elasticsearch.xpack.secur
ity.Security
[2018-10-05T22:03:45,219][INFO ][o.e.d.DiscoveryModule    ] [7hjIEAW] using discovery type [zen]
[2018-10-05T22:03:46,490][INFO ][o.e.n.Node               ] [7hjIEAW] initialized
[2018-10-05T22:03:46,507][INFO ][o.e.n.Node               ] [7hjIEAW] starting ...
[2018-10-05T22:03:47,868][INFO ][o.e.t.TransportService   ] [7hjIEAW] publish_address {127.0.0.1:9300}, bound_addresses
{127.0.0.1:9300}, {[::1]:9300}
[2018-10-05T22:03:50,980][INFO ][o.e.c.s.MasterService    ] [7hjIEAW] zen-disco-elected-as-master ([0] nodes joined)[, ]
, reason: new_master {7hjIEAW}{7hjIEAWgQZSUMVToeasiVw}{NJ6SQ9aoSgmXUDQMG2mmiQ}{127.0.0.1}{127.0.0.1:9300}{ml.machine_mem
ory=6242537472, xpack.installed=true, ml.max_open_jobs=20, ml.enabled=true}
[2018-10-05T22:03:51,005][INFO ][o.e.c.s.ClusterApplierService] [7hjIEAW] new_master {7hjIEAW}{7hjIEAWgQZSUMVToeasiVw}{N
J6SQ9aoSgmXUDQMG2mmiQ}{127.0.0.1}{127.0.0.1:9300}{ml.machine_memory=6242537472, xpack.installed=true, ml.max_open_jobs=2
0, ml.enabled=true}, reason: apply cluster state (from master [master {7hjIEAW}{7hjIEAWgQZSUMVToeasiVw}{NJ6SQ9aoSgmXUDQM
G2mmiQ}{127.0.0.1}{127.0.0.1:9300}{ml.machine_memory=6242537472, xpack.installed=true, ml.max_open_jobs=20, ml.enabled=t
rue} committed version [1] source [zen-disco-elected-as-master ([0] nodes joined)[, ]]])
[2018-10-05T22:03:51,155][WARN ][o.e.x.s.a.s.m.NativeRoleMappingStore] [7hjIEAW] Failed to clear cache for realms [[]]
[2018-10-05T22:03:51,225][INFO ][o.e.g.GatewayService     ] [7hjIEAW] recovered [0] indices into cluster_state
[2018-10-05T22:03:51,528][INFO ][o.e.c.m.MetaDataIndexTemplateService] [7hjIEAW] adding template [.watches] for index pa
tterns [.watches*]
[2018-10-05T22:03:51,637][INFO ][o.e.c.m.MetaDataIndexTemplateService] [7hjIEAW] adding template [.watch-history-9] for
index patterns [.watcher-history-9*]
[2018-10-05T22:03:51,808][INFO ][o.e.c.m.MetaDataIndexTemplateService] [7hjIEAW] adding template [.triggered_watches] fo
r index patterns [.triggered_watches*]
[2018-10-05T22:03:51,941][INFO ][o.e.x.s.t.n.SecurityNetty4HttpServerTransport] [7hjIEAW] publish_address {127.0.0.1:920
0}, bound_addresses {127.0.0.1:9200}, {[::1]:9200}
[2018-10-05T22:03:51,942][INFO ][o.e.n.Node               ] [7hjIEAW] started
```
It's started on 127.0.0.1:9300

Open a browser, type in http://http://localhost:9200/ on url, the browser shows
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
Now ElasticSearch started successfully

### ElasticSearch REST API URL format
ElasticSearch URL format follow http://host:port/index/type
for example
>http://localhost:9200/moviedb/movies

**index must be in lower case**

I'm using postman to do ElasticSearch operations, and use movie as sample document

  <img width="880" src="https://user-images.githubusercontent.com/3359299/46566539-855c7200-c8ee-11e8-830f-809c76dcad8d.PNG" />

### Index a new document
In postman, create below request
 - url: http://localhost:9200/moviedb/movies/tt0076759
 - method: POST
 - body: 
```
{
	"title": "Star Wars: Episode IV - A New Hope",
	"year": 1977,
	"director": " George Lucas",
	"writer": " George Lucas",
	"stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
	"description": "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station, while also attempting to rescue Princess Leia from the evil Darth Vader."
}
```
And click send button, get below response
```
{
    "_index": "moviedb",
    "_type": "movies",
    "_id": "tt0076759",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```
  <img width="880" src="https://user-images.githubusercontent.com/3359299/46566639-7bd40980-c8f0-11e8-8c57-2d583a127b0b.PNG" />
A new movie document is indexed successfully.

Open browser, type in url http://localhost:9200/moviedb/movies/tt0076759, it shows

  <img width="880" src="https://user-images.githubusercontent.com/3359299/46566665-ea18cc00-c8f0-11e8-9316-41f3e8d0e88b.PNG" />
  
Now I'm insert more movie documents
 - url: http://localhost:9200/moviedb/movies/tt0080684
 - method: POST
 - body: 
```
{
	"title": "Star Wars: Episode V - The Empire Strikes Back",
	"year": 1980,
	"director": "Irvin Kershner",
	"writer": "George Lucas",
	"stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
	"description": "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader."
}
```
 - url: http://localhost:9200/moviedb/movies/tt0086190
 - method: POST
 - body: 
```
{
	"title": "Star Wars: Episode VI - Return of the Jedi",
	"year": 1983,
	"director": "Richard Marquand",
	"writer": "George Lucas",
	"stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
	"description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
}
```

### Search document

**Search all movies**
 - url: http://localhost:9200/moviedb/movies/_search
 - method: GET

Response:
```json
{
    "took": 3,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 3,
        "max_score": 1,
        "hits": [
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0076759",
                "_score": 1,
                "_source": {
                    "title": "Star Wars: Episode IV - A New Hope",
                    "year": 1977,
                    "director": " George Lucas",
                    "writer": " George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station, while also attempting to rescue Princess Leia from the evil Darth Vader."
                }
            },
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0086190",
                "_score": 1,
                "_source": {
                    "title": "Star Wars: Episode VI - Return of the Jedi",
                    "year": 1983,
                    "director": "Richard Marquand",
                    "writer": "George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
                }
            },
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0080684",
                "_score": 1,
                "_source": {
                    "title": "Star Wars: Episode V - The Empire Strikes Back",
                    "year": 1980,
                    "director": "Irvin Kershner",
                    "writer": "George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader."
                }
            }
        ]
    }
}
```
**Search with parameter**
Syntax
>http://localhost:9200/moviedb/movies/_search?q=key:value

For example search by year=1977
>http://localhost:9200/moviedb/movies/_search?q=year:1977

Get Response:
```json
{
    "took": 4,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 1,
        "hits": [
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0076759",
                "_score": 1,
                "_source": {
                    "title": "Star Wars: Episode IV - A New Hope",
                    "year": 1977,
                    "director": " George Lucas",
                    "writer": " George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station, while also attempting to rescue Princess Leia from the evil Darth Vader."
                }
            }
        ]
    }
}
```

### Update a document

 - url: http://localhost:9200/moviedb/movies/tt0076759
 - method: PUT
 - body: 
```json
{
	"title": "Star Wars: Episode IV - A New Hope",
	"year": 1977,
	"director": "George Lucas",
	"writer": "George Lucas",
	"stars": "Mark Hamill, Harrison Ford, Carrie Fisher, David Prowse",
	"description": "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station, while also attempting to rescue Princess Leia from the evil Darth Vader."
}
```
Get response
```json
{
    "_index": "moviedb",
    "_type": "movies",
    "_id": "tt0076759",
    "_version": 2,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 2,
    "_primary_term": 2
}
```

### Delete a document
 - url: http://localhost:9200/moviedb/movies/tt0076759
 - method: DELETE
Get response
```json
{
    "_index": "moviedb",
    "_type": "movies",
    "_id": "tt0076759",
    "_version": 3,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 3,
    "_primary_term": 2
}
``` 
The movie tt0076759 is deleted.

### Full text search examples

 - url: http://localhost:9200/moviedb/movies/_search?q=title:Star
 - method: GET

Response
```
{
    "took": 134,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 2,
        "max_score": 0.2876821,
        "hits": [
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0086190",
                "_score": 0.2876821,
                "_source": {
                    "title": "Star Wars: Episode VI - Return of the Jedi",
                    "year": 1983,
                    "director": "Richard Marquand",
                    "writer": "George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
                }
            },
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0080684",
                "_score": 0.2876821,
                "_source": {
                    "title": "Star Wars: Episode V - The Empire Strikes Back",
                    "year": 1980,
                    "director": "Irvin Kershner",
                    "writer": "George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "After the rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader."
                }
            }
        ]
    }
}
```
 - url: http://localhost:9200/moviedb/movies/_search?q=description:Solo
 - method: GET
 
Response:
```
{
    "took": 3,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 0.29058146,
        "hits": [
            {
                "_index": "moviedb",
                "_type": "movies",
                "_id": "tt0086190",
                "_score": 0.29058146,
                "_source": {
                    "title": "Star Wars: Episode VI - Return of the Jedi",
                    "year": 1983,
                    "director": "Richard Marquand",
                    "writer": "George Lucas",
                    "stars": "Mark Hamill, Harrison Ford, Carrie Fisher",
                    "description": "After a daring mission to rescue Han Solo from Jabba the Hutt, the rebels dispatch to Endor to destroy a more powerful Death Star. Meanwhile, Luke struggles to help Vader back from the dark side without falling into the Emperor's trap."
                }
            }
        ]
    }
} 
```

For more on learning ElasticSearch, check out this [Youtube tutorial](https://www.youtube.com/watch?v=ksTTlXNLick).

 
