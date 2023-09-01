import { Button, Checkbox, FormControlLabel, FormGroup, Grid, Typography } from '@mui/material';
import React, { useState } from 'react';
/**
 * This checklist tracks which meals a user wants to cook per day
 */
export default function DailyMealChecklist(props : {day: string, id: number, adjustMap: (meal:string, id:number) => void, totalNum: number, map: Map<number,string[]>, setDayIdsToMeals: React.Dispatch<React.SetStateAction<Map<number, string[]>>>}){
    const [breakfastChecked, setBreakfastChecked] = useState<boolean>(false)
    const [lunchChecked, setLunchChecked] = useState<boolean>(false)
    const [dinnerChecked, setDinnerChecked] = useState<boolean>(false)
    const [dessertChecked, setDessertChecked] = useState<boolean>(false)
    const [snackChecked, setSnackChecked] = useState<boolean>(false)

    return(
        <Grid item xs={12/props.totalNum}>
            <Typography>{props.day}</Typography>
            <FormGroup aria-label='daily-meal-checklist'>
                <FormControlLabel control={<Checkbox checked={breakfastChecked} 
                    onChange={(e: any) => 
                        {setBreakfastChecked(!breakfastChecked)
                         props.adjustMap('breakfast',props.id)
                        }}/>} label="Breakfast" />
                <FormControlLabel control={<Checkbox checked={lunchChecked} 
                    onChange={(e: any) => 
                        {setLunchChecked(!lunchChecked)
                        props.adjustMap('lunch',props.id)
                        }}/>} label="Lunch" />
                <FormControlLabel control={<Checkbox checked={dinnerChecked} 
                    onChange={(e: any) => 
                        {setDinnerChecked(!dinnerChecked)
                        props.adjustMap('dinner',props.id)
                        }}/>} label="Dinner" />
                <FormControlLabel control={<Checkbox checked={dessertChecked} 
                    onChange={(e: any) => 
                        {setDessertChecked(!dessertChecked)
                        props.adjustMap('dessert',props.id)
                        }}/>} label="Dessert" />
                <FormControlLabel control={<Checkbox checked={snackChecked} 
                    onChange={(e: any) => 
                        {setSnackChecked(!snackChecked)
                        props.adjustMap('snack',props.id)
                        }}/>} label="Snack" />
            </FormGroup>
        </Grid>
    )
}
