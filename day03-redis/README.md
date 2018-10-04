# Day 3: Redis - An open-source in-memory key-value database

[Redis](https://redis.io/) is the most popular in-memory database which supports hashes, lists, sets, sorted sets etc.

### Install Redis in local windows system

**My local environment: Windows 10 Home Edition**

The Redis project does not officially support Windows. However, the Microsoft Open Tech group develops and maintains this Windows port targeting Win64 at [https://github.com/MSOpenTech/redis](https://github.com/MSOpenTech/redis)

 1. Open [Redis windows package download](https://github.com/rgl/redis/downloads) page in browser, download redis-2.4.6-setup-64-bit.exe to local computer.
 2. Double click redis-2.4.6-setup-64-bit.exe and follow installation steps
 3. Once it is installed in your windows, the program folder structure as below
    <img width="880" src="https://user-images.githubusercontent.com/3359299/46451592-0c7dde80-c765-11e8-9142-f56a4e81343d.PNG" />
 4. Start Redis server by double click redis-server.exe, it shows below screen
    <img width="880" src="https://user-images.githubusercontent.com/3359299/46452252-d2164080-c768-11e8-9544-9033a8012049.PNG" />
    Redis server is started on port 6379
 5. Start Redis command line interface by double click redis-cli.exe
    <img width="880" src="https://user-images.githubusercontent.com/3359299/46452457-d8f18300-c769-11e8-84c4-1c70fc2a4f15.PNG" />
 6. Now you can execute Redis command in this screen
 ```
 redis 127.0.0.1:6379> set foo bar
 OK
 redis 127.0.0.1:6379> get foo
 "bar"
 redis 127.0.0.1:6379>
 ```
    
### Install Redis in docker

The first step is to install Docker on the windows machine that we want to our Redis on. My computer already installed docker toolbox to run docker. Start Docker terminal, you will see following screen
    <img width="880" src="https://user-images.githubusercontent.com/3359299/46451997-6384b300-c767-11e8-90c5-424d83334aa3.PNG"/>

Getting the Redis image by command
```
docker pull redis
```  
wait until redis image download completed
```
$ docker pull redis
Using default tag: latest
latest: Pulling from library/redis
802b00ed6f79: Pull complete
8b4a21f633de: Pull complete
92e244f8ff14: Pull complete
fbf4770cd9d6: Pull complete
1479f3bcce09: Pull complete
1d0259f5f9fc: Pull complete
Digest: sha256:3af96d7643f46b9cfb475b6e26720db8ac00cbc7396d5b74fe7b0080f6df337e
Status: Downloaded newer image for redis:latest
```
List docker images by command
```
docker image ls
```
You can see Redis image is in the docker
```
$ docker image ls
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
redis               latest              0a153379a539        34 hours ago        83.4MB
hello-world         latest              e38bc07ac18e        5 months ago        1.85kB
java                8-jre               e44d62cf8862        20 months ago       311MB
```

Start a new Redis instance with name “my-redis” on the default 6379 port by following command
```
docker run --name my-redis  -p 6379:6379 -d redis
```
List current docker container by command
```
$ docker container ls
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
4ceb7d828827        redis               "docker-entrypoint.s…"   22 seconds ago      Up 20 seconds       6379/tcp            my-redis
```
Open a command prompt and goto windows Redis program directory, since the machine IP of docker in my windows is 192.168.99.100, run Redis command line by
```
redis-cli -h 192.168.99.100 -p 6379
```
Now the redis-cli is connected to Redis on docker
```
redis 192.168.99.100:6379> ping
PONG
redis 192.168.99.100:6379>
```

## Redis commands

**PING** -- to check whether the server is running or not.
```
redis 192.168.99.100:6379> ping
PONG
redis 192.168.99.100:6379>
```

**SET** -- Writing Data
```
redis 192.168.99.100:6379> set mykey "myvalue"
OK
redis 192.168.99.100:6379>
```

**GET** -- Reading data
```
redis 192.168.99.100:6379> get mykey
"myvalue"
redis 192.168.99.100:6379>
```

**DEL** -- Deleting data
```
redis 192.168.99.100:6379> del mykey
(integer) 1
redis 192.168.99.100:6379> get mykey
(nil)
redis 192.168.99.100:6379>
```

**SETNX** -- Non-Destructive Write, creates a key in memory if and only if the key does not exist already 
```
redis 192.168.99.100:6379> SETNX mykey "myvalue"
(integer) 1
redis 192.168.99.100:6379> GET mykey
"myvalue"
redis 192.168.99.100:6379> SETNX mykey "mynewvalue"
(integer) 0
redis 192.168.99.100:6379> GET mykey
"myvalue"
redis 192.168.99.100:6379>
```

**EXPIRE**  Specify how long that key should stay stored in memory
```
redis 192.168.99.100:6379> SET mykey "myvalue"
OK
redis 192.168.99.100:6379> expire mykey 5
(integer) 1
```
After 5 seconds
```
redis 192.168.99.100:6379> get mykey
(nil)
redis 192.168.99.100:6379>
```

**EXIST** Check key is exists or not
```
redis 192.168.99.100:6379> set mykey "myvalue"
OK
redis 192.168.99.100:6379> exists mykey
(integer) 1
redis 192.168.99.100:6379>
```
### Resource3
[Official website](https://redis.io/)
[Documentation](https://redis.io/documentation)
[Commands](https://redis.io/commands)
[Top 10 Redis Interview Questions](https://career.guru99.com/top-10-redis-interview-questions/)
[Cheat Sheet](https://www.cheatography.com/tasjaevan/cheat-sheets/redis/)

  
