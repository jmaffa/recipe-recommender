import React from 'react';
import {
    BrowserRouter as Router,
    Route,
    Link,
  } from "react-router-dom";
  import { AppBar, Grid, Box, Typography } from '@mui/material';
/**
 * This is the navigation bar at the top. It contains links to all of the page components
 * that a user can use to get around the web app.
 * 
 */
export default function NavBar(){
    
    return (
            <Box color='black'>
                <AppBar sx={{height: 40}} position='static' aria-label='navbar'>
                    <Box display='flex' alignItems='center' justifyContent='space-around'>
                        <Grid container direction='row' justifyContent='space-around' alignItems='center' maxWidth={'100%'}>
                            <Link to='/login' aria-label='link to login'>
                               <Typography variant='body1'>Login</Typography>
                            </Link>
                            <Link to='/' aria-label='link to home page'>
                                <Typography variant='body1'>Home</Typography>
                            </Link>
                            <Link to='/recipes' aria-label='link to recipe retrieval page'>
                                <Typography variant='body1'>Recipes</Typography>
                            </Link>
                            <Link to='/planner' aria-label='link to planner page'>
                                <Typography variant='body1'>Planner</Typography>
                            </Link>
                            <Link to='/waste' aria-label='ink to waste information page'>
                                <Typography variant='body1'>Waste</Typography>
                            </Link>
                            <Link to='/saved' aria-label='link to saved recipes page'>
                                <Typography variant='body1'>Saved</Typography>
                            </Link>
                            <Link to='/lucky' aria-label='link to feeling lucky page'>
                                <Typography variant='body1'>Feeling Lucky?</Typography>
                            </Link>
                        </Grid>
                    </Box>
                </AppBar>
            </Box>
    )
}