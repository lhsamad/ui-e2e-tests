Feature: Manage Pets
  As a user
  I want to be able to log in and manage pets
  So that I can keep track of adopted pets

  Scenario: User can view the list of pets
    Given the user is on the login page
    When the user logs in with valid credentials
    Then the user should see the pet list

Scenario: User can edit existing pet
    Given the user is logged in
    When the user navigates edit pet
    And the user updates a pet detail
    And the user submits the pet form
    And the user navigates back to the pet list
    Then the newly added pet should appear in the list

  Scenario: User can add a new pet
    Given the user is logged in
    When the user navigates to add a new pet
    And the user enters pet details
    And the user submits the pet form
    And the user navigates back to the pet list
    Then the newly added pet should appear in the list

  Scenario: User can view a pet's details
    Given the user is logged in
    When the user navigates to view a pet with id 1
    Then the user should see the pet details

  Scenario: User can navigate to the home page
    Given the user is logged in
    When the user navigates to the home page
    Then the user should see the home page
