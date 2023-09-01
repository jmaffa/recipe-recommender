### Project name: Cooler Recipe Recommender
## General Overview: A web app that recommends recipes to users based on the ingredients they currently have. 
## Team members and contributions (include cs logins)
Nadya Tan (ntan13): Database Integration 
Created database readers + handlers on the backend 
Added functionality to frontend components to call relevant endpoints + render input accordingly
Google sign-on on frontend

Joe Maffa (jmaffa): Frontend Development 
Designed (Figma) and developed frontend web app framework
Used React Router to have multi-page functionality
Adapted Material UI components to fit project specifications

Zyn Yee Ang (zang): Recommender Algorithm 
Ratings-based recommender through SlopeOne algorithm
Ingredient substitution recommender (web-scraped data)
Backend API endpoints by userID
Testing of SlopeOne, Recommender and UserHandler
Lynda Winnie Umuhoza (lumuhoza): Recipe API Handling and Testing

Handled Spoonacular API integration into the project.
Backend API endpoint for RecipeHandler
Backend Testing for the handlers.
### Include the total estimated time it took to complete the project: 100+ hours
### A link to your repo: https://github.com/cs0320-f2022/term-project-jmaffa-lumuhoza-ntan13-zang 

### Design choices -- high-level design of your program
## Frontend Pages 
Login: user login is handled by a pop up to sign in with Google (function in Firebase.tsx file) 

Home: automatically renders the list of ingredients that the user has, allows the user to add and delete ingredients (done by making calls to the backend which edits the Firebase database) 

Recipes: allows users to get a list of recipes (with clickable links that take them to the recipe’s URL) that they can make using the ingredients list in their profiles. Done by making calls to the backend (User Handler) 

Planner: allows users to get a meal plan for the upcoming week 

Waste: a static page which contains more information about the motivation behind this project, as well as information on food waste 

Saved: automatically renders the list of saved recipes for the user, allowing the user to unsave and update ratings (done by making calls to the backend which edits the Firebase database) 

Feeling Lucky? Allows users to get a list of recipes that we think they’d like based on the ratings that they have given previously tried recipes. Makes a call to our backend slopeOne recommendation system 

## Backend (API Server) 
Database Handler, Reader and Writer 
User Handler: takes in a user ID, and returns recipes that the user can make based on the ingredients in their profile 

Able to filter responses based on query parameters, including 
Recipe Handler: The Recipe Handler essentially makes calls to the Spoonacular recipe information endpoint. This gives us access to the RecipeID, titles, source url for the recipe, ready in minutes, and price per serving.  

SlopeOne recommendation system (https://en.wikipedia.org/wiki/Slope_One) 
A type of collaborative filtering algorithm
Recommends recipes to users based on recipes that they previously enjoyed (classified based on ratings given) and recipes that others in the database have enjoyed so far

CSV file-parsing for ingredient substitution 
We compiled web-scraped ingredient substitution data into a CSV file, and parsed that using the CSVParser we created in Sprint 1.
Existing ingredients in the database are compared against the substitution data, and possible substitutes are returned and automatically factored into the recipes recommended. 

Firebase Realtime Database 
JSON-structured, no-SQL database 
Stores user profiles - a userId, with the list of ingredients and a hashmap of recipe IDs (that they have saved) to ratings 
### Errors/Bugs:
“Saved” page is occasionally unable to properly render all saved recipes 
Sudden/random 500 Internal Server Errors are usually triggered by the API key running out of calls. We recommend making a new account with Spoonacular and using a new API key to continue running the project
Tests 
We mainly prioritized backend testing. We tested all handlers on the backend of the project ie: DatabaseHandler, RecipeHandler, and UserHandler. Focused on edge cases, and implemented random/ fuzz testing. 
Tested the SlopeOne algorithm with smaller datasets
### Challenges encountered: 
API key exhaustion
We frequently faced an issue of exhausting our API Key especially during fuzz testing. Spoonacular allows users to use a single API Key for a limited number of ties per day. We mostly found ourselves exhausting this key during fuzz testing because we had to make calls to Spoonacular multiple times. We have limited the number to 10 classes for fuzz testing for now until we get a more reliable API Key. 
The TestAPIUserHandler class makes multiple calls to the Spoonacular API, making it extremely expensive to run. The entire test suite requires around 5 API keys to complete. With more time, we would have used mocked data for testing instead, to prevent API key exhaustion.
### How to…
Run tests:
To run the tests on the back end, 
Run the server
Click on the green play button at the top of the testing class to run all the tests in the class, or run individual tests by clicking on the green play buttons on the right of each method.
NOTE! The API calls made by the TestAPIUserHandler cannot be supported with just one API key on spoonacular’s free plan. You will need around 5 API keys to support the running of the entire file, because the cost of each call in the test file exceeds the number of allotted calls in the free plan.
Build and run your program

Backend:
Add the com.google.api maven dependency to the project structure on intelliJ 
Run the server (green play button)
Frontend: 
cd into my-app, and then npm start 
Begin by logging in on the login page


