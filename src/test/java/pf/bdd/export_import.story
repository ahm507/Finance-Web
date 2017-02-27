
Story consists of multiple scenarios 

Narrative:

As a user
	I can get balance of a certain account in a certain year

Notes:
    Export exports only a record with tranaction and account path and currency.
     If you have accounts with no related transactions, it will be lost.

Scenario: check balance of accounts in a specific year, Food2

Given user email 'test4@test.com' and password 'secret'

And have these accounts:
    name|type|currency|
    Salary2|income|egp|
    Food2|expense|egp|
    Freelance2|income|usd|
    Education2|expense|usd|



!-- desc must be unique as it is used in test code to find it

And have these transactions:
    date|desc|fromAccount|toAccount|amount|
    2013-12-01|desc1|Salary2|Food2|1000|
	2012-11-01|desc2|Freelance2|Education2|2000|


When exporting and then importing it

Then ensure accounts are:
    name|type|currency|
    Salary2|income|egp|
    Food2|expense|egp|
    Freelance2|income|usd|
    Education2|expense|usd|


And ensure transactions are:
    date|desc|fromAccount|toAccount|amount|
    2013-12-01|desc1|Salary2|Food2|1000|
	2012-11-01|desc2|Freelance2|Education2|2000|

