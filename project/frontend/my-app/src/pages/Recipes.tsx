import React, { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline, Autocomplete, FormGroup, FormControlLabel, Checkbox, TextField, InputLabel, FormControl, InputAdornment, OutlinedInput, Button } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import RecipeTable, { createIngredient, createRecipe, Recipe, mockRows } from '../components/RecipeTable';
import { getUID } from '../firebase';
/**
 * TODO!!! Need to make it only push to the thing once!
 */
const mealsToCook = [
    {meal: 'breakfast'},
    {meal: 'lunch'},
    {meal: 'dinner'},
    {meal: 'snack'},
    {meal: 'dessert'},
    {meal: 'side dish'},
    {meal: 'surprise me!'}
]
export const dietaryRestrictions = [
    {diet: 'Vegan'},
    {diet: 'Vegetarian'},
    {diet: 'Gluten free'},
    {diet: 'None'},
]


export const mockedResult = `{"result":"success","recipes":[{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZMNTRKRQ/jalapeno-corn-cakes-with-avocado-salsa","missedIngredients":["yellow corn meal","baking powder","fresh corn","jalapeno","milk","avocados","green onions","tomatoes","garlic","cilantro","juice of lime"],"vegetarian":true,"glutenFree":false,"pricePerServing":20.11,"id":648345.0,"vegan":false,"title":"Jalapeño Corn Cakes With Avocado Salsa","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/HY8GLB3V/chewy-gingersnaps","missedIngredients":["baking soda","cinnamon","ginger","molasses"],"vegetarian":true,"glutenFree":false,"pricePerServing":45.79,"id":637824.0,"vegan":false,"title":"Chewy Gingersnaps","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/ZD6LKFHL/pecan-topped-sweet-potato-casserole","missedIngredients":["sweet potatoes","ground cinnamon","ground ginger","nutmeg","pecans"],"vegetarian":true,"glutenFree":true,"pricePerServing":103.68,"id":655534.0,"vegan":false,"title":"Pecan Topped Sweet Potato Casserole","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/SDLRXKKC/kaiser-rolls","missedIngredients":["instant yeast"],"vegetarian":true,"glutenFree":false,"pricePerServing":48.26,"id":648704.0,"vegan":false,"title":"Kaiser Rolls","dishTypes":[]},{"readyInMinutes":50.0,"sourceUrl":"https://spoonacular.com/site-1522954610439","missedIngredients":["milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":137.16,"id":994607.0,"vegan":false,"title":"Kaiserschmarrn","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":70.0,"sourceUrl":"http://www.pinkwhen.com/cinnamon-nut-squares/","missedIngredients":["cinnamon","pecans"],"vegetarian":true,"glutenFree":false,"pricePerServing":26.01,"id":715389.0,"vegan":false,"title":"Cinnamon Nut Squares","dishTypes":["antipasti","starter","snack","appetizer","antipasto","hor d'oeuvre"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/MM3F6P6J/cinnamon-twists","missedIngredients":["active yeast","milk"],"vegetarian":true,"glutenFree":true,"pricePerServing":23.61,"id":639492.0,"vegan":false,"title":"Cinnamon Twists","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/VFNBYSJR/apricot-slice","missedIngredients":["dried apricot","vanilla extract"],"vegetarian":true,"glutenFree":false,"pricePerServing":27.52,"id":632678.0,"vegan":false,"title":"Apricot Slice","dishTypes":["side dish"]},{"readyInMinutes":45.0,"sourceUrl":"http://www.foodista.com/recipe/KXSKHL3G/almond-crusted-torte","missedIngredients":["almond extract","almonds"],"vegetarian":true,"glutenFree":false,"pricePerServing":513.17,"id":632125.0,"vegan":false,"title":"Almond Crusted Torte","dishTypes":["lunch","main course","main dish","dinner"]},{"readyInMinutes":45.0,"sourceUrl":"https://www.foodista.com/recipe/JVHM6NJ5/amazingly-fluffy-waffles","missedIngredients":["baking powder","milk"],"vegetarian":true,"glutenFree":false,"pricePerServing":32.97,"id":632305.0,"vegan":false,"title":"Amazingly Fluffy Waffles","dishTypes":["morning meal","brunch","breakfast"]}],"ingredients":"butter,sugar,salt,eggs,pepper"}`
export function readAPIfromMock(mockResult : string, recipeList : Recipe[], setRecipeList :React.Dispatch<React.SetStateAction<Recipe[]>>, setLoaded : React.Dispatch<React.SetStateAction<boolean>>){
        const obj = JSON.parse(mockResult)
        console.log(obj)
        var newRecipeList = recipeList.slice()
        for (let index = 0; index < obj.recipes.length; index++) {

            var recipe = obj.recipes[index];
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
            var day = 0
                                
            var newRecipe : Recipe = createRecipe(name, link, time, price, actualIngredientList, meal, save, vegetarian, vegan, glutenFree, id, day, 'noMeal')
            
            newRecipeList.push(newRecipe)
            setRecipeList(newRecipeList)
           
            }
        setLoaded(true)

}
export default function Recipes() {
    const [open, setOpen] = useState<boolean>(false)
    const [meal, setMeal] = useState<string | null>('')
    const [diet, setDiet] = useState<string | null>('')
    const [budget, setBudget] = useState(0)
    const [useSaved, setUseSaved] = useState(false)
    const [recipeList, setRecipeList] = useState<Recipe[]>([])
    const [loaded, setLoaded] = useState<boolean>(false)

    const userID = getUID()
    const mockuserID = `ZYlw1AcclrbfZCfkq0j8ixQQWVM2`
    // const userID = `ZYlw1AcclrbfZCfkq0j8ixQQWVM2`
    useEffect(() =>{
        // COMMENT IN TO WORK WITH API
        fetch(`http://localhost:3232/recipeIngredients?userID=`+userID)
        .then(response => response.json())
        .then(json => JSON.stringify(json))
        .then(stringJson=> {
            readAPIfromMock(stringJson, recipeList, setRecipeList, setLoaded)
        })
        // MOCK Comment in to work with mocks!!!
        // readAPIfromMock(mockedResult,recipeList, setRecipeList, setLoaded)
    }, [])
    
    
    
    return (
        // somehow only allow this page if ingredients are already loaded
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                {/* This is the header text and input ingredient marker*/}
                <Grid container aria-label='header-text' textAlign='center' justifyContent='center' spacing={2}>
                    <Grid item xs={12}>
                        <Typography variant='h1'>Recipe Recommender!</Typography>
                    </Grid>
                    {/* TODO BUDGET THING... */}
                    <Grid item>
                    </Grid>
                    <Grid item >
                        <FormGroup>
                            <FormControlLabel control={<Checkbox disabled checked={loaded}/>} label = "Possible recipes loaded?"/>
                        </FormGroup>
                    </Grid>
                </Grid>

                {/** This is the autocomplete boxes */}
                <Grid container aria-label='header-text' textAlign='center' justifyContent='center' spacing={2}>
                    <Grid item xs={3}>
                        <Autocomplete
                                aria-label="meals-autocomplete"
                                clearOnEscape
                                value={meal}
                                onChange={(e: any, value : string | null) => {
                                    setMeal(value)
                                    // console.log(meal)
                                }

                            }
                                options={mealsToCook.map((option) => option.meal)}
                                renderInput={(params) => <TextField {...params} label="Meal to Cook" />}
                        /> 
                    </Grid>
                    <Grid item xs={3}>
                        <Autocomplete
                                aria-label="dietary-restriction-autocomplete"
                                clearOnEscape
                                value={diet}
                                onChange={(e: any, val : string | null) => {
                                    setDiet(val)
                                }}
                                options={dietaryRestrictions.map((option) => option.diet)}
                                renderInput={(params) => <TextField {...params} label="Dietary Restrictions I have" />}
                        /> 
                    </Grid>
                    {/* Budget input box */}
                    <Grid item xs={3}>
                        <FormControl fullWidth>
                            <TextField
                                required
                                onChange= {(e: any) => {
                                    setBudget(e.target.value)
                                }}
                                label ='Budget for new ingredients&nbsp;($)'
                                helperText='Enter a number'
                                id="outlined-adornment-amount"
                                aria-label="budget-input"
                                
                            />
                        </FormControl>
                    </Grid>
                    {/* New Recipe checkbox */}
                    <Grid item xs={3}>
                        <FormGroup>
                            <FormControlLabel control={<Checkbox checked={useSaved} onChange={() => setUseSaved(!useSaved)}/>} label = "New Recipe?"/>
                        </FormGroup>
                    </Grid>
                    {/** Get Recipes button */}
                    <Grid item>
                        <Button variant="contained" onClick={() => {
                            console.log(recipeList)
                            setOpen(!open)
                        }}>Get Recipes</Button>
                    </Grid>
                </Grid>
                {/* Recipe Table */}
                <Grid container>
                    {             
                    open && loaded && (<RecipeTable useIngredients={true} meal={meal} diet={diet} budget={budget} useSaved={useSaved} rows={recipeList}/>)
                    }   
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}