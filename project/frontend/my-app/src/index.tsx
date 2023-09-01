import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Planner from './pages/Planner';
import Recipes from './pages/Recipes';
import Waste from './pages/Waste';
import Social from './pages/Social';
import Saved from './pages/Saved';
import FeelingLucky from './pages/FeelingLucky';


const router = createBrowserRouter(
  createRoutesFromElements(
    <Route>
      <Route path="/" element={<Home />}/>
      <Route path='login' element={<Login />}/>
      <Route path="recipes" element={<Recipes />}/>
      <Route path="planner" element={<Planner />}/>
      <Route path="waste" element={<Waste />}/>
      <Route path="saved" element={<Saved />}/>
      {/* <Route path="social" element={<Social />}/> */}
      <Route path="lucky" element={<FeelingLucky/>}/>
    </Route>
  )
);
ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
