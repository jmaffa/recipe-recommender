import React from 'react';
import { useState,  useRef, useEffect} from 'react'
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, ThemeProvider, Autocomplete, TextField, CssBaseline, List, ListItem, ListItemText, Button } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import IngredientList from '../components/IngredientList';
import {ingredientsJson} from '../data/ingredients';
import { getUID, retrieveIngredients, updateIngredients} from '../firebase';
import { ingredientsWSpaceJson } from '../data/ingredientsWspaces';
const mockIngredientList = [
    {name: 'cucumber'},
    {name: 'onion'},
    {name: 'wheat bread'}
]
const mockIngredientsOwned = [
    'cucumber', 'joseph', 'eating'
]
const mockPantryStaples = [
    {name: 'sugar'},
    {name: 'salt'},
    {name: 'pepper'},
    {name: 'flour'},
    {name: 'cayenne'},
    {name: 'rice'},
    {name: 'water'},
    {name: 'curry powder'},
    {name: 'pasta'},
    {name: 'garlic powder'}
]
export default function Home() {
    const [ingredients, setIngredients] = useState<string[]>([]);
    const userId = getUID();
    const [ingredRef, setIngredRef] = useState("");

    useEffect(()=>{
        retrieveIngredients(userId).then( 
        newIngredients =>{
                console.log(ingredients)
                setIngredients(String(newIngredients).split(","))
            } 
        )
    },[])

    return (
        <ThemeProvider theme={theme}>
        <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container aria-label='header-text'  textAlign='center' spacing={2}>
                    <Grid item xs={12}>
                        <Typography variant='h1'>Home</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant='h2'>With RecipeRecommender, you can put in ingredients that you own and find recipes that use it.</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant='h2'>Input ingredients to get started, click refreshIngredients, then click Recipes or Planner to find recipes!</Typography>
                    </Grid>
                </Grid>
                <Grid container aria-label='autocomplete-input-boxes'rowSpacing ={2} textAlign = 'center' padding={8}>
                    {/* Heading row*/}
                    <Grid item xs={12}>
                        <Typography variant='h6'>Input ingredients below</Typography>
                    </Grid>
                    {/* Autocomplete Row */}
                    <Grid item xs={12}>
                        {/* An interesting case could arise when you try to add the same ingredient */}
                        <Autocomplete
                            aria-label="ingredients-autocomplete"
                            clearOnEscape
                            options={ingredientsWSpaceJson.map((option: { name: any; }) => option.name)}
                            onChange={ (event: any, value: string|null) => 
                                {   
                                        setIngredRef(String(value))
                                        console.log(ingredRef)
                                }}
                            renderInput={(params) => <TextField 
                                {...params} 
                                label="Ingredients I have"
                                />}
                        />
                        <Button variant='contained' onClick={() => {
                            console.log("button pressed")
                            const userId = getUID()
                            console.log(userId)
                            retrieveIngredients(userId).then( 
                                ingredients => 
                                {console.log(ingredients)
                                    console.log(ingredRef)
                                    setIngredients(String(ingredients+","+ingredRef).split(","))
                                    updateIngredients(userId, String(ingredients+","+ingredRef))
                                }
                            )
                            // append the new input to the ingredients string
                            // write the new ingredients string into the database 
                            // set ingredients list string as the spliced new ingredients string 
                        }} 
                        >Add Ingredient</Button>  
                    </Grid>
                </Grid>
                
                <Grid container textAlign='center' paddingX={20}>
                    <Grid item xs={12} alignSelf='center'>
                        <IngredientList IngredientList={ingredients}></IngredientList>
                        <Button variant='contained' onClick={() => {
                            retrieveIngredients(userId).then( 
                                newIngredients =>{
                                    console.log(ingredients)
                                    setIngredients(String(newIngredients).split(","))
                                } 
                            )
                        }} 
                        >Refresh Ingredients </Button> 
                    </Grid>
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}

/*
<button className='login-button' type='button'
                        
                    // insert onClick function here 
                     onClick={() => {
                        console.log("button pressed")
                        const userId = getUID()
                        console.log(userId)
                        retrieveIngredients(userId).then( 
                            ingredients => 
                            console.log(ingredients)
                        )
                      }} 
                     > "Submit Button"</button>
*/