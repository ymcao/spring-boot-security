# spring-boot-security
### I'am learning springboot,I'am a mobile coder

`springboot create and refresh token use jwt`

> build.gradle

```c
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    runtime('mysql:mysql-connector-java')
    compile("org.springframework.boot:spring-boot-starter-web")
    compile ("org.springframework.boot:spring-boot-starter-data-rest")
    //compile( "org.springframework.boot:spring-boot-starter-jetty")
    compile ("org.springframework.boot:spring-boot-starter-data-jpa")
    //compile ("org.springframework.boot:spring-boot-configuration-processor")
    compile ("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile ("org.springframework.boot:spring-boot-starter-security")
    compile group: 'io.jsonwebtoken', name: 'jjwt', version: '0.7.0'
    compile 'org.springframework.mobile:spring-mobile-device:1.1.5.RELEASE'
    compile 'javax.servlet:javax.servlet-api:3.1.0'
}

```
Thanks for 
[https://github.com/spring-projects/spring-boot](https://github.com/spring-projects/spring-boot)

### ScreenShot:
![Image](http://github.com/ymcao/spring-boot-security/raw/master/screenshot/F8863352-5741-4B28-93D0-BE027C96DF85.png)

![Image](http://github.com/ymcao/spring-boot-security/raw/master/screenshot/4B3A5219-7709-4113-8482-4413A6994A29.png)


<table>
  <tr>
    <td>Next Plan</td>
  </tr>
  <tr>
    <td>1. Add Redis cache</td>
  </tr>
   <tr>
    <td>2. Add Auth2.0</td>
       
  </tr>
 
</table>
