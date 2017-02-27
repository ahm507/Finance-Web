Narrative:

As a user
	I want to see a tree for all my accounts with balance and a summation of
	balance per category. I should make sure of the workings of different currencies
	correlctley.


Scenario: check balance of accounts in a specific year, Food2

Given user email 'test1@test.com' and password 'secret' and USD rate is 8.0 and SAR rate is 3.0

And have these accounts:
    name|type|currency|
    _Salary_EGP|income|egp|
    _Salary_USD|income|usd|
    _Salary_SAR|income|sar|
    _Cash_EGP|asset|egp|
    _Cash_USD|asset|usd|
    _Cash_SAR|asset|sar|



!-- desc must be unique as it is used in test code to find it
And have these transactions:
    date|desc|fromAccount|toAccount|amount|
    2013-12-01|desc1|_Salary_EGP|_Cash_EGP|100|
    2013-12-01|desc1|_Salary_USD|_Cash_USD|100|
    2013-12-01|desc1|_Salary_SAR|_Cash_SAR|100|


When getting account total balances

Then Income balance is 1200.0

Then Assets balance is 1200.0

