
Narrative:


As a user I want to calculate statistics about my transaction.

!-- For the sake of testing, we need desc to be unique


Scenario: checking expenses

Given user email 'test2@test.com' and password 'secret' and USD rate is 8.0 and SAR rate is 3.0

And user have these accounts
    name|type|currency|
    Bonus2_usd|income|usd|
    Salary2_egp|income|egp|
    Salary2_usd|income|usd|
    Cash2_egp|asset|egp|
    Cash2_usd|asset|usd|
    Bank2_usd|asset|usd|
    Food2_usd|expense|usd|
    Food2_egp|expense|egp|
    Car2_egp|expense|egp|
    Credit Card 2_usd|liability|usd|



And have these transactions:
    date|desc|fromAccount|toAccount|amount|
    2013-10-10|desc1|Bonus2_usd|Cash2_usd|10|
    2013-10-10|desc6|Salary2_usd|Bank2_usd|10|
    2013-10-10|desc7|Bonus2_usd|Bank2_usd|20.5|
    2013-10-10|desc5|Cash2_egp|Food2_egp|10|
    2013-10-10|desc4|Salary2_egp|Cash2_egp|20.5|
    2013-10-10|desc2|Cash2_egp|Car2_egp|8|
    2013-10-10|desc3|Credit Card 2_usd|Bank2_usd|10|
    2013-10-10|desc8|Credit Card 2_usd|Cash2_usd|20.5|
    2013-11-10|desc9|Cash2_egp|Car2_egp|8|
    2013-11-10|desc10|Cash2_egp|Food2_egp|10|


When calculating trend data for category expense in year 2013
Then balance of account 'Food2_usd' in jan is 0.0
And balance of account 'Food2_usd' in dec is 0.0
And balance of account 'Food2_usd' in oct is 0.0
And balance of account 'Food2_usd' in nov is 0.0
And balance of account 'Car2_egp' in oct is 8.0
And balance of account 'Car2_egp' in nov is 8.0


When calculating trend data for category income in year 2013
Then balance of account 'Salary2_egp' in oct is 20.5
And balance of account 'Bonus2_usd' in oct is 244.0

When calculating trend data for category asset in year 2013
Then balance of account 'Bank2_usd' in oct is 324.0


When calculating trend data for category liability in year 2013
Then balance of account 'Credit Card 2_usd' in oct is 244.0


When calculating trend data total statistics for year 2013

Then Income balance of oct is 344.5

Then Expenses balance in oct is 18.0
Then Expenses balance in nov is 18.0

Then Liabilities balance in nov is 244.0

Then Assets balance in oct is 570.5
Then Assets balance in nov is 552.5


Scenario: testing whole expenses statistics over all years

Given user email 'test3@test.com' and password 'secret' and USD rate is 8.0 and SAR rate is 3.0

And user have these accounts
    name|type|currency|
    Salary2_egp|income|egp|
    Cash2_egp|asset|egp|
    Car2_egp|expense|egp|
    liability_egp|liability|egp|


And have these transactions:
    date|desc|fromAccount|toAccount|amount|
    2015-10-10|desc|Salary2_egp|Car2_egp|30|
    2016-10-10|desc|Salary2_egp|Car2_egp|30|
    2016-10-10|desc|Salary2_egp|Car2_egp|10|
    2016-10-10|desc|liability_egp|Cash2_egp|11|
    2016-10-10|desc|liability_egp|Cash2_egp|11|
    2016-10-10|desc|liability_egp|Cash2_egp|11|


When calculating trend data for all categories for all years
Then balance of expenses in year 2015 is 30
Then balance of expenses in year 2016 is 40
And balance of income in year 2015 is 30
And balance of asset in year 2016 is 33
And balance of liability in year 2016 is 33

