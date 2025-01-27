# Glady Backend challenge
## Guidelines
* Implement the solution in Java.
* Use the stack and architecture of your choice.
* We do not expect a user interface for this challenge.

## Evaluation
Your solution will be evaluated based on the following criteria:
* Effective use of object-oriented programming principles.
* Code quality and cleanliness.
* Appropriate use of design patterns.
* Ability to write unit tests.

## Statements
Companies can use Wedoogift services to distribute:
- Gift deposits
- Meal deposits

### Gift deposits
Gift deposits has 365 days lifespan, beyond this period it will no longer be counted in the user's balance.

example:
John receives a Gift distribution with the amount of \$100 from Tesla. he will therefore have $100 in gift cards in his account.
He received it on 06/15/2021. The gift distribution will expire on 06/14/2022.

### Meal deposits
Meal deposit works like the Gift deposit excepting for the end date. In fact meal deposits expires at the end of February of the year following the distribution date.

example:
Jessica receives a Meal distribution from Apple with the amount of $50 on 01/01/2020, the distribution ends on 02/28/2021.

* Implement one or two services allowing companies to distribute gift and meal deposits to a user if the company balance allows it.
* Implement a service to calculate the user's balance.

## Rendu
* Projet effectué en Spring Boot avec Java 21
* Tests en JUnit5
* Couverture des tests etimée à 96%

### Pour démarrer le projet
Exécuter le fichier : ChallengeBackendApplication.java

Pas de configuration particulière requise
