import * as React from 'react';
import { alpha } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import { visuallyHidden } from '@mui/utils';
import RecipeRow from './RecipeRow';


// Adapted from sortable table on Material UI documentation

export interface Recipe {
  name: string;
  link: string;
  time: number;
  price: number;
  ingredientList: Ingredient[];
  meal: string[]; // hidden
  save: boolean;
  vegetarian: boolean, // hidden
  vegan: boolean, // hidden
  glutenFree: boolean // hidden
  id: string
  day: number
  mealToCook: string;
}
export function createRecipe(
  name: string,
  link: string,
  time: number,
  price: number,
  ingredientList: Ingredient[],
  meal: string[],
  save: boolean,
  vegetarian: boolean,
  vegan: boolean,
  glutenFree: boolean,
  id: string, 
  day: number,
  mealToCook: string
): Recipe {
  return {
    name,
    link,
    time,
    price,
    ingredientList,
    save,
    meal,
    vegetarian,
    vegan,
    glutenFree,
    id,
    day,
    mealToCook
  };
}
export interface Ingredient{
  name: string;
}
export function createIngredient(
    name: string
) : Ingredient{
  return{
    name
  }
}
export const mockFruitSaladIngredients : Ingredient[] = [
  {
    name: 'Strawberry',
  },
  {
    name: 'Honeydew',
  },
]

export const mockRows : Recipe[] = [
  createRecipe('Cupcake', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',305, 5, mockFruitSaladIngredients,['Breakfast'],false,true,true,false, 'fakeid', 0, 'breakfast'),
  createRecipe('Donut', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',452, 3, mockFruitSaladIngredients, ['Lunch'], false,false,true,true , 'fakeid',0,'breakfast'),
  createRecipe('Eclair', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',262, 1, mockFruitSaladIngredients, ['Breakfast'], false,true,false,true, 'fakeid',0,'breakfast'),
  createRecipe('Frozen yoghurt', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',159, 43, mockFruitSaladIngredients, ['Dinner'], false,true,true,false, 'fakeid',0,'breakfast'),
  createRecipe('Gingerbread', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',356, 13, mockFruitSaladIngredients, ['Breakfast'], false,false,true,true, 'fakeid',0,'breakfast'),
  createRecipe('Honeycomb', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',408, 4314, mockFruitSaladIngredients, ['Breakfast'], false,true,false,true, 'fakeid',0,'breakfast'),
  createRecipe('Ice cream sandwich', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',237, 43, mockFruitSaladIngredients, ['Snack'], false,true,true,true, 'fakeid',0,'breakfast'),
  createRecipe('Jelly Bean', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',375,1, mockFruitSaladIngredients, ['Dessert'], false,false,true,false, 'fakeid',0,'breakfast'),
  createRecipe('KitKat', 'https://natashaskitchen.com/perfect-vanilla-cupcake-recipe/',518,0, mockFruitSaladIngredients, ['Breakfast'], false,false,false,false, 'fakeid',0,'breakfast'),
];

// comparator for table sort
function descendingComparator<T>(a: T, b: T, orderBy: keyof T) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

type Order = 'asc' | 'desc';

// Runs comparator
function getComparator<Key extends keyof any>(order: Order, orderBy: Key,): (
  a: { [key in Key]: number | string | string[] | Ingredient[] | boolean},
  b: { [key in Key]: number | string | string[] | Ingredient[] | boolean},
) => number {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

interface HeadCell {
  disablePadding: boolean;
  id: keyof Recipe;
  label: string;
  numeric: boolean;
  sortable: boolean;
}

const headCells: readonly HeadCell[] = [
  {
    id: 'name',
    numeric: false,
    disablePadding: true,
    label: 'Meal',
    sortable: true
  },
  {
    id: 'time',
    numeric: true,
    disablePadding: false,
    label: 'Time(mins)',
    sortable: true
  },
  {
    id: 'price',
    numeric: true,
    disablePadding: false,
    label: 'Price($)',
    sortable: true
  },
  {
    id: 'save',
    numeric: true,
    disablePadding: false,
    label: 'Save?',
    sortable: false
  }
];

interface EnhancedTableProps {
  onRequestSort: (event: React.MouseEvent<unknown>, property: keyof Recipe) => void;
  order: Order;
  orderBy: string;
  rowCount: number;
}

// Adatped from Material UI for Table Header with sorting buttons
function EnhancedTableHead(props: EnhancedTableProps) {
  const { order, orderBy, onRequestSort } =
    props;
  const createSortHandler =
    (property: keyof Recipe) => (event: React.MouseEvent<unknown>) => {
      onRequestSort(event, property);
    };
  
  return (
    <TableHead>
      <TableRow>
          <TableCell></TableCell>
        {headCells.map((headCell) => (
          <TableCell
            key={headCell.id}
            align={headCell.numeric ? 'right' : 'left'}
            padding={headCell.disablePadding ? 'none' : 'normal'}
            sortDirection={orderBy === headCell.id ? order : false}
          >
            {headCell.sortable ?
            <TableSortLabel
              aria-label='clickable sort header label'
              active={orderBy === headCell.id}
              direction={orderBy === headCell.id ? order : 'asc'}
              onClick={createSortHandler(headCell.id)}
            >
            
              {headCell.label}
              {orderBy === headCell.id ? (
                <Box component="span" sx={visuallyHidden}>
                  {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                </Box>
              ) : null}
            </TableSortLabel>
            : <Typography>{headCell.label}</Typography>}
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}


export default function RecipeTable(props : {meal: string|null, diet: string|null, budget: number, useIngredients: boolean, useSaved: boolean, rows : Recipe[]}) {
  const [order, setOrder] = React.useState<Order>('asc');
  const [orderBy, setOrderBy] = React.useState<keyof Recipe>('price');
  const mealsToCook : string[] = ['breakfast', 'lunch','dinner','snack','dessert','side dish']
  var mealToMake = props.meal
  
  if(props.meal==='Surprise me!'){
    var ranNum = Math.floor(Math.random()*5)
    mealToMake = mealsToCook[ranNum]
  }
  const handleRequestSort = (
    event: React.MouseEvent<unknown>,
    property: keyof Recipe,
  ) => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };
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

  return (
    <Box sx={{ width: '100%' }}>
      <Paper sx={{ width: '100%', mb: 2 }}>
        <TableContainer>
          <Table
            aria-label='Recipe table'
            sx={{ minWidth: 750 }}
            aria-labelledby="tableTitle"
          >
            <EnhancedTableHead
              order={order}
              orderBy={orderBy}
              onRequestSort={handleRequestSort}
              rowCount={mockRows.length}
            />
            <TableBody>
              {props.rows.sort(getComparator(order, orderBy)).slice()
                .map((row, index) => {
                    if(row.meal.includes(mealToMake!) && handleDiet(props.diet!, row)
                      && row.price<=props.budget && row.save!==props.useSaved){
                        console.log(row.link)
                      return(<RecipeRow useIngredients={props.useIngredients} row= {row} day={-1} useMeal={false}></RecipeRow>)
                    }
                    else if(props.meal==='any' && props.diet==='any' && props.budget> 1000000 && props.useSaved===false){
                        return(<RecipeRow useIngredients={props.useIngredients} row= {row} day={-1} useMeal={false}></RecipeRow>)
                    }
                })}
                <TableRow
                >
                  <TableCell colSpan={6} />
                </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Box>
  );
}