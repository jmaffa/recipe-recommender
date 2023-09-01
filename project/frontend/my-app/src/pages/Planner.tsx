import React, { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, CssBaseline, Checkbox, FormControlLabel, FormGroup, Autocomplete, TextField, FormControl, Button, TableContainer, TableHead, TableCell, Table, TableRow, TableBody } from '@mui/material';
import NavBar from '../components/NavBar';
import { ThemeProvider } from '@emotion/react';
import theme from '../styles/theme';
import { dietaryRestrictions } from './Recipes';
import DailyMealChecklistList from '../components/DailyMealChecklistList';
import { createIngredient, createRecipe, Recipe } from '../components/RecipeTable';
import { getUID } from '../firebase';
const daysToCook = [
    {day: 1},
    {day: 2},
    {day: 3},
    {day: 4},
    {day: 5},
    {day: 6},
    {day: 7}
]
const savedRecipes = [
    {name : 'Fruit Salad'},
    {name : 'Oatmeal'},
    {name : 'Steak Frites'}
]
interface Ingredient {
    name: string;
    owned: string;
    amount: number;
    price: number;
}

const mockRecipes = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/3GRJBKNQ/brownie-pudding","missedIngredients":["baking powder","cocoa","vanilla"],"vegetarian":true,"glutenFree":false,"pricePerServing":637.85,"id":636333.0,"vegan":false,"title":"Brownie Pudding","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/D2XW8CWD/pan-fried-fish","missedIngredients":["butter","capers","fish fillets"],"vegetarian":false,"glutenFree":false,"pricePerServing":129.03,"id":654403.0,"vegan":false,"title":"Pan Fried Fish","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZZ3MCQMD/baharat-panko-crusted-albacore-tuna","missedIngredients":["canned albacore tuna","coconut milk","panko","baharat"],"vegetarian":false,"glutenFree":false,"pricePerServing":120.66,"id":633386.0,"vegan":false,"title":"Baharat Panko Crusted Albacore Tuna","dishTypes":[]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/NDTLG63Q/corn-flan","missedIngredients":["corn kernels","eggs","fresh ricotta","pecorino romano cheese"],"vegetarian":true,"glutenFree":true,"pricePerServing":102.37,"id":640085.0,"vegan":false,"title":"Corn Flan Side Dish","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/LW8G8RTY/brandade-de-morue","missedIngredients":["garlic clove","salt cod","juice of lemon","bread"],"vegetarian":false,"glutenFree":false,"pricePerServing":551.7,"id":635890.0,"vegan":false,"title":"Brandade De Morue","dishTypes":[]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/2B5T467K/salad-with-apples-gorgonzola-and-walnuts","missedIngredients":["baguette","gorgonzola","green apple","lemon juice","salad mix"],"vegetarian":true,"glutenFree":false,"pricePerServing":279.01,"id":659015.0,"vegan":false,"title":"Salad With Apples, Gorgonzola and Walnuts","dishTypes":["salad"]},{"readyInMinutes":25.0,"sourceUrl":"http://www.foodista.com/recipe/W2NX45C6/spring-time-lunch-baked-salmon-with-parsley-sauce","missedIngredients":["dijon mustard","flat leaf parsley","juice of lemon","salmon filets","unsalted butter"],"vegetarian":false,"glutenFree":false,"pricePerServing":446.11,"id":661460.0,"vegan":false,"title":"Spring Time Lunch: Baked Salmon with Parsley Sauce","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/L4VDVMC6/cherry-date-nut-muffins","missedIngredients":["baking powder","splenda","egg","dates","cherries"],"vegetarian":true,"glutenFree":false,"pricePerServing":58.54,"id":637792.0,"vegan":false,"title":"Cherry, Date & Nut Muffins","dishTypes":["morning meal","brunch","breakfast"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/XF8JFZXN/kitchen-cabinet-coffee-cake","missedIngredients":["baking powder","blueberry jam","butter","eggs","ground cinnamon"],"vegetarian":true,"glutenFree":false,"pricePerServing":71.71,"id":648939.0,"vegan":false,"title":"Kitchen Cabinet Coffee Cake","dishTypes":["side dish"]},{"readyInMinutes":660.0,"sourceUrl":"http://www.pinkwhen.com/easy-baked-chicken-recipe/","missedIngredients":["chicken broth","paprika","rosemary","white onion","whole chicken"],"vegetarian":false,"glutenFree":true,"pricePerServing":173.67,"id":710766.0,"vegan":false,"title":"Easy Baked Chicken","dishTypes":["lunch","main course","main dish","dinner"]}],"ingredients":"pepper,salt,milk,walnuts,lemon"}`
// const mockBreakfast  =`{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
// const mockLunch = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
// const mockDinner = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
// const mockDessert = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
// const mockSnack = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
function readRecipe(recipe : any, recipeList: Recipe[], setRecipeList: React.Dispatch<React.SetStateAction<Recipe[]>>){
        var name = recipe.title
        var link = recipe.sourceUrl
        var time = recipe.readyInMinutes
        var price = recipe.pricePerServing/100
        var actualIngredientList = []
        for(let i = 0; i <recipe.missedIngredients.length; i++){
            actualIngredientList.push(createIngredient(recipe.missedIngredients[i]))
        }
        var meal = recipe.dishTypes
        var save = false 
        var vegetarian = recipe.vegetarian
        var vegan = recipe.vegan 
        var id = recipe.id 
        var glutenFree = recipe.glutenFree
                            
        var newRecipe : Recipe = createRecipe(name, link, time, price, actualIngredientList, meal, save, vegetarian, vegan, glutenFree, id, 0, 'noMeal')
        
        recipeList.push(newRecipe)
        setRecipeList(recipeList)
}
function readAPIForAllMeals(mockResult: string, recipeList: Recipe[], setRecipeList: React.Dispatch<React.SetStateAction<Recipe[]>>, setLoaded : React.Dispatch<React.SetStateAction<boolean>>){
    // console.log(userID)
    const obj = JSON.parse(mockResult)
    console.log(obj)
    var newRecipeList = recipeList.slice()
    for (let index=0; index < obj.recipes.length; index++){
        var recipe = obj.recipes[index]
        readRecipe(recipe,newRecipeList, setRecipeList)
    }
    setLoaded(true)
    

}

export default function Planner() {
    const userID = getUID()
    // load all the recipes at beginning
    useEffect(() =>{
        // fetch(`http://localhost:3232/recipeIngredients?userID=`+userID)
        // COMMENT IN TO WORK WITH API
        fetch(`http://localhost:3232/recipeIngredients?userID=`+userID)
        .then(response => response.json())
        .then(json => JSON.stringify(json))
        .then(stringJson=> {
            readAPIForAllMeals(stringJson, recipeList, setRecipeList, setLoaded)
        })
        // MOCK Comment in to work with mocks!!!
        // readAPIForAllMeals(mockRecipes, recipeList, setRecipeList, setLoaded)
    }, [])
    const [open, setOpen] = useState(false)
    const [loaded, setLoaded] = useState(false)
    const [diet, setDiet] = useState<string | null>('')
    const [recipeList, setRecipeList] = useState<Recipe[]>([])
    const [daysToPlan, setDaysToPlan] = useState<number>(0)
    const [budget, setBudget] = useState(0)
    const [shoppingList, setShoppingList] = useState<Ingredient[]>([])

    // const [dayIdToMeals, setDayIdToMeals] = useState<Map<number,string[]>>(new Map<number,string[]>())

    // function adjustMap(meal:string, id:number) {
    //     // const newMap : Map<number,string[]> = new Map(dayIdToMeals)
  
    //     if (dayIdToMeals.get(id)?.includes(meal)){
    //         let indexOfItem : number | undefined= dayIdToMeals.get(id)?.indexOf(meal)
    //         // props.map.get(props.id)?.splice(indexOfItem!,1)
    //         dayIdToMeals.get(id)?.splice(indexOfItem!,1)
    //         console.log('removing item')
    //         // console.log(props.map)
    //     }
    //     else{
    //         // props.map.get(props.id)?.push(meal)
    //         dayIdToMeals.get(id)?.push(meal)
    //         console.log('adding item')
    //         // console.log(props.map)
    //     }
    //     // let ourMap = newMap
    //     setDayIdToMeals(dayIdToMeals)
    //     console.log(dayIdToMeals)
    //     // setMealMap(newMap)
    //     // console.log(mealsMap)
        
  
    // }
    

    return (
        <ThemeProvider theme ={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container aria-label='header-text' textAlign='center' justifyContent='center' spacing={2}>
                    <Grid item xs={12}>
                        <Typography variant='h1'>Weekly Planner</Typography>
                    </Grid>
                    <Grid item>
                        <FormGroup>
                            {/* this should not just be a default checked, people shouldn't interact with it */}
                            <FormControlLabel control={<Checkbox checked={loaded}/>} label = "Ingredients loaded"/>
                        </FormGroup>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant='h2'>Once the checkbox is checked, fill out the fields, then click Get Meal Plan!</Typography>
                    </Grid>
                </Grid>
                
                <Grid container aria-label='autocompletes' textAlign='center' justifyContent='center' spacing={2}>
                    <Grid item xs={3}>
                        <Autocomplete
                                aria-label="days-autocomplete"
                                clearOnEscape
                                // needed to override these to handle numbers
                                isOptionEqualToValue = {(option: number, value: number) => option===value}
                                getOptionLabel = {(option: number) => option.toString()}
                                value={daysToPlan} 
                                onChange={(e: any, value : number | null) => {
                                    setDaysToPlan(value!)
                                    console.log(daysToPlan)
                                }
                                }
                                options={daysToCook.map((option) => option.day)}
                                // options={mealsToCook.map((option) => option.meal)}
                                renderInput={(params) => <TextField {...params} label="Days I want to cook" />}
                        /> 
                    </Grid>
                    
                    <Grid item xs={4}>
                        <Autocomplete
                                aria-label="dietary-restriction-autocomplete"
                                clearOnEscape
                                onChange={(e: any, val : string | null) => {
                                    setDiet(val)
                                }}
                                options={dietaryRestrictions.map((option) => option.diet)}
                                renderInput={(params) => <TextField {...params} label="Dietary Restrictions" />}
                        /> 
                    </Grid>
                    <Grid item xs={4}>
                        <FormControl fullWidth>
                            <TextField
                                required
                                label ='Budget for new ingredients&nbsp;($)'
                                // error
                                helperText='Enter a number'
                                id="outlined-adornment-amount"
                                onChange= {(e: any) => {
                                    setBudget(e.target.value)
                                }}
                                // startAdornment={<InputAdornment position="start">$</InputAdornment>}
                                aria-label="budget-input"
                            />
                        </FormControl>
                    </Grid>
                    <Grid item xs={4}>
                    </Grid>
                </Grid>
                
                <Grid container aria-label='day-checkboxes'>
                    {/* need to pass the recipelist here too */}
                    <DailyMealChecklistList recipeList={recipeList} numDays= {daysToPlan} diet={diet!} budget={budget}/>
                </Grid>
                {/*U USE THIS AS THE RENDERING THING */}

            </Box>
        </ThemeProvider>
       
    )
}