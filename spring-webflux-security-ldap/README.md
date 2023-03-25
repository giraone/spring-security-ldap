# Spring WebFlux Security with external LDAP

## Shows

- Spring Security setup with Spring WebFlux
- Method based security with Spring WebFlux

## Setup

- 5 users: user1, user2, user3, admin, other
- 2 roles: A1 (user1, user2), A2 (user3), USER (user1, user2, user3), ADMIN (admin)

### Build

`mvn package`

### Tests

Direct access to the LDAP without any user authentication on the `ldap/**` endpoints (for debugging only):

```shell
curl http://localhost:8080/ldap/users
curl http://localhost:8080/ldap/groups
```

Using BASIC authentication on the `api/**` endpoints:

```
# Returns 401
curl -v http://localhost:8080/api/balance/U1
# Returns 200
curl --basic --user user1:user1 http://localhost:8080/api/who-am-i
curl --basic --user user1:user1 http://localhost:8080/api/my-details
curl --basic --user user1:user1 http://localhost:8080/api/balance/A1
curl --basic --user user2:user2 http://localhost:8080/api/balance/A1
curl --basic --user user3:user3 http://localhost:8080/api/balance/A2
curl --basic --user admin:admin http://localhost:8080/api/balance/A1
curl --basic --user admin:admin http://localhost:8080/api/balance/A2
# Returns 403
curl -v --basic --user user1:user1 http://localhost:8080/api/balance/A2
curl -v --basic --user user3:user3 http://localhost:8080/api/balance/A1
```

## Links

- [WebFlux Security](https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html)
- [Reactive Test Support](https://docs.spring.io/spring-security/reference/reactive/test/index.html)
- [Expression-Based Access Control](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)
- [LDAP Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/ldap.html)
- [Password Encoder in Spring Security](https://springhow.com/spring-security-password-encoder/)