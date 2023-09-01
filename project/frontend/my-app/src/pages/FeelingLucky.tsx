import React, { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline, TextField, Button, List, ListItem, ListItemText, Checkbox, FormControlLabel, FormGroup } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import RecipeTable, { createRecipe, Recipe, mockRows } from '../components/RecipeTable';
import { mockedResult } from './Recipes';
import { getUID } from '../firebase';
const mockRecResult = `{"result":"success","recipes":[{"sourceUrl":"https://www.foodista.com/recipe/ZVXPQHB5/blue-cheese-burgers","readyInMinutes":45.0,"vegetarian":false,"glutenFree":false,"pricePerServing":450.87,"id":635350.0,"vegan":false,"title":"Blue Cheese Burgers","dishTypes":["lunch","main course","main dish","dinner"]},{"sourceUrl":"http://www.foodista.com/recipe/G8JXNCD8/pasta-with-italian-sausage","readyInMinutes":45.0,"vegetarian":false,"glutenFree":false,"pricePerServing":245.44,"id":654928.0,"vegan":false,"title":"Pasta With Italian Sausage","dishTypes":["lunch","main course","main dish","dinner"]},{"sourceUrl":"http://www.foodista.com/recipe/DF3VCPLF/tomato-cutlets","readyInMinutes":45.0,"vegetarian":false,"glutenFree":false,"pricePerServing":313.78,"id":663588.0,"vegan":false,"title":"Tomato Cutlets","dishTypes":["lunch","main course","main dish","dinner"]},{"sourceUrl":"http://www.foodista.com/recipe/YCKK77YK/chicken-burrito-by-bing","readyInMinutes":45.0,"vegetarian":false,"glutenFree":false,"pricePerServing":300.17,"id":637999.0,"vegan":false,"title":"Chicken Burritos","dishTypes":["lunch","main course","main dish","dinner"]},{"sourceUrl":"http://www.foodista.com/recipe/FB3GPT43/rice-pilaf","readyInMinutes":45.0,"vegetarian":false,"glutenFree":false,"pricePerServing":72.18,"id":658277.0,"vegan":false,"title":"Rice Pilaf","dishTypes":["side dish"]}]}`
function readAPIfromMock(mockResult : string, recipeList : Recipe[], setRecipeList :React.Dispatch<React.SetStateAction<Recipe[]>>, setLoaded : React.Dispatch<React.SetStateAction<boolean>>){
    const obj = JSON.parse(mockResult)
    console.log(obj)
    var newRecipeList = recipeList.slice()
    for (let index = 0; index < obj.recipes.length; index++) {

        var recipe = obj.recipes[index];
        var name = recipe.title
        var link = recipe.sourceUrl
        var time = recipe.readyInMinutes
        var meal = recipe.dishTypes
        var save = false //TODO WITH THE SAVE FUNCTIONALITY!
        var price = recipe.pricePerServing/100
        var vegetarian = recipe.vegetarian
        var vegan = recipe.vegan 
        var id = recipe.id 
        var glutenFree = recipe.glutenFree
                            
        var newRecipe : Recipe = createRecipe(name, link, time, price, [], meal, save, vegetarian, vegan, glutenFree, id,-1, 'noMeal')
        
        newRecipeList.push(newRecipe)
        setRecipeList(newRecipeList)
       
        }
    setLoaded(true)
}


export default function FeelingLucky() {
    const [inputVal, setInputVal] = useState('')
    const [open, setOpen] = useState(false)
    const [recList, setRecList] = useState<Recipe[]>([])
    const [loaded, setLoaded] = useState<boolean>(false)
    const userID = getUID()
    useEffect(() =>{
        // fetch(`http://localhost:3232/recipeIngredients?userID=`+userID)
        // COMMENT IN TO WORK WITH API
        fetch(`http://localhost:3232/getRatingRec?userID=`+userID)
        .then(response => response.json())
        .then(json => JSON.stringify(json))
        .then(stringJson=> {
            readAPIfromMock(stringJson, recList, setRecList, setLoaded)
        })
        // MOCK Comment in to work with mocks!!!
        // readAPIfromMock(mockRecResult,recList, setRecList, setLoaded)
    }, [])
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container textAlign='center' justifyContent='center'>
                    <Grid item xs={12}>
                        <Typography variant='h1'>Feeling Lucky?</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant='h2'>Click below to get a list of recipes to try based on your saved recipe ratings</Typography>
                    </Grid>
                    <Grid item >
                        <FormGroup>
                            <FormControlLabel control={<Checkbox disabled checked={loaded}/>} label = "Possible recipes loaded?"/>
                        </FormGroup>
                    </Grid>
                </Grid>
                
                
                <Grid container justifyContent='center'>
                    <Grid item>
                        <Button variant='contained' onClick={(e: any)=> setOpen(!open) }>Feeling Lucky</Button>
                    </Grid>
                </Grid>
                <Grid container>
                    {open && loaded && <RecipeTable useIngredients={false} meal={'any'} diet={'any'} budget={1000000000} useSaved={false} rows={recList}/>}
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}
