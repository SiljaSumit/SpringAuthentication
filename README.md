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
    
    6. open another terminal and issue the following commands
     
    # HTTP 403 Forbidden status for a request without a token
		curl http://localhost:8080/hello
		
		{"timestamp":"2021-02-07T20:07:05.495+00:00",
		 "status":403,"error":"Forbidden", 
		 "message":"Access Denied",
		 "path":"/hello"
		}
		
	# registers a new user
		curl -H "Content-Type: application/json" -X POST -d '{
		    "username": "admin",
		    "password": "adminpassword"
		}' http://localhost:8080/users/signup

		{"userName":"admin",
		"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNjEyNzM4ODYwLCJleHAiOjE2MTI4MjUyNjB9.mZCfyA9JN1feUQzA9plUjcJYQY13Sa5dbcNKK2yQjeEm3hihYcyqlmM2omxIjGYR27zi2ChL47myNZWAT0kZPw"}

	# log into the application (JWT is generated)
		curl -i -H "Content-Type: application/json" -X POST -d '{
		    "username": "admin",
		    "password": "adminpassword"
		}' http://localhost:8080/users/signin

		HTTP/1.1 200
		Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNjEyNzM4OTc4LCJleHAiOjE2MTI4MjUzNzh9.yfNoJIyoNETFCOkP_h6drJ5ayTS648V4MQufIgOjtQ_DYJ4TTsrozEMuJpWjx4MT03zOxNjsNBN_7WVDulBlWA

	# create a variable with our token
		ACCESS_TOKEN=<OUR_ACCESS_TOKEN>
	
	# use this variable to fetch contacts
		curl -H 'Authorization: Bearer '$ACCESS_TOKEN http://localhost:8080/users/hello
	
		Hello

## References
   	https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
   	
   	https://medium.com/@xoor/jwt-authentication-service-44658409e12c



