# Bank

This is a springboot application and all the configs are in application.properties (src\main\resources).

The database creation and schema initialization is done using liquibase. its configs are referenced in application.properties as below:

	spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

Please note that the application uses root DB user since the liquibase has to create a DB. Hence ensure that MYSQL root password is set for below property.

	spring.datasource.username=root
	spring.datasource.password=<PASSWORD>

Once DB is initialized, may be create a new user for the DB, and refer it in the above properties.

Exchange rates:
---------------
Since the application has to use exchange rate, when the application is run, if you are behind a firewall ensure that the firewall is open for the application. It uses moneta implementation of JSR 354 - Java Money (https://javamoney.github.io/ri.html)

Running the application:
---------------------

Checkout from git, and then run

> gradlew bootRun

Note that it uses 8080 as the default port.


End points:
---------------
1. Create an account:

POST http://localhost:8080/accountsapi/accounts
{
    "accname":"SGSavings726",
    "currency":"SGD",
    "acctype":"Savings"
}

2. List all accounts:

GET http://localhost:8080/accountsapi/accounts

[
    {
        "accno": 1,
        "accname": "SGSavings726",
        "acctype": {
            "code": "Savings",
            "description": "This is a savings account"
        },
        "currency": {
            "code": "SGD",
            "description": "Singapore Dollar"
        },
        "balance": -132.91
    }
]

3. Create an transaction for one account:

POST http://<host>:<port>/accountsapi/transactions
{
    "accNo":"1",
    "currency":"USD",
    "transAmount":"100",
    "transType":"debit"
}

4. List all Debit transactions for an account starting from a day:

GET http://localhost:8080/accountsapi/transactions?accNo=1&startDate=20 Jan 2021&transType=Debit

[
    {
        "id": 2,
        "accountDetails": {
            "accno": 1,
            "accname": "SGSavings726",
            "acctype": {
                "code": "Savings",
                "description": "This is a savings account"
            },
            "currency": {
                "code": "SGD",
                "description": "Singapore Dollar"
            },
            "balance": -132.91
        },
        "transDate": "2021-01-19T21:23:15.000+00:00",
        "currency": {
            "code": "USD",
            "description": "US Dollar"
        },
        "transAmt": -100.00
    }
]

5. List all transactions of all accounts:

GET http://localhost:8080/accountsapi/transactions


Pending work:
---------------
1. Add proper unit test cases
2. Setup Swagger for the endpoints
3. Error messages should be moved to specific exception classes.
