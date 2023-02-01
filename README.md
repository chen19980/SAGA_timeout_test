
SAGA Distributed Transactions Pattern using Apache Camel | Microservices Design Pattern


## Get All Data
~~~
curl --location --request GET 'http://localhost:9090/camel/databases'
~~~

## Book an order
~~~
curl --location --request POST 'http://localhost:9090/camel/orders' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--data-raw '{

"customerId": "3000",
"itemName": "Laptop",
"quantity": 1,
"amount": 1.00
}'
~~~

## Swagger
~~~
http://localhost:9090/camel/api-doc
~~~

~~~
Ref: https://www.youtube.com/watch?v=MnDBRs7L75k&t=1562s
Git: https://github.com/jssaggu/camel-tutorial
~~~