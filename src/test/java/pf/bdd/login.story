
Narrative:
As a user
I want to perform login
So that I can use the system


Scenario: correct credinitials

Given I am at the login screen
When that I enter username joe@ed.ibm.com and password 'secret'
Then the user should proceed to home screen


Scenario: incorrect username and/or password

Given I am at the login screen
When that I enter username joe@ed.ibm.com and password '1234'
Then the error message 'Wrong username or password entered'



Scenario: empty username and/or password:

Given I am at the login screen
When that I enter username '' and password ''
Then the error message 'Enter username and password'


Scenario: connection error

Given I am at login screen
When that I enter username joe@ed.ibm.com and password '1234'
And connection is timeout after 5 minutes
Then error message is displayed 'connection error'


Scenario: server error

Given I am at login screen
When that I enter username joe@ed.ibm.com and password '1234'
And an error message is thrown
Then error message is displayed 'Internal server error'



Scenario: server error

Given I am at login screen
When that I enter username joe@ed.ibm.com and password '<pass>'
And an error message is thrown
Then error message is displayed '<error>'



Examples:
pass| message
123  |Incrorrect passwor
secret | hello user


