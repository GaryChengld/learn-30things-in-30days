# Day 8: Kubernetes overview and install Minikube on windows

This is the second week of my 30 day challenge, this week I'm going to focus on container and orchestration technologies.

## Containers and Orchestration

| |Containers|Container Orchestration|
|:---|:---|:---|
|Function|Keep software separated into its own "Clean" view and operation system|Define relationships between containers, where they come from, how they scale, and how they connect t the world around them|
|Predecessors / Alternatives|Virtual Machines, Direct installation|Home grow scripts / Manual, bespoke static configuration between containers
|Packages / Vendors|Docker / RKT / Garden / LXC / Mesos|Kubernetes / Docker Swarm / Amazon ECS / Mesos|

**The Container** 

The "war" maybe coming to a close and Docker is by and far the leader.

**The Orchestration**

- A much newer space
- kubernetes has quite a lead, and emerging as the clear leader & winner
- Cooperation & formal standards bodies are more prevalent in the growth of orchestration.

**Using kubernetes Orchestrating containers**
- Containers have helped simplify & standardize how software is modularized & deployed
- Container orchestration has arisen to help equally simplify & standardize how these containers come together to make a usable software system.
- Kubernetes as a container orchestration standard

### Kubernetes

- Born from a Google internal project in mid-2014(Google "Borg")
- 1.0 Release in july 2015
- Google partnered with Linux Foundation to form the Cloud Native Computing Foundation(CNCF) to offer Kubernetes as an open standard.
- Frequently abbreviated "k8s"

### Advantages of kubernetes

- Based on extensive experience from Google, over a long period of time.
- Large open source community & project, mature governing organization(CNCF)
- Auto-scaling, cloud-agnostic-yet-integratable technologies

### Where does K8S live

- Evaluation/Training - minikube (local version of k8s)
- Development - minikube, dev cluster on a cloud provider
- Deployment - cloud provider or bare metal

## Minikube setup

Note

>My local environment: Window 10 Home Edition

### Overview of installing minikube

- Minikube will be a Kubernetes cluster running on local machine
- Minikube be started and connect it from the kubernetes command tools(kubectl)

### Kubernetes concepts

- **Deployments** - the high-level construct that define an application
- **Pods** - instances of a container in a deployment
- **Services** - endpoints that export ports to the outside world
- Create, delete, modify, and retrieve information about any of these using _kubectl_ command(as well as the Kubernetes local UI)

### Installation steps

Minikube gives you a single node cluster that is running in a VM on your development machine, this require virtualization be enabled in local environment.

1. Install VirtualBox to virtualize windows 10.
2. Install kubectl
3. Install Minukube

#### Download and install VirtualBox

 - Open browser and goto [VirtualBox download page](https://www.virtualbox.org/wiki/Downloads)

<img width="880" src="https://user-images.githubusercontent.com/3359299/46640158-53d6e700-cb38-11e8-9c9c-70273e966503.PNG" />

 - Click "Windows Hosts" under VirtualBox 5.2.18 platform packages, and download executable package to local system.
 - Execute executable package in windows and follow default installation steps until VirtualBox installed to the windows.
 
#### Download kubectl

 - In browser, type  https://storage.googleapis.com/kubernetes-release/release/v1.12.0/bin/windows/amd64/kubectl.exe in url and download kubectl.exe to local.
 - Create folder c:\Development\minikube, it can be any folder name
 - Add c:\Development\minikube default path
 - Copy kubectl.exe to this folder.
 
#### Download minikube

- Open the web browser and access the URL https://github.com/kubernetes/minikube/releases

<img width="880" src="https://user-images.githubusercontent.com/3359299/46640773-e927aa80-cb3b-11e8-8170-6aa4d7cc2d3d.PNG" />

- Scroll down and download the latest version of minikube for windows 64-bit version 
- Once the download completed, rename the file to minikube.exe
- Move the file to c:\Development\minikube
- Open command prompt, execute command
```
C:\>minikube version
minikube version: v0.30.0
```

#### Install minikube
- in same command prompt, type the below command to start minikube VM

```
minikube start
```

After about 15 minutes, minikube is ready to use

```
C:\>minikube start
Starting local Kubernetes v1.10.0 cluster...
Starting VM...
Downloading Minikube ISO
 170.78 MB / 170.78 MB [============================================] 100.00% 0s
Getting VM IP address...
Moving files into cluster...
Downloading kubelet v1.10.0
Downloading kubeadm v1.10.0
Finished Downloading kubeadm v1.10.0
Finished Downloading kubelet v1.10.0
Setting up certs...
Connecting to cluster...
Setting up kubeconfig...
Starting cluster components...
Kubectl is now configured to use the cluster.
Loading cached images from config file.

C:\>
```

#### Verify minikube status 

```
C:\>minikube status
minikube: Running
cluster: Running
kubectl: Misconfigured: pointing to stale minikube-vm.
To fix the kubectl context, run minikube update-context
C:\>

```

Type **minikube update-context** command

```
C:\>minikube update-context
Reconfigured kubeconfig IP, now pointing at 192.168.99.101

C:\>minikube status
minikube: Running
cluster: Running
kubectl: Correctly Configured: pointing to minikube-vm at 192.168.99.101

C:\>
```

Now minikube running correctly.










