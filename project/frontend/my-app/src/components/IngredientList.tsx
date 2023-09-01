import React from 'react';

import { Grid, ListItem, List, ListItemText, Divider } from '@mui/material';
import IngredientInputted from './IngredientInputted';

const mockIngredients = ["sugar", "butter", "flour"]

/**
 * This list is shown on the Home page to display the ingredients that a user has input from
 * the input box and is saved to their database.
 */
export default function IngredientList({IngredientList} : {IngredientList : string[]} ) {

    return(
        <Grid container boxShadow={5} >
            <Grid item xs={12} >
                <List aria-label='ingredient list'>
                    <ListItem>
                        <ListItemText primary= 'Ingredient'></ListItemText>
                    </ListItem>
                    <Divider/>
                    {
                        IngredientList.map((ingredient) => 
                        <IngredientInputted ingredient={ingredient}/>
                        )
                    }
                </List>
            </Grid>
        </Grid>
        
    )
}