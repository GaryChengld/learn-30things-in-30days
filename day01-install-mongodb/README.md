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

 