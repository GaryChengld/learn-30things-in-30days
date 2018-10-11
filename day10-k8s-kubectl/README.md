# Day 10: Kubernetes architecture and basic kubectl

Today I continue learn kubernetes deployment

## Kubernetes architecture

<img width="880" src="https://user-images.githubusercontent.com/3359299/46778498-ee265e80-cce1-11e8-9e4e-fe556ec4c73f.PNG" />

<img width="880" src="https://user-images.githubusercontent.com/3359299/46778568-465d6080-cce2-11e8-8196-31ffe6da48de.PNG" />

## Basic kubectl commands

- kubectl provides access to nearly every Kubernetes
- Primary command line access tool

let's start Minikube first

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
```

**List all pods** - kubectl get pods

```
C:\>kubectl get pods
NAME             READY   STATUS    RESTARTS   AGE
petstore-sv7qn   1/1     Running   1          17h
```

**Display detail of pods** - kubectl describe pod

```
C:\>kubectl describe pod petstore-sv7qn
Name:           petstore-sv7qn
Namespace:      default
Node:           minikube/10.0.2.15
Start Time:     Wed, 10 Oct 2018 01:33:38 -0400
Labels:         app=petstore
Annotations:    <none>
Status:         Running
IP:             172.17.0.4
Controlled By:  ReplicationController/petstore
Containers:
  petstore:
    Container ID:   docker://54b229f74b6402e6c7274a9c3868edb694884b3d1938152153b668a18473b690
    Image:          garycheng/petstore:1.0
    Image ID:       docker-pullable://garycheng/petstore@sha256:a86274349784b139cd692d977a66407914c93974e88b495207f325d684337a36
    Port:           9080/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Wed, 10 Oct 2018 18:37:00 -0400
    Last State:     Terminated
      Reason:       Error
      Exit Code:    143
      Started:      Wed, 10 Oct 2018 01:34:03 -0400
      Finished:     Wed, 10 Oct 2018 01:40:53 -0400
    Ready:          True
    Restart Count:  1
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from default-token-g8t4f (ro)
Conditions:
  Type           Status
  Initialized    True
  Ready          True
  PodScheduled   True
Volumes:
  default-token-g8t4f:
    Type:        Secret (a volume populated by a Secret)
    SecretName:  default-token-g8t4f
    Optional:    false
QoS Class:       BestEffort
Node-Selectors:  <none>
Tolerations:     node.kubernetes.io/not-ready:NoExecute for 300s
                 node.kubernetes.io/unreachable:NoExecute for 300s
Events:
  Type    Reason                 Age   From               Message
  ----    ------                 ----  ----               -------
  Normal  Scheduled              17h   default-scheduler  Successfully assigned petstore-sv7qn to minikube
  Normal  SuccessfulMountVolume  17h   kubelet, minikube  MountVolume.SetUp succeeded for volume "default-token-g8t4f"
  Normal  Pulling                17h   kubelet, minikube  pulling image "garycheng/petstore:1.0"
  Normal  Pulled                 17h   kubelet, minikube  Successfully pulled image "garycheng/petstore:1.0"
  Normal  Created                17h   kubelet, minikube  Created container
  Normal  Started                17h   kubelet, minikube  Started container
  Normal  SuccessfulMountVolume  31m   kubelet, minikube  MountVolume.SetUp succeeded for volume "default-token-g8t4f"
  Normal  SandboxChanged         31m   kubelet, minikube  Pod sandbox changed, it will be killed and re-created.
  Normal  Pulled                 30m   kubelet, minikube  Container image "garycheng/petstore:1.0" already present on machine
  Normal  Created                30m   kubelet, minikube  Created container
  Normal  Started                29m   kubelet, minikube  Started container
```

**Kubectl expose port command**

- Exposes a port (TCP or UDP) for a given deployment, pod, or other resource

```
kubectl expose ReplicationController petstore --type=NodePort
```

**kubectl port-forward**

- Forwards one or more local ports to a pod

```
kubectl port-forward petstore-sv7qn 9080:9980
Forwarding from 127.0.0.1:9080 -> 9980
Forwarding from [::1]:9080 -> 9980
```

**kubectl attach**

- Attaches to a process that is already running inside an existing container

```
C:\>kubectl attach petstore-sv7qn
Defaulting container name to petstore.
Use 'kubectl describe pod/ -n default' to see all of the containers in this pod.
If you don't see a command prompt, try pressing enter.
```

**kubectl exec**

- Execute command in a container
- -i option will pass stdin to container
- -t option will sprcify stdin is a TTY

```
C:\>kubectl exec -it petstore-sv7qn whoami
root
```

**kubectl lable pods**

- Update the labels on a resource

```
C:\>kubectl label pods petstore-sv7qn healthy=true
pod/petstore-sv7qn labeled
```

**kubectl run**

- Run a particular image on the cluster

```
C:\>kubectl run hello --image=hello-world --port=5701
kubectl run --generator=deployment/apps.v1beta1 is DEPRECATED and will be removed in a future version. Use kubectl create instead.
deployment.apps/hello created

C:\>kubectl get pods
NAME                     READY   STATUS      RESTARTS   AGE
hello-77d96c6754-4p8pl   0/1     Completed   0          17s
petstore-sv7qn           1/1     Running     1          21h
```

Full kubectl reference & cheat sheet

- Reference: https://kubernetes.io/docs/reference/kubectl/kubectl/
- Cheat sheet: https://kubernetes.io/docs/reference/kubectl/cheatsheet/





