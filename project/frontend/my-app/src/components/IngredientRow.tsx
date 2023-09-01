import { TableRow, TableCell, IconButton, Checkbox } from "@mui/material";
import { Link } from "react-router-dom";
import { Ingredient } from "./RecipeTable";

/**
 * This is a row of ingredients as displayed in the Planner page. It displays an ingredient
 * that is required to make the meals for the week
 */
export default function IngredientRow(props: {ingredient : Ingredient}){
    return (
        <TableRow aria-label='ingredient row' sx={{ '& > *': { borderBottom: 'unset' } }}>
            <TableCell align="left">{props.ingredient.name}</TableCell>
      </TableRow>
    )
}