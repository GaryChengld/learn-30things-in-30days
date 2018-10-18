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