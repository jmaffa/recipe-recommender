import React from 'react';
import {
  BrowserRouter as Router,
  createBrowserRouter,
  RouterProvider,
  Route,
  Link
} from "react-router-dom";
import { Grid, Box, Typography, ThemeProvider, CssBaseline, } from '@mui/material';
import NavBar from '../components/NavBar';
import theme from '../styles/theme';
export default function Waste() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Box>
                <NavBar/>
                <Grid container p={5} spacing={5} aria-label='header-text' textAlign='center' justifyContent='center'>
                    <Grid item xs={12}>
                        <Typography variant='h2'>Hey there! :) We’re so glad that you’ve decided to give our app a try, and more importantly, that you’re interested in how to handle food waste in a sustainable way!</Typography>
                    </Grid>
                </Grid>
                <Grid container textAlign='center' spacing={5}>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Why is food waste a problem?</Typography>
                        <Typography px={4} variant='body1'>Food waste is easily one of the top offenders for global pollution. Almost one third of all food produced for human consumption is wasted every year, which contributes to about 8% of the world’s greenhouse gasses. Precious natural resources are also wasted along with it, as the production of food is extremely resource-intensive! </Typography>
                        <Typography px={4} variant='body1'>Of course, not all of this waste happens at home. A lot of food waste happens in the production and transportation process. But where you can make a difference is in the handling and storage of food products at home.Cutting down on food waste isn’t just good for the planet, but for your wallet too! Studies show that you can save an average of $746 per person per year by cutting down on food waste. For college students, that’s almost $3000 over the course of a 4-year education!</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h1'>What can we do?</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Meal Planning</Typography>
                        <Typography px={4} variant='body1'>Make a list of the meals you’d like to prepare for the whole week, including which nights you’ll be eating out and plan which ingredients (and which quantities) you’ll need. Have a look in your fridge or pantry to see what foods you still have and what meals you can make with your leftovers.</Typography>

                        <Typography px={4} variant='body1'>Once you’re out shopping, stick to the ingredients on your list and avoid impulse buys. Give ‘ugly produce’ a chance too, as fruits and vegetables with bumps or irregularities are perfectly fine to eat and are usually sold at discounted prices. And be cautious of bulk offers! Be realistic: if it’s not going to get eaten before its expiration date, don’t buy it. You’ll just end up throwing it out.</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Get creative with scraps</Typography>
                        <Typography px={4} variant='body1'>Just because it is not fit for consumption doesn’t mean it has to go in the bin! There are lots of ways that you can make use of your food scraps:</Typography>
                        <Typography px={6} variant='body1'>Make your own broth. Use vegetable or meat scraps (including bones) to make your own broth to be stored in the fridge or freezer. It will be delicious, and put store-bought broths to shame.</Typography>
                        <Typography px={6} variant='body1'>Make soup. Limp or wilted veggies can be revived in cold water or chopped down and processed into a soup. Spinach and leek greens are especially delicious!</Typography>
                        <Typography px={6} variant='body1'>Never throw away bread. Stale bread is so versatile and can be turned into croutons, breadcrumbs, French toast or a scrumptious bread pudding.</Typography>
                        <Typography px={6} variant='body1'>Embrace the power of citrus. Citrus peels (like lemon and orange) are great for flavoring water, sugar or marinades, and can also be added to vinegar for a natural, eco-friendly cleaning product.</Typography>
                        <Typography px={6} variant='body1'>Have a spa day. Browned avocados can be blended and applied to the hair for an indulgent, moisturizing hair mask, and cucumber peels are great for soothing itchy skin. Apply them directly to your skin or add them to your bathwater for an at-home spa retreat.</Typography>
                        <Typography px={6} variant='body1'>Grow your own produce. Lots of pits, seeds and veggie scraps (like onion, lettuce, avocado, ginger, lemongrass and potatoes) can be used to regrow new veggies.</Typography>                    </Grid>
                    
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Learn how to read expiration dates</Typography>
                        
                        <Typography px={4} variant='body1'>Expiration dates can be misleading and cause a lot of confusion. In fact, expiration labels contribute to almost 20% of global food waste. A whopping 84% of consumers discard food on or near the expiration date on the packaging, while in most cases it’s still perfectly fine for consumption. </Typography>
                        <Typography px={4} variant='body1'>While ‘use by’ dates are important to follow, ‘best before’ dates usually allow for a wide margin of error – so use your common sense and your other senses to assess whether the item has actually expired. Here’s a great {<a href='https://hospitalityinsights.ehl.edu/how-manage-food-waste-home' target='_blank' rel="noreferrer">resource</a>} to learn how to read expiration dates. </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Start a compost</Typography>
                        <Typography px={4} variant='body1'>Whatever can’t be reused can almost definitely be recycled. Most of the food waste you’re producing can be composted, giving it a second life, and preventing it from producing methane in landfills. You don’t need a garden or outdoor space to start your own compost bin! Follow this easy {<a href='https://www.gardena.com/int/garden-life/garden-magazine/sustainable-and-practical-composting-on-the-balcony/' target='_blank' rel="noreferrer">guide</a>} on how to compost on an apartment balcony. And if you don’t have your own compost bin, save your scraps and discard them in your neighborhood compost bin.</Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography px={2} variant='h2'>Adapted from {<a target='_blank' href='https://hospitalityinsights.ehl.edu/how-manage-food-waste-home' rel="noreferrer">Hospitality Insights</a>}</Typography>
                    </Grid>
                    
                </Grid>
            </Box>
        </ThemeProvider>
       
    )
}