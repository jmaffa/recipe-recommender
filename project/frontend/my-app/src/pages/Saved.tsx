import React, { useState, useEffect } from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import{getUID, retrieveRatingsMap} from '../firebase';
import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
import SavedRow from '../components/SavedRow';

interface SavedRecipe{
  id : string,
  name: string,
  link: string,
  time: number,
  price: number, 
}

function createData(
  id : string,
  name: string,
  link: string,
  time: number, // this might want to be a string im rly not sure, rn it is in hours
  price: number,
  // similarity: number, // um @Zyn?
) : SavedRecipe {
  return {
    id, 
    name,
    link,
    time,
    price 
  };
}

// function that takes in recipeID and returns an object of createData 

function getCreatedData(recipeID : string) : Promise<SavedRecipe> {
  const newURL = 'http://localhost:3232/recipe?recipeID='+recipeID
  return new Promise((resolve) =>{
    return fetch(newURL)
    .then(response => response.json())
    .then(nextResponse =>{
      const responseDetails = JSON.parse(JSON.stringify(nextResponse))
      const recipeDetails = responseDetails.recipes
      const name = (recipeDetails.title).toString()
      const link = (recipeDetails.sourceUrl).toString()
      const time = Number(recipeDetails.readyInMinutes)
      const price = Number(recipeDetails.pricePerServing)/100
      resolve(createData(recipeID, name, link, time, price))
    })
  })
}

// need to be able to write recipe id and rating
export default function Saved() {
  const userId = getUID();
  const [rows, setRows] = useState<SavedRecipe[]>([])
  const [recipeIDs, setRecipeIDs] = useState<string[]>([])

  useEffect(() => {
                  
    var userMap = new Map<string, string>()
    retrieveRatingsMap(userId).then( 
      ratingsmap =>{
          var stringMap = String(ratingsmap).replaceAll("{", "").replaceAll("}", "")
          console.log(stringMap)
          var stringArr = stringMap.split(",")
          for (let i=0; i<stringArr.length; i++){
            var ratingArr = stringArr[i].split(":")
            userMap.set(ratingArr[0].replaceAll('"', '').toString(), ratingArr[1])
          }
          var newRecipeIDs = Array.from(userMap.keys())
          var newRows : SavedRecipe[] = []
                    for(let i=0; i<newRecipeIDs.length; i++){
                      getCreatedData(newRecipeIDs[i]).then(
                        createdData => {
                          newRows.push(createdData)
                        }
                      ).then(()=> {
                        setRows(newRows)
                        console.log(rows)
                      })} }
    )

                }, [] );

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container justifyContent='center'>
                    <Grid item>
                        <Typography variant='h1'>Saved Recipes</Typography>
                    </Grid>
                </Grid>
                <Grid container>
                    {/* this needs to have can make checkboxes, not save checkboxes */}
                    <TableContainer component={Paper}>
                        <Table aria-label="collapsible table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Meal</TableCell>
                                <TableCell align="right">Time&nbsp;(mins)</TableCell>
                                {/* <TableCell align="right">Difficulty&nbsp;</TableCell> */}
                                <TableCell align="right">Price&nbsp;($)</TableCell>
                                <TableCell align="right">Unsave?</TableCell>
                                <TableCell align='right'>Rating</TableCell>
                                <TableCell align='right'></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row, index) => (
                            <SavedRow key={index} row={row} />
                            ))}
                        </TableBody>
                        </Table>
                </TableContainer> 
                <Button variant ='contained' onClick={()=>{
                  
                  var userMap = new Map<string, string>()
                  retrieveRatingsMap(userId).then( 
                    ratingsmap =>{
                        var stringMap = String(ratingsmap).replaceAll("{", "").replaceAll("}", "")
                        console.log(stringMap)
                        var stringArr = stringMap.split(",")
                        for (let i=0; i<stringArr.length; i++){
                          var ratingArr = stringArr[i].split(":")
                          userMap.set(ratingArr[0].replaceAll('"', '').toString(), ratingArr[1])
                        }
                        var newRecipeIDs = Array.from(userMap.keys())
                        var newRows : SavedRecipe[] = []
                                  for(let i=0; i<newRecipeIDs.length; i++){
                                    getCreatedData(newRecipeIDs[i]).then(
                                      createdData => {
                                        newRows.push(createdData)
                                      }
                                    ).then(()=> {
                                      setRows(newRows)
                                      console.log(rows)
                                    })} }
                  )
              
                              }}>Refresh</Button>
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
            }