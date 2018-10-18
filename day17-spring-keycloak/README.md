# Day 17: Secure Spring-Boot application with Keycloak

Yesterday I installed Keycloak on my windows 10 and created realm "demo-app", and created client "demo-client", user "testuser" in this realm, today I'm going create a spring-boot application which secured by Keycloak througn OAuth2.

## Create spring-boot project

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47128328-c5144980-d25e-11e8-9a2f-44ae91044d95.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

## Defining Keycloak’s configuration

Add below Keycloak configuration in applicaiton.properties file based on local Keycloak setting
```
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.realm=demo-app
keycloak.resource=demo-client
keycloak.public-client=true
```

Then define some Security constraints in same file

```
keycloak.security-constraints[0].authRoles[0]=user
keycloak.security-constraints[0].securityCollections[0].patterns[0]=/movies/*
```

## Web pages

Create index.html in resource/static

```html
<html>
<head>
    <title>Star Wars Movies</title>
</head>
<body>
<h1>Star Wars Movies</h1>
<a href="/movies">See All</a>
</body>
</html>
```

## Controller

Now need to create a controller class

```java
@Controller
public class MovieController {

    @GetMapping(path = "/movies")
    public String getMovies(Model model){
        model.addAttribute("movies", Arrays.asList("STAR WARS: EPISODE IV A NEW HOPE", "STAR WARS: EPISODE V THE EMPIRE STRIKES BACK","STAR WARS: EPISODE VI RETURN OF THE JEDI"));
        return "movies";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }
}
```

## Movies template

Create a thymeleaf template for displaying movies

```html
<#import "/spring.ftl" as spring>
<html>
<h1>Star Wars Movies</h1>
<ul>
<#list movies as movie>
    <li>${movie}</li>
</#list>
</ul>
<br>
<a href="/logout">Logout</a>
</html>
```

## Start application

Start spring-boot applicaiton, and open http://localhost:9080/ in browser

it shows following page

<img width="880" src="https://user-images.githubusercontent.com/3359299/47130072-f2b0c100-d265-11e8-97c9-820753204ca1.PNG" />

click the “See All” links and it will be redirected to the Keycloak login page:

<img width="880" src="https://user-images.githubusercontent.com/3359299/47130158-533ffe00-d266-11e8-9cbc-fa188fd0e69f.PNG" />

Enter "testuser" as username and "password" as password, since it's temporary password, Keycload ask to reset password in following page

<img width="880" src="https://user-images.githubusercontent.com/3359299/47130251-c184c080-d266-11e8-875f-03982973eeec.PNG" /> 

After reset password, it shows Star Wars Movies page

<img width="880" src="https://user-images.githubusercontent.com/3359299/47130590-54722a80-d268-11e8-9205-47cba35a30cf.PNG" />

If click "Logout", it will go to first page.

 That's all for today, you can find the complete source code under [this folder](.).



