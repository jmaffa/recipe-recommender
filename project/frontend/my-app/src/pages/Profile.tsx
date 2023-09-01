import React from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, ThemeProvider, CssBaseline } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
export default function Profile() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container>
                    <Grid item>
                        <Typography variant='h1'>This is the Profile page</Typography>
                    </Grid>
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}