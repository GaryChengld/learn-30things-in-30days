# Day 8: Kubernetes overview and install Minikube on windows

This is the second week of my 30 day challenge, this week I'm going to focus on container and orchestration technologies.

### Containers and Orchestration

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

### Minikube setup






