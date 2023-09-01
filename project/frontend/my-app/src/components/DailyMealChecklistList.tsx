import { Box, Button, Checkbox, FormControlLabel, FormGroup, Grid, Paper, Switch, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';
import DailyMealChecklist from './DailyMealChecklist';
import IngredientRow from './IngredientRow';
import RecipeRow from './RecipeRow';
import { Ingredient, Recipe } from './RecipeTable';
/**
 * This List of day checklists tracks the meals that a user wants to cook for the given amount of
 * days. They can choose the amount of days they want to cook, and it will show as many DailyMealChecklists
 * as they request.
 */
// idea taken from https://stackoverflow.com/questions/64655265/react-render-multiple-buttons-in-for-loop-from-given-integer


export default function DailyMealChecklistList(props : {numDays: number, recipeList : Recipe[], diet: string, budget: number} ){
    const [dayIdToMeals, setDayIdToMeals] = useState<Map<number,string[]>>(new Map<number,string[]>())
    const [rows, setRows] = useState<Recipe[]>([])
    const [toBuyIngredientList, setToBuyIngredientList] = useState<Ingredient[]>([])
    const [open, setOpen] = useState<boolean>(false)


    function adjustMap(meal:string, id:number) {

        if (dayIdToMeals.get(id)?.includes(meal)){
            let indexOfItem : number | undefined= dayIdToMeals.get(id)?.indexOf(meal)
            dayIdToMeals.get(id)?.splice(indexOfItem!,1)
            console.log('removing item')
        }
        else{
            dayIdToMeals.get(id)?.push(meal)
            console.log('adding item')
        }

        setDayIdToMeals(dayIdToMeals)
        console.log(dayIdToMeals)        
    }
    /**
     * Adds ingredients to the grocery list
     * @param ingredientList 
     */
    function addIngredients(ingredientList: Ingredient[]){
        for(let ingredient of ingredientList){
            if (!toBuyIngredientList.includes(ingredient)){
                toBuyIngredientList.push(ingredient)
                setToBuyIngredientList(toBuyIngredientList)
            }
            
        }
    }
    const checkList : any = [];

    for (let index = 1; index < props.numDays+1; index++) {
        dayIdToMeals.set(index, [])
        checkList.push(<DailyMealChecklist adjustMap={adjustMap} setDayIdsToMeals={setDayIdToMeals} map={dayIdToMeals} day={'Day '+index} id={index} totalNum={props.numDays}/>) 
    }

    // Not much error handling for if people change their mind
    function addRows(){
        const days : number[] = Array.from(dayIdToMeals.keys())
        let workingBudget : number = props.budget
        // if no budget is passed, just say they have 1,000,000 to spend
        if (workingBudget ===0){
            workingBudget += 1000000
        }
        for(let index = 0; index< days.length; index++){
            const mealArray : string[] = dayIdToMeals.get(days[index])!
            for(let mealIndex =0; mealIndex < mealArray.length; mealIndex++ ) {
                const mealToCook : string = mealArray[mealIndex]
                for(let recipe of props.recipeList) {
                    if (recipe.meal.includes(mealToCook) && !rows.includes(recipe) && handleDiet(props.diet, recipe) && workingBudget-recipe.price > 0){
                        workingBudget -= recipe.price
                        recipe.day = days[index]
                        recipe.mealToCook = mealToCook
                        addIngredients(recipe.ingredientList)
                        rows.push(recipe)
                        setRows(rows)
                        break
                    }
            }
        }
    }
    function handleDiet(dietType: string, row : Recipe) : boolean {
        if (dietType==='Vegetarian'){
          return row.vegetarian
        }
        else if (dietType==='Gluten free'){
          return row.glutenFree
        }
        else if (dietType==='Vegan'){
          return row.vegan
        }
        else{
          return true
        }
      }
}
    
    return(
        <Grid container alignContent='center' alignItems='center' textAlign='center'>
            <Grid aria-label='list of checklists' container>
                {checkList}
            </Grid>
            <Grid container aria-label='Get Meal Plan button' alignContent='center' alignItems='center' textAlign='center'>
                <Grid item xs={12}>
                    <Button variant='contained' 
                        onClick={(e: any) => {
                            addRows()
                            setOpen(!open)
                        }}>Get Meal Plan</Button>
                </Grid>
            </Grid>
            {open && <Grid container>
                <TableContainer component={Paper} aria-label='planner-table'>
                    <Table aria-label="collapsible table"  sx={{ minWidth: 750 }}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Planner</TableCell>
                                <TableCell>Name</TableCell>
                                <TableCell align="right">
                                Time&nbsp;(mins)
                                </TableCell>
                                <TableCell align="right">Price&nbsp;($)</TableCell>
                                <TableCell align="right">Save?</TableCell>
                                <TableCell align="right">Day #</TableCell>
                                <TableCell align="right">Meal</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                rows.map((row) => {
                                    return <RecipeRow useIngredients={true} row={row} day={row.day} useMeal={true}></RecipeRow>
                                }
                                )
                            }
                        </TableBody>
                    </Table>
                </TableContainer>
            </Grid> }
            
            {open && <Grid container>
                    <Grid item textAlign='center' xs={12}>
                            <Typography variant='h2'>Grocery List</Typography>
                        </Grid>
                        <Grid item textAlign='center' xs ={12}>
                            <TableContainer aria-label='grocery list table'>
                                <Table>
                                    <TableHead>
                                       <TableRow>
                                            <TableCell align='left'>Ingredient</TableCell>
                                            <TableCell align='center'>Quantity</TableCell>
                                            <TableCell align='right'>Price&nbsp;($)&nbsp;&nbsp;</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {toBuyIngredientList.map((ingredient) => (
                                            <IngredientRow key={ingredient.name} ingredient={ingredient}/>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Grid>
                     </Grid>
                }     
        </Grid>
    )
}
