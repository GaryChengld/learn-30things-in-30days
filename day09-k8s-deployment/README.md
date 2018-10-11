# Day 9: Deploy a java based MicroService to Minikube

Today I'm going to deploy a spring-boot MicroService application to MiniKube

The sample MicorService implements following resources
                        
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

The source is already copied to this directory.

### The Dockerfile to build a Docker image from application

```
FROM openjdk:8-jdk-alpine

ENV SERVICE_APP_FILE target/petstore-0.0.1-SNAPSHOT.jar
# Set the location of the services
ENV SERVICE_HOME /opt/services

EXPOSE 9080
COPY $SERVICE_APP_FILE $SERVICE_HOME/petstore.jar
WORKDIR $SERVICE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar petstore.jar --debug"]
```

#### Build MicroService application

```
mvn clean install
```

#### Test MicroService application locally

Run command

```
mvn spring-boot:run
```

It shows follow messages in command prompt

<img width="880" src="https://user-images.githubusercontent.com/3359299/46710648-d03ff780-cc16-11e8-803f-4fdeb7a0116b.PNG" />

Open http://localhost:9080/v1/pet/ in browser, it displays

<img width="880" src="https://user-images.githubusercontent.com/3359299/46710742-39276f80-cc17-11e8-867e-64af2d625ccc.PNG" />

The jar build is working properly, stop local application by press Ctrl+C

#### Build docker image

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>docker build -t garycheng/petstore:1.0 .
Sending build context to Docker daemon  18.84MB
Step 1/8 : FROM openjdk:8-jdk-alpine
 ---> 54ae553cb104
Step 2/8 : ENV SERVICE_APP_FILE target/petstore-0.0.1-SNAPSHOT.jar
 ---> Using cache
 ---> b8c79e841d16
Step 3/8 : ENV SERVICE_HOME /opt/services
 ---> Using cache
 ---> 7416c8bc4b41
Step 4/8 : EXPOSE 9080
 ---> Using cache
 ---> 1952f7a6e873
Step 5/8 : COPY $SERVICE_APP_FILE $SERVICE_HOME/petstore.jar
 ---> Using cache
 ---> 7be131571ba4
Step 6/8 : WORKDIR $SERVICE_HOME
 ---> Using cache
 ---> dd5cb8a3d181
Step 7/8 : ENTRYPOINT ["sh", "-c"]
 ---> Using cache
 ---> eceb53ec2a4c
Step 8/8 : CMD ["java -jar petstore.jar --debug"]
 ---> Using cache
 ---> 54314948d491
Successfully built 54314948d491
Successfully tagged garycheng/petstore:1.0
SECURITY WARNING: You are building a Docker image from Windows against a non-Windows Docker host. All files and directories added to build context will have '-rwxr-xr-x' permissions. It is recommended to double check and reset permissions for sensitive files and directories.

E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>
```

#### Push the image to Docker Hub

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username: garycheng
Password:
Login Succeeded

E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>docker push garycheng/petstore:1.0
The push refers to repository [docker.io/garycheng/petstore]
d43455466a30: Pushed
f2ec1bba02a6: Mounted from library/openjdk
0c3170905795: Mounted from library/openjdk
df64d3292fd6: Mounted from library/openjdk
1.0: digest: sha256:a86274349784b139cd692d977a66407914c93974e88b495207f325d684337a36 size: 1159

E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>
```

#### Deploy the docker image on Minikube

**Create deployment.yaml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: petstore
  labels:
    app: petstore
spec:
  type: NodePort
  selector:
    app: petstore
  ports:
  - protocol: TCP
    port: 9080
    name: http

---
apiVersion: v1
kind: ReplicationController
metadata:
  name: petstore
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: petstore
    spec:
      containers:
      - name: petstore
        image: garycheng/petstore:1.0
        ports:
        - containerPort: 9080
```        

**Start Minikube**

```
C:\>minikube start
Starting local Kubernetes v1.10.0 cluster...
Starting VM...
Getting VM IP address...
Moving files into cluster...
Setting up certs...
Connecting to cluster...
Setting up kubeconfig...
Starting cluster components...
Kubectl is now configured to use the cluster.
Loading cached images from config file.

C:\>
```

**Deployment petstore**

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>kubectl apply -f deployment.yaml
service/petstore created
replicationcontroller/petstore created

```

**View all Services within Minikube by using the “kubectl get svc” command.**

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>kubectl get svc
NAME         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP          1d
petstore     NodePort    10.107.199.113   <none>        9080:31522/TCP   3m

```

**View all containers in Minikube**

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>kubectl get pods
NAME             READY   STATUS             RESTARTS   AGE
petstore-597gj   0/1     ImagePullBackOff   0          4m
```

**Display service detail**
```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>kubectl describe services petstore
Name:                     petstore
Namespace:                default
Labels:                   app=petstore
Annotations:              kubectl.kubernetes.io/last-applied-configuration:
                            {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"labels":{"app":"petstore"},"name":"petstore","namespace":"default"},"spe...
Selector:                 app=petstore
Type:                     NodePort
IP:                       10.107.199.113
Port:                     http  9080/TCP
TargetPort:               9080/TCP
NodePort:                 http  31522/TCP
Endpoints:
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
```

**Start service**

```
E:\Workspaces\learn-30things-in-30days\day09-k8s-deployment>minikube service petstore
Opening kubernetes service default/petstore in default browser...
```

Then open URL http://192.168.99.101:31522/v1/pet in browser

<img width="880" src="https://user-images.githubusercontent.com/3359299/46715291-5a935600-cc2d-11e8-95a2-525ce7a0520b.PNG" />

Now the MicroService is deployed to Minikube successfully.





