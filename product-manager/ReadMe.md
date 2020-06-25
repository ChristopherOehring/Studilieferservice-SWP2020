# Product Manager
A basic product microservice.

## Setup and access
The service Product manager has 4 REST interfaces:
1./create

JSON example:
```
{
    "name": "test",
    "price": 1.99,
    "description": "bla",
    "imageUrl": "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"
}
```

2./search

3./delete

4./listall

**Web Controller:**

You can see a page listing all created products at http://localhost:9060/products


Locally accessed on port 8060 on docker on 9060