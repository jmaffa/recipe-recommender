import React, { useEffect, useState } from "react";
import { auth, signInWithGoogle} from '../firebase';
import { useAuthState } from "react-firebase-hooks/auth";
import GoogleIcon from '@mui/icons-material/Google';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link,
  useNavigate,
  Navigate
} from "react-router-dom";

import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline, Button, IconButton } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';

const TEXT_LOGIN_BUTTON = "Login with Google"



export default function Login() {
    const navigate = useNavigate();
    return (
        <ThemeProvider theme={theme}>
        <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container spacing={5} justifyContent='center' textAlign='center'>
                    <Grid item xs={12}>
                        <Typography variant='h1'>Welcome to Recipe Recommender!</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant='h2'>Login with Google to get started!</Typography>
                    </Grid>
                    {/* TODO: This is kind of small... */}
                    <Grid item alignSelf='center'>
                        <IconButton color='secondary' size='large' aria-label='login-button' className='login-button' onClick={
                            () => {
                                console.log("button pressed")
                                signInWithGoogle().then(
                                    () => {
                                        navigate("/")
                                    }
                                )   
                              }
                        }><GoogleIcon/></IconButton>
                    </Grid>
                </Grid>
            </Box>
        </ThemeProvider>

    )
}

