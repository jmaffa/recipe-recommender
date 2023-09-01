import React from 'react';
//import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { AppBar, Grid, Box, Typography, Button } from '@mui/material';
import Home from './pages/Home';
import { ThemeProvider } from '@mui/material/styles';
import { CssBaseline } from '@mui/material';
import NavBar from './components/NavBar';
import Login from './pages/Login'
import theme from './styles/theme';

export function App() {
  return (

  
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <NavBar/> 
    </ThemeProvider>
  );
 }

export default App;
