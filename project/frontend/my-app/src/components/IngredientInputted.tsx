import { Button, ListItem, ListItemText } from "@mui/material";
import { getUID, retrieveIngredients, deleteIngredient, updateIngredients} from '../firebase';
/**
 * This is an ingredient in the list of ingredients on the Home page. It can be deleted with the click of a button
 */
export default function IngredientInputted(props : {ingredient : string}){
    const userId = getUID()
    return(
        <ListItem>
                <ListItemText primary= {props.ingredient} ></ListItemText>
                {/* On click deletes ingredient from the database  */}
                <Button variant='contained' aria-label='delete ingredient button' onClick={
                    () => {
                        deleteIngredient(userId, props.ingredient)
                    }
                }>Delete</Button>
        </ListItem>
    )
}