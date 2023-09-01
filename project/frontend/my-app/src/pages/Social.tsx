import React, { useState } from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline, TextField, Button, List, ListItem, ListItemText } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
const mockFriendList =[
    {name: 'Zyn', email: 'zyn_yee_ang@brown.edu'},
    {name: 'Nadya', email: 'nadya_tan@brown.edu'},
    {name: 'Lynda', email: 'lynda_umuhoza@brown.edu'},
]
export default function Social() {
    const [friendList, setFriendList] = useState([])
    const [inputVal, setInputVal] = useState('')
    // need to get the input from the thing
    function onClick(){

    }
    
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container justifyContent='center'>
                    <Grid item>
                        <Typography variant='h1'>Feeling Lucky?</Typography>
                        <Typography variant='h2'>Click below to get a list of recipes to try based on your saved recipe ratings</Typography>
                    </Grid>
                </Grid>
                <Grid container justifyContent='center'>
                    <Grid item xs={2}>
                        <Typography variant='h2'>Add a friend:</Typography>
                    </Grid>
                    <Grid item xs={2}>
                        <TextField
                                required
                                label ='Email'
                                // error
                                helperText='Enter a number'
                                id="outlined-adornment-amount"
                                // startAdornment={<InputAdornment position="start">$</InputAdornment>}
                                aria-label="budget-input"
                                onChange={(e) => setInputVal(e.target.value)}
                                // need an onChange event
                                />
                    </Grid>
                    <Grid item xs={2}>
                        {/* TODO: defensive copy here and onClick*/}
                        <Button variant='contained' 
                        // onClick={(e) => friendList.push(inputVal)}
                        >
                            Add friend</Button>
                    </Grid>
                </Grid>
                <Grid container>
                    <Grid item pl={3} xs={12}>
                        <Typography variant='h2'>Friends</Typography>
                    </Grid>
                    <Grid item pl={5} xs={12}>
                        {/* custom list component */}
                        <List>
                            
                            {mockFriendList.map((friend) => (
                            <ListItem key={friend.name}>
                                <ListItemText primary={friend.email}/>
                            </ListItem>))}
                        </List>
                    </Grid>
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}