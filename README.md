# SpringAuthentication   

To support both authentication and authorization, we 

   1. implemented an authentication filter named **JwtAuthenticationFilter** to issue JWTS to users sending credentials.
   2. implemented an authorization filter named **JwtAuthorizationFilter** to validate requests containing JWTS.
   3. created a custom implementation of UserDetailsService named **JwtUserDetailService** to help spring security loading user-specific data in the framework.
   4. extended the WebSecurityConfigurerAdapter class to customize the security framework to our needs.
   
## How to use this code? 

    1. Make sure you have Java 8, Maven and mysql installed.
    
    2. Fork this repository and clone it
       $ git clone https://github.com/<your-user>/SpringAuthentication/
      
    3. Execute all the queries given in JwtAuth/src/main/resources/schema.sql to create database and tables.
    
    4. Edit the JwtAuth/src/main/resources/application.properties if required.
   
    5. Navigate to the project folder and run 
       $ mvn spring-boot:run



