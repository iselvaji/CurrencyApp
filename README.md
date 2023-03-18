## Currency App
Currency Conversion App that allows a user view exchange rates for any given currency.

## Features
* Currency Exchange rates must be fetched from Open Exchange Rate API.
* User able to select a currency from a list of currencies provided by the API
* User able to enter desired amount for selected currency
* User then see a list of exchange rates for the selected currency
* Rates should be persisted locally and refreshed no more frequently than every 30 minutes (to limit bandwidth usage)

## Exchange Rate API
https://docs.openexchangerates.org/reference/api-introduction

## Technology
Kotlin - Programming language
Jetpack Compose - UI toolkit for native UI
Jetpack Compose Navigation - Navigation between compose screens
View model - To manage UI-related data in a lifecycle conscious way
State Flow - state holder observable for UI state
KotlinX Serialisation - For Json data parsing
Koin - Kotlin based Dependency Injection library
Ktor - Kotlin based networking library
Room - Database for local storage
Junit and Mockk - Unit testing and mocking
Espresso and Compose Unit - For UI testing
Turbine - library to test kotlin flow

## Architecture.
MVVM and Repository design pattern used

Data Module
- This module defines the abstraction for the remote (Api) and cache (Database) repository and its implementation.
- Remote Module - Contains Remote Api interface and implementation
- Local Module - Contains local database implementation
- Repository - It has implementation to return cached or remote data source depending on the amount of time since the cached data was saved (30 minutes cache time limit)
- Remote api keys are fetched in native c++.
- Testing - Includes the remote and database unit test cases

Domain Module
- This module contains the various use cases. The domain defines all the rules that the program must comply with.
- Testing - Includes the use case test cases
- This module depends on the Data Module.

App module (Presentation)
- Views are with single activity and compose screens and its communicates to domain layer via View model
- Testing - Includes the View model and Compose screen test cases
- This module depends on the Domain Module.

Model Module
- This module defines the models required for Domain layer which will be passed to Presentation layer

Common Module
- This module contains the various common classes and functions which can be used across app

