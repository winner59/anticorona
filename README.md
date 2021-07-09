
## 코로나 백신 접종 증명서 발급 신청
![image](https://user-images.githubusercontent.com/82795860/123352333-0a55b100-d59a-11eb-8ea6-9aec60b5214e.png)

*전체 소스 받기*
```
git clone https://github.com/winner59/anticorona.git
```

### Table of contents

- [서비스 시나리오](#서비스-시나리오)
  - [기능적 요구사항](#기능적-요구사항)
  - [비기능적 요구사항](#비기능적-요구사항)
- [분석/설계](#분석설계)
  - [AS-IS 조직 (Horizontally-Aligned)](#AS-IS-조직-(Horizontally-Aligned))
  - [TO-BE 조직 (Vertically-Aligned)](#TO-BE-조직-(Vertically-Aligned))
  - [Event 도출](#Event-도출)
  - [부적격 이벤트 제거](#부적격-이벤트-제거)
  - [액터, 커맨드 부착](#액터,-커맨드-부착)
  - [어그리게잇으로 묶기](#어그리게잇으로-묶기)
  - [바운디드 컨텍스트로 묶기](#바운디드-컨텍스트로-묶기)
  - [폴리시 부착/이동 및 컨텍스트 매핑](#폴리시-부착/이동-및-컨텍스트-매핑)
  - [Event Storming 최종 결과](#Event-Storming-최종-결과)
  - [기능 요구사항 Coverage](#기능-요구사항-Coverage)
  - [헥사고날 아키텍처 다이어그램 도출](#헥사고날-아키텍처-다이어그램-도출)
  - [System Architecture](#System-Architecture)
- [구현](#구현)
  - [DDD(Domain Driven Design)의 적용](#DDD(Domain-Driven-Design)의-적용)
  - [Gateway 적용](#Gateway-적용)
  - [CQRS](#CQRS)
  - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
  - [동기식 호출과 Fallback 처리](#동기식-호출과-Fallback-처리)
- [운영](#운영)
  - [Deploy/ Pipeline](#Deploy/Pipeline)
  - [Config Map](#Config-Map)
  - [Persistence Volume](#Persistence-Volume)
  - [Autoscale (HPA)](#Autoscale-(HPA))
  - [Circuit Breaker](#Circuit-Breaker)
  - [Zero-Downtime deploy (Readiness Probe)](#Zero-Downtime-deploy-(Readiness-Probe))
  - [Self-healing (Liveness Probe)](#Self-healing-(Liveness-Probe))


# 서비스 시나리오

## 기능적 요구사항

* 접종 관리자는 접종정보를 등록한다.
* 접종 관리자는 접종정보를 수정한다.
* 고객은 증명서 발급을  신청한다.
* 고객은 증명서 발급 신청을 취소 할 수 있다.
* 증명서 발급 신청은 접종 완료 상태에서만 신청할 수 있다.
* 증명서 발급은 접종 관리자에 의해 발급완료된다.
* 증명서 발급이 완료되면 증명서발급 상태가 변경된다
* 고객은 증명서 발급 신청 정보를 확인 할 수 있다. 
* 증명서 발급 신청과  발급은 게이트웨이를 통해 고객과 통신한다.


## 비기능적 요구사항
* 트랜잭션
    * 증명서 발급은 접종 완료에 한하여 신청 할 수 있다.(Sync 호출)
* 장애격리
    * 증명서 발급 신청 기능이 수행되지 않더라도 신청은 365일 24시간 받을 수 있어야 한다. Async (event-driven), Eventual Consistency
    * 증명서 발급 시스템이 과중 되면 사용자를 잠시동안 받지 않고 예약을 잠시후에 하도록 유도한다. Circuit breaker, fallback
* 성능
     * 고객은 MyPage에서 본인 증명서 발급 신청 상태를 확인하고 증명서를 발급받을 수 있어야 한다.(CQRS)
    
# 분석/설계

## AS-IS 조직 (Horizontally-Aligned)
![Horizontally-Aligned](https://user-images.githubusercontent.com/2360083/119254418-278d0d80-bbf1-11eb-83d1-494ba83aeaf1.png)

## TO-BE 조직 (Vertically-Aligned)
![image](https://user-images.githubusercontent.com/82795860/124969096-43ddf000-e061-11eb-8235-77987ae05835.png)

## Event 도출
![image](https://user-images.githubusercontent.com/82795860/124969169-5a844700-e061-11eb-95db-cb3f180cfa0a.png)

## 부적격 이벤트 제거
![image](https://user-images.githubusercontent.com/82795860/124969211-653edc00-e061-11eb-8470-457133b32bde.png)

```
- 이벤트를 식별하여 타임라인으로 배치하고 중복되거나 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함
- 현업이 사용하는 용어를 그대로 사용(Ubiquitous Language) 
```
## 액터, 커맨드 부착
```
- Event를 발생시키는 Command와 Command를 발생시키는주체, 담당자 또는 시스템을 식별함 
- Command : 접종 정보 등록,접종 정보 수정,증명서 발급 신청,증명서 발급 신청 취소, 증명서 발급 완료
- Actor : 접종관리자,접종자, 시스템 , 증명서 발급 관리자

```
## 어그리게잇으로 묶기
```
- 연관있는 도메인 이벤트들을 Aggregate 로 묶었음 
- Aggregate : 접종정보, 증명서 신청 정보, 증명서 발급 정보

```
## 바운디드 컨텍스트로 묶기
## 폴리시 부착/이동 및 컨텍스트 매핑
```
- Policy의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Res)
```

## Event Storming 최종 결과
![image](https://user-images.githubusercontent.com/82795860/124969329-843d6e00-e061-11eb-8fd1-5c2dde76bbf9.png)

![image](https://user-images.githubusercontent.com/82795860/124969256-725bcb00-e061-11eb-831c-50acd59df256.png)



## 기능 요구사항 Coverage

![image](https://user-images.githubusercontent.com/82795860/125000711-f4f98000-e08b-11eb-8399-7248f058aa86.png)
```
 1.접종관리자는 접종정보 (백신이름,접종상태)을 등록한다
 2.고객은 증명서 발급을 신청한다 (Async)
 3.증명서 발급은 접종 완료상태에서만 신청할 수 있다. (Sync)
 4.발급관리자는 고객이 신청한 증명서를 발급한다(Async)
 5.증명서 발급을 하면 증명서 발급 상태가 변경된다 (Async)

```

## 헥사고날 아키텍처 다이어그램 도출
![image](https://user-images.githubusercontent.com/82795860/124969377-928b8a00-e061-11eb-9650-515994e194ec.png)

## System Architecture
![image](https://user-images.githubusercontent.com/82795860/124970058-6b818800-e062-11eb-8bbd-24874adfdd52.png)


# 구현
분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라,구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다
(각자의 포트넘버는 8081 ~ 8084, 8088 이다)
```shell
cd injection
mvn spring-boot:run

cd applying
mvn spring-boot:run 

cd mypage 
mvn spring-boot:run 

cd issue 
mvn spring-boot:run

cd gateway
mvn spring-boot:run

```
## DDD(Domain-Driven-Design)의 적용
msaez.io 를 통해 구현한 Aggregate 단위로 Entity 를 선언 후, 구현을 진행하였다.
Entity Pattern 과 Repository Pattern을 적용하기 위해 Spring Data REST 의 RestRepository 를 적용하였다.

Applying 서비스의 applying.java

![image](https://user-images.githubusercontent.com/82795860/124970262-ab486f80-e062-11eb-8044-274208d86b93.png)

 Applying 서비스의 PolicyHandler.java

![image](https://user-images.githubusercontent.com/82795860/124970349-c4512080-e062-11eb-8a0b-766534b1ab03.png)

 Applying 서비스의 ApplyingRepository.java

![image](https://user-images.githubusercontent.com/82795860/123354917-90c0c180-d59f-11eb-9760-5a6f31814742.png)

DDD 적용 후 REST API의 테스트를 통하여 정상적으로 동작하는 것을 확인할 수 있었다.

## Gateway 적용

API GateWay를 통하여 마이크로 서비스들의 진입점을 통일할 수 있다. 
다음과 같이 GateWay를 적용하였다.

![image](https://user-images.githubusercontent.com/82795860/124970513-f6628280-e062-11eb-9375-c7aa8848adc9.png)
![image](https://user-images.githubusercontent.com/82795860/125002512-16f50180-e090-11eb-8365-f5d12db55fc3.png)

## CQRS
Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능하게 구현해 두었다.
본 프로젝트에서 View 역할은 mypage 서비스가 수행한다.

증명서 발급 신청(Applied) 실행 후 myPage 화면
![image](https://user-images.githubusercontent.com/82795860/125006282-b3230680-e098-11eb-90e2-297db5673a91.png)
  
## 폴리글랏 퍼시스턴스
mypage 서비스의 DB와 applying/injection/issue 서비스의 DB를 다른 DB를 사용하여 MSA간 서로 다른 종류의 DB간에도 문제 없이 
동작하여 다형성을 만족하는지 확인하였다.(폴리글랏을 만족)

|서비스|DB|pom.xml|
| :--: | :--: | :--: |
|injection| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|mypage| HSQL |![image](https://user-images.githubusercontent.com/2360083/120982836-1842be00-c7b4-11eb-91de-ab01170133fd.png)|
|applying| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|
|issue| H2 |![image](https://user-images.githubusercontent.com/2360083/121104579-4f10e680-c83d-11eb-8cf3-002c3d7ff8dc.png)|

## 동기식 호출과 Fallback 처리
분석단계에서의 조건 중 하나로  증명서 발급 신청은 접종 완료 상태인 경우에만 신청할 수 있으며
신청(applying)->접종(injection) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 
호출 프로토콜은 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다.



Applying 서비스 내 external.InjectionService

![image](https://user-images.githubusercontent.com/82795860/123353483-9668d800-d59c-11eb-9939-039db5370c1e.png)

Applying 서비스 내 Req/Resp

![image](https://user-images.githubusercontent.com/82795860/124970705-33c71000-e063-11eb-85f3-6c25c7da2fcd.png)

Injection 서비스 내 Applying 서비스 Feign Client 요청 대상

![image](https://user-images.githubusercontent.com/82795860/124970833-62dd8180-e063-11eb-83bb-20ebc922fdf6.png)

동작 확인

증명서 발급 신청하기 시도 시  접종 완료 여부를 체크함

접종완료 시 증명서 발급 신청 가능

![image](https://user-images.githubusercontent.com/82795860/125006049-29733900-e098-11eb-808c-cdb79aea9631.png)


접종완료가 아닐경우 증명서 발급 신청 안됨

![image](https://user-images.githubusercontent.com/82795860/125006148-693a2080-e098-11eb-828f-b6e2fa6d6642.png)

  
# 운영
  
## Deploy/ Pipeline
각 구현체들은 각자의 source repository 에 구성되었고, 사용한 CI/CD 플랫폼은 Azure를 사용하였으며, pipeline build script 는 각 프로젝트 폴더 이하에 cloudbuild.yml 에 포함되었다.

- git에서 소스 가져오기

```
git clone https://github.com/winner59/anticorona.git
```

- Build 하기

```bash
cd /anticorona
cd gateway
mvn package

cd ..
cd applying
mvn package

cd ..
cd injection
mvn package

cd ..
cd issue
mvn package

cd ..
cd mypage
mvn package

```

- Docker Image Push/deploy/서비스생성(yml이용)

```sh
-- 기본 namespace 설정
kubectl config set-context --current --namespace=anticorona

-- namespace 생성
kubectl create ns anticorona

cd gateway
az acr build --registry skccanticorona --image skccanticorona.azurecr.io/gateway:latest .

cd kubernetes
kubectl apply -f deployment.yml
kubectl apply -f service.yaml

cd ..
cd applying
az acr build --registry skccanticorona --image skccanticorona.azurecr.io/applying:latest .

cd kubernetes
kubectl apply -f deployment.yml
kubectl apply -f service.yaml

cd ..
cd injection
az acr build --registry skccanticorona --image skccanticorona.azurecr.io/injection:latest .

cd kubernetes
kubectl apply -f deployment.yml
kubectl apply -f service.yaml

cd ..
cd issue
az acr build --registry skccanticorona --image skccanticorona.azurecr.io/issue:latest .

cd kubernetes
kubectl apply -f deployment.yml
kubectl apply -f service.yaml

cd ..
cd mypage
az acr build --registry skccanticorona --image skccanticorona.azurecr.io/mypage:latest .

cd kubernetes
kubectl apply -f deployment.yml
kubectl apply -f service.yaml


```

- anticorona/applying/kubernetes/deployment.yml 파일 
```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: applying
  namespace: anticorona
  labels:
    app: applying
spec:
  replicas: 1
  selector:
    matchLabels:
      app: applying
  template:
    metadata:
      labels:
        app: applying
    spec:
      containers:
        - name: applying
          image: skccanticorona.azurecr.io/applying:latest
          ports:
            - containerPort: 8080
          env:
            - name: injection-url
              valueFrom:
                configMapKeyRef:
                  name: apiurl
                  key: url
          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
          livenessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 120
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5
          volumeMounts:
            - name: volume
              mountPath: "/mnt/azure"
          resources:
            requests:
              memory: "64Mi"
              cpu: "250m"
            limits:
              memory: "500Mi"
              cpu: "500m"
      volumes:
      - name: volume
        persistentVolumeClaim:
          claimName: applying-disk
```	
- anticorona/applying/kubernetes/service.yaml 파일 

```yml
apiVersion: v1
kind: Service
metadata:
  name: applying
  namespace: anticorona
  labels:
    app: applying
spec:
  ports:
    - port: 8080
      targetPort: 8080
  selector:
    app: applying
```	  

- deploy 완료
![image](https://user-images.githubusercontent.com/82795860/124970951-98826a80-e063-11eb-97bd-7ba543383a80.png)


***

## Config Map

- 변경 가능성이 있는 설정을 ConfigMap을 사용하여 관리  
  - applying 서비스에서 바라보는 injection 서비스 url 일부분을 ConfigMap 사용하여 구현​  

- in applying src (applying/src/main/java/anticorona/external/InjectionService.java)  
![image](https://user-images.githubusercontent.com/82795860/125002770-a3072900-e090-11eb-8811-896ed0516e65.png)

- applying application.yml (applying/src/main/resources/application.yml)​  
  ![image](https://user-images.githubusercontent.com/82795860/123354126-f4e28600-d59d-11eb-8db6-7d4e60b2a299.png)

- applying deploy yml (applying/kubernetes/deployment.yml)  
  ![image](https://user-images.githubusercontent.com/82795860/123354155-0330a200-d59e-11eb-8f10-0c5198b150a3.png)

- configmap 생성 후 조회

    ```sh
    kubectl create configmap apiurl --from-literal=url=injection -n anticorona
    ```

  ![image](https://user-images.githubusercontent.com/82795860/125003058-4c4e1f00-e091-11eb-8a50-81a91f4069c9.png)

- configmap 삭제 후, 에러 확인  

    ```sh
    kubectl delete configmap apiurl
    ```

![image](https://user-images.githubusercontent.com/82795860/125003279-d26a6580-e091-11eb-9a25-de40dba6c3e9.png)

![image](https://user-images.githubusercontent.com/82795860/125003351-00e84080-e092-11eb-9bbf-fab88cddb259.png)

## Persistence Volume
  
PVC 생성 파일

<code>applying-pvc.yml</code>
- AccessModes: **ReadWriteMany**
- storeageClass: **azurefile**
![image](https://user-images.githubusercontent.com/82795860/123357735-13984b00-d5a5-11eb-9475-86d023eac0a4.png)

<code>deployment.yml</code>

- Container에 Volumn Mount

![image](https://user-images.githubusercontent.com/2360083/120983890-175e5c00-c7b5-11eb-9332-04033438cea1.png)

<code>application.yml</code>
- profile: **docker**
- logging.file: PVC Mount 경로

![image](https://user-images.githubusercontent.com/2360083/120983856-10374e00-c7b5-11eb-93d5-42e1178912a8.png)

마운트 경로에 logging file 생성 확인

```sh
$ kubectl exec -it pod/applying-5bbc5b6cfd-4r4dg -- /bin/sh
$ cd /mnt/azure/logs
$ tail -f applying.log
```

![image](https://user-images.githubusercontent.com/82795860/125009031-b7522280-e09e-11eb-92ee-90b6c9483307.png)

## Autoscale (HPA)

  앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 

- 증명서 발급 서비스에 리소스에 대한 사용량을 정의한다.

<code>applying/kubernetes/deployment.yml</code>

```yml
  resources:
    requests:
      memory: "64Mi"
      cpu: "250m"
    limits:
      memory: "500Mi"
      cpu: "500m"
```

- 증명서 신청 서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:

```sh
$ kubectl autoscale deploy applying --min=1 --max=10 --cpu-percent=15
```

![image](https://user-images.githubusercontent.com/82795860/125007197-aef7e880-e09a-11eb-8a45-c8915603dbed.png)


- CB 에서 했던 방식대로 워크로드를 걸어준다.

```sh
$ siege -c200 -t10S -v --content-type "application/json" 'http://applying:8080/applyings POST {"injectionId":2, "vcName":"FIZER",  "status":"INJECTED"}'
```

- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어둔다:

```sh
$ watch kubectl get all
```

- 어느정도 시간이 흐른 후 스케일 아웃이 벌어지는 것을 확인할 수 있다:

* siege 부하테스트 - 전

![image](https://user-images.githubusercontent.com/82795860/125007630-a05e0100-e09b-11eb-95cc-e927e8dfc7c6.png)

* siege 부하테스트 - 후

![image](https://user-images.githubusercontent.com/82795860/125008007-883ab180-e09c-11eb-9ba6-7baaf4e99de0.png)


- siege 의 로그를 보아도 전체적인 성공률이 높아진 것을 확인 할 수 있다. 

![image](https://user-images.githubusercontent.com/82795860/125007714-d0a59f80-e09b-11eb-9fb4-fbc0daf4b8be.png)

## Circuit Breaker

  * 서킷 브레이킹 프레임워크의 선택: Spring FeignClient + Istio를 설치하여, anticorona namespace에 주입하여 구현함

시나리오는 증명서발급 신청(applying)-->증명서 발급(issue) 연결을 RESTful Request/Response 로 연동하여 구현이 되어있고, 발습 신청 요청이 과도할 경우 CB 를 통하여 장애격리.

- Istio 다운로드 및 PATH 추가, 설치, namespace에 istio주입

```sh
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.7.1 TARGET_ARCH=x86_64 sh -
※ istio v1.7.1은 Kubernetes 1.16이상에서만 동작
```

- istio PATH 추가

```sh
$ cd istio-1.7.1
$ export PATH=$PWD/bin:$PATH
```

- istio 설치

```sh
$ istioctl install --set profile=demo --set hub=gcr.io/istio-release
※ Docker Hub Rate Limiting 우회 설정
```

- namespace에 istio주입

```sh
$ kubectl label anticorona tutorial istio-issue=enabled
```

- Virsual Service 생성 (Timeout 3초 설정)
- anticorona/issue/kubernetes/issue-istio.yaml 파일 

```yml
  apiVersion: networking.istio.io/v1alpha3
  kind: VirtualService
  metadata:
    name: vs-applying-network-rule
    namespace: anticorona
  spec:
    hosts:
    - booking
    http:
    - route:
      - destination:
          host: applying
      timeout: 3s
```	  

![image](https://user-images.githubusercontent.com/82795806/120985451-956f3280-c7b6-11eb-95a4-eb5a8c1ebce4.png)


- Applying 서비스 재배포 후 Pod에 CB 부착 확인

![image](https://user-images.githubusercontent.com/82795806/120985804-ed0d9e00-c7b6-11eb-9f13-8a961c73adc0.png)


- 부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:
  - 동시사용자 100명, 60초 동안 실시

```sh
$ siege -c100 -t10S -v --content-type "application/json" 'http://booking:8080/bookings POST {"vaccineId":1, "vcName":"FIZER", "userId":5, "status":"BOOKED"}'
```
![image](https://user-images.githubusercontent.com/82795806/120986972-1549cc80-c7b8-11eb-83e1-7bac5a0e80ed.png)


- 운영시스템은 죽지 않고 지속적으로 CB 에 의하여 적절히 회로가 열림과 닫힘이 벌어지면서 자원을 보호하고 있음을 보여줌. 
- 약 84%정도 정상적으로 처리되었음.

***

## Zero-Downtime deploy (Readiness Probe)

- deployment.yml에 정상 적용되어 있는 readinessProbe  
```yml
readinessProbe:
  httpGet:
    path: '/actuator/health'
    port: 8080
  initialDelaySeconds: 10
  timeoutSeconds: 2
  periodSeconds: 5
  failureThreshold: 10
```

- deployment.yml에서 readiness 설정 제거 후, 배포중 siege 테스트 진행  
    - hpa 설정에 의해 target 지수 초과하여 booking scale-out 진행됨  
        ![readiness-배포중](https://user-images.githubusercontent.com/18115456/120991348-7ecbda00-c7bc-11eb-8b4d-bdb6dacad1cf.png)

    - booking이 배포되는 중,  
    정상 실행중인 booking으로의 요청은 성공(201),  
    배포중인 booking으로의 요청은 실패(503 - Service Unavailable) 확인
        ![readiness2](https://user-images.githubusercontent.com/18115456/120987386-81c4cb80-c7b8-11eb-84e7-5c00a9b1a2ff.PNG)  

- 다시 readiness 정상 적용 후, Availability 100% 확인  
![readiness4](https://user-images.githubusercontent.com/18115456/120987393-825d6200-c7b8-11eb-887e-d01519123d42.PNG)

    
## Self-healing (Liveness Probe)

- deployment.yml에 정상 적용되어 있는 livenessProbe  

```yml
livenessProbe:
  httpGet:
    path: '/actuator/health'
    port: 8080
  initialDelaySeconds: 120
  timeoutSeconds: 2
  periodSeconds: 5
  failureThreshold: 5
```

- port 및 path 잘못된 값으로 변경 후, retry 시도 확인 (in booking 서비스)  
    - booking deploy yml 수정  
        ![selfhealing(liveness)-세팅변경](https://user-images.githubusercontent.com/18115456/120985806-ed0d9e00-c7b6-11eb-834f-ffd2c627ecf0.png)

    - retry 시도 확인  
        ![selfhealing(liveness)-restarts수](https://user-images.githubusercontent.com/18115456/120985797-ebdc7100-c7b6-11eb-8b29-fed32d4a15a3.png)  
