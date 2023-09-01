import { TableRow, TableCell, IconButton, Box, Collapse, Table, TableBody, TableHead, Typography, Checkbox, Link, Button } from "@mui/material";
import React from "react";
import {getUID, editSpecificRating, unsaveRecipe} from '../firebase';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { Recipe } from "./RecipeTable";

// adapted from Row in Material UI table documentation
export default function RecipeRow(props : {useIngredients:boolean, row : Recipe, day: number, useMeal: boolean}){
    const userId = getUID();
    const {row} = props
    const [open, setOpen] = React.useState<boolean>(false);

    function saveRecipe(userId : string, recipeId : string){
      editSpecificRating(userId, recipeId, 0)
    }

    return (
    <React.Fragment>
      <TableRow
        hover
        role="checkbox"
        tabIndex={-1}
        key={row.name}
        aria-label='recipe row'
      >
        <TableCell>
              {/* This button expands a dropdown with the missing ingredients*/}
          {props.useIngredients && <IconButton
              aria-label="expand row button"
              size="small"
              onClick={() => setOpen(!open)}
          >
              {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>}
        </TableCell>
        <TableCell
          component="th"
          scope="row"
          padding="none"
        >
          <Link aria-label='recipe link' href= {row.link} target='_blank'>{row.name}</Link>
        </TableCell>
        <TableCell aria-label='time to cook in minutes' align="right">{row.time}</TableCell>
        <TableCell aria-label='price of recipe' align="right">{(row.price).toFixed(2)}</TableCell>
        {/* push the recipe to the saved list onclick */}
        <TableCell align="right" aria-label='save recipe button'>
          <Button variant='contained' onClick ={() =>{saveRecipe(userId, row.id)}}>Save</Button>
        </TableCell>
        {props.day > 0 && <TableCell aria-label='day of recipe planner' align="right">{row.day}</TableCell>}
        {props.useMeal && <TableCell aria-label='meal for the day' align="right">{row.mealToCook}</TableCell>}
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
         {props.useIngredients &&  <Collapse in={open} timeout="auto" unmountOnExit aria-label='missing ingredients drop down'>
                <Box sx={{ margin: 1 }}>
                <Typography variant="h6" gutterBottom component="div">
                    Missing Ingredients:
                </Typography>
                <Table size="small" aria-label="ingredients to get">
                    <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                    </TableRow>
                    </TableHead>
                    <TableBody>
                    {row.ingredientList.map((ingredient : any) => (
                        <TableRow key={ingredient.name}>
                        <TableCell scope="row">{ingredient.name}</TableCell>
                        <TableCell>{ingredient.owned}</TableCell>
                        <TableCell align="right">{ingredient.amount}</TableCell>
                        <TableCell align="right">
                            {ingredient.price}
                        </TableCell>
                        </TableRow>
                    ))}
                    </TableBody>
                </Table>
                </Box>
            </Collapse>} 
        
            </TableCell>
      </TableRow>
      </React.Fragment>
    );
  }