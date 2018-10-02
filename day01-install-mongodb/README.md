# Install and use MongoDB

MongoDB is a NoSQL database which stores the data in form of key-value pairs. It provides high performance and scalability along with data modelling and data management of huge sets of data in an enterprise application.

### MongoDB vs RDBMS
Following are some of the key points in MongoDB vs RDBMS which clearly show differences between MongoDB and RDBMS(SQL Databases):

|RDBMS (SQL Database)|MongoDB (NoSQL Database)|
|:---|:---|
|Supports SQL|Supports JSON query language also|
|Predefined schema|Dynamic schema|
|Database|Database|
|Table|Collection|
|Row|Document|
|Column |Field|
|Primary Key|Default key _id provided by MongoDB itself|
|Foreign key/Join|Embedded Documents|
|GROUP_BY|Aggregation Pipeline|


### Install MongoDB Community Edition on local environment

My local environment: Windows 10 Home edition

### Download

Download the latest MongoDB communite Edition from [MongoDB Download Center](https://www.mongodb.com/download-center/v2/community) and make below selection

<img width="880" src="https://user-images.githubusercontent.com/3359299/46323674-588e1f00-c5bd-11e8-9f2b-2ad7a687b2da.PNG"/>

Click Download button, the row file of MongoDB will be download to your computer, unzip the row file to your local directory.

### Setup data directory
```
cd \
mkdir var
cd var
mkdir mongodb
cd mongodb
mkdir data
mkdir log
```

###  Startup MongoDB

Suppose the installation folder is C:\myfolder\mongodb

Open a command prompt
```
cd C:\myfolder\mongodb
cd bin
mongod --dbpath \var\mongodb\data --logpath \var\mongodb\log\mongod.log
```

This will show waiting for connections message on the console output, which indicates that the mongod.exe process is running successfully. And you can find the database data files are created in \var\mongodb\data directory, and logs are added to \var\mongodb\log\mongod.log file.

To run the MongoDB commands, you need to open another command prompt and execute following command.
```
cd C:\myfolder\mongodb
cd bin
mongo
```
It shows MongoDB shell

<img width="880" src="https://user-images.githubusercontent.com/3359299/46324663-a0fc0b80-c5c2-11e8-82a1-a03f3b7f690a.PNG"/>

Now you are able to run MongoDB commands in the shell.

#### Create database

```
> use testdb
switched to db testdb
```
To check current database list, use the command show dbs.
 ```
 > show dbs
 admin   0.000GB
 config  0.000GB
 local   0.000GB
 ```
 testdb is not in the list, to create database, you need at least create one document into it
 ```
 > db.pet.insert({"id":1, "name":"Goldfish", "category":"Fish"})
 WriteResult({ "nInserted" : 1 })
 > show dbs
 admin   0.000GB
 config  0.000GB
 local   0.000GB
 testdb  0.000GB
 ```
 
#### Drop database
 ```
 > use testdb
 switched to db testdb
 > db
 testdb
 > db.dropDatabase()
 { "dropped" : "testdb", "ok" : 1 }
 > show dbs
 admin   0.000GB
 config  0.000GB
 local   0.000GB
 ```
 
####Create Collection
 ```
> db.createCollection("myCollection", {capped: true, autoIndexId: true, size: 50000})
{
        "note" : "the autoIndexId option is deprecated and will be removed in a future release",
        "ok" : 1
}
> show collections
myCollection
```                               
#### Drop collection
 ```
 > use testdb
 switched to db testdb
 > show collections
 myCollection
 > db.myCollection.drop()
 true
 > show collections
 >
 ```
 
#### Insert document
 ```
 > db.pet.insert({"id":1, "name":"Goldfish", "category":"fish"})
 WriteResult({ "nInserted" : 1 })
 ```
 
#### Query
 
 find all
 ```
 > db.pet.find().pretty()
 {
         "_id" : ObjectId("5bb2e1906ea39748805893ce"),
         "id" : 1,
         "name" : "Goldfish",
         "category" : "fish"
 }
 ```
 find by id
 ```
 > db.pet.find({"id":1}).pretty()
 {
         "_id" : ObjectId("5bb2e1906ea39748805893ce"),
         "id" : 1,
         "name" : "Goldfish",
         "category" : "fish"
 }
 ```
 find by category
 ```
  > db.pet.find({"category":"fish"}).pretty()
  {
          "_id" : ObjectId("5bb2e1906ea39748805893ce"),
          "id" : 1,
          "name" : "Goldfish",
          "category" : "fish"
  }
  ```
  
#### Update
```
> db.pet.update({"id":1}, {$set:{"name":"Angelfish"}})
WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
> db.pet.find({"id":1}).pretty()
{
        "_id" : ObjectId("5bb2e1906ea39748805893ce"),
        "id" : 1,
        "name" : "Angelfish",
        "category" : "fish"
}
```

#### Delete
```
> db.pet.remove({"id":1})
WriteResult({ "nRemoved" : 1 })
> db.pet.find({"id":1}).pretty()
>
```

This is for day 1, very basic of MongoDB but at least it's a good start.
