import * as React from 'react';
import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import { Button, Checkbox, Input, Link, TextField } from '@mui/material';
import{getUID, editSpecificRating, unsaveRecipe} from '../firebase';
import { useState,  useRef } from 'react'

interface SavedRecipe{
  name: string,
  link: string,
  id: string,
  time: number,
  price: number, 
}

function Unsave(userId: string, recipeId : string){
  // pass in a recipe id
  // want to make a call to the API and remove it from the ratings hashmap (firebase function)
  unsaveRecipe(userId, recipeId)
}

function UpdateRating(userId: string, recipeId: string, rating: number){
  console.log(recipeId, rating)
  editSpecificRating(userId, recipeId, rating)
}

export default function SavedRow(props: {row : SavedRecipe}) {
  const [rating, setRating] = useState("");
  const userId = getUID();
  const {row} = props
  return (
    <React.Fragment>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell scope="row">
            <Link href= {row.link} target='_blank'>{row.name}</Link>
        </TableCell>
        <TableCell align="right">{row.time}</TableCell>
        <TableCell align="right">{row.price}</TableCell>
        {/* The onclick should delete the row from the state rows  */}
        <TableCell align="right"><Button aria-label='unsave button' variant ='contained' onClick={()=>{Unsave(userId, row.id)}}>Unsave</Button></TableCell>
        <TableCell align="right">
            <TextField
            aria-label='rating input'
            required
            label="Rating from 0 to 5"
            defaultValue=""
            onChange={ (e) => 
              {   
                      setRating(String(e.target.value))
              }}
            />
        </TableCell>
        <TableCell align="right">
            <Button aria-label='update rating' variant ='contained'onClick={() => {UpdateRating(userId, row.id, Number(rating))}}>Set Rating</Button>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

