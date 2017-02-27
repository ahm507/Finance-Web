Narrative:
As a user
I can get balance of a certain account in a certain year



Scenario: check balance of accounts in a specific year, Food2


!--Given transactions in sql file '"transaction-test.sql"'
Given user email 'test5@test.com' and password 'secret'

!-- for the sake of this tests, accountMgmt names are distinct
And have these accounts:
	name|type|currency|
	Salary2|income|egp|
	Bonus2|income|egp|
	Cash2|asset|egp|
	Bank Account2|asset|egp|
	Food2|expense|egp|
	Clothes2|expense|egp|
	Education2|expense|egp|
	Master Card2|liability|egp|
	Visa Card2|liability|egp|

!-- The number 2 suffix in Salary2 is used to avoid clash with built in accounts created when registering a new user

And have these transactions:
    date|desc|fromAccount|toAccount|amount|
    2013-12-01|desc|Salary2|Cash2|1000|
	2012-11-01|desc|Cash2|Food2|20|
	2012-11-01|desc|Cash2|Food2|50|
	2013-11-01|desc|Cash2|Food2|50|
	2013-11-01|desc|Cash2|Food2|50|
	2013-11-01|desc|Cash2|Food2|50|
	2014-01-01|desc|Cash2|Food2|30|
	2013-12-01|desc|Bonus2|Bank Account2|1000|
	2013-12-01|desc|Salary2|Cash2|1000|
	2013-12-01|desc|Bonus2|Cash2|1000|
	2013-12-01|desc|Bank Account2|Cash2|900|
	2013-12-01|desc|Visa Card2|Cash2|300|
	2013-12-01|desc|Master Card2|Clothes2|100|
	2012-10-01|desc|Cash2|Clothes2|150|
	2013-10-01|desc|Cash2|Clothes2|50|
	2012-12-01|desc|Cash2|Education2|350|
	2013-12-01|desc|Cash2|Education2|200|


When getting transactions of account 'Food2' and year 2012
Then verify balance is 70.00

When getting transactions of account 'Food2' and year 2013
Then verify balance is 150.00

When getting transactions of account 'Cash2' and year 2012
Then verify balance is -570.0

When getting transactions of account 'Cash2' and year 2013
Then verify balance is 3800.0

