// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getDatabase, ref, set, get, child} from "firebase/database";


// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries


import {GoogleAuthProvider, getAuth, signInWithPopup, signOut} from 'firebase/auth';
import { Navigate } from "react-router-dom";
import { resolve } from "path";
import { NewReleasesOutlined } from "@mui/icons-material";

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyC_hHPxynVORpdE97xzqK1i583qPPXKEw0",
  authDomain: "cs32reciperecommender.firebaseapp.com",
  databaseURL: "https://cs32reciperecommender-default-rtdb.firebaseio.com",
  projectId: "cs32reciperecommender",
  storageBucket: "cs32reciperecommender.appspot.com",
  messagingSenderId: "215134613951",
  appId: "1:215134613951:web:5e5ef3aee2cc01a1d4b10a",
  measurementId: "G-D2LBEB24JX"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const analytics = getAnalytics(app);
const db = getDatabase();


const googleProvider = new GoogleAuthProvider();

function signInWithGoogle(){
  console.log('Login Btn Call')
  return new Promise((resolve)=> {
    signInWithPopup(auth, googleProvider).then(res=>{
      console.log(res.user)
  
      const lookupURL = 'http://localhost:3232/database?function=lookup&userID='+res.user.uid
      var lookupArray = []
  
      fetch(lookupURL)
        .then(response => response.json())
        .then(nextResponse =>{
          console.log(nextResponse)
          lookupArray = Object.entries(nextResponse)
          console.log(lookupArray)
          if (lookupArray.length == 0){
            addUser(res.user.uid)
          }
        }).then( () =>{
          resolve(res.user)
        }
        )
    }).catch(e=>{
      console.log(e)
    })
  })
  
}

function getUID(){
  const auth = getAuth();
  const user = auth.currentUser;
  if (user !== null) {
    return user.uid
  }
  else{
    return "user not found"
  }
}

function addUser(userId: String){
  const newURL = 'http://localhost:3232/database?function=new&userID='+userId
          fetch(newURL)
          .then(response => console.log("new user created"))
}


function updateIngredients(userId: String, newIngredients: String){
  const newURL = 'http://localhost:3232/database?function=ingredient&userID='+userId+'&input='+newIngredients
  fetch(newURL)
  .then(response => console.log(newIngredients))
}

function deleteIngredient(userId: String, ingredientToDelete: String){
  console.log(ingredientToDelete)
  var oldIngredientsList = []
  var newIngredientsList: string[] = []
  retrieveIngredients(userId).then(
    
    oldIngredients => {
      oldIngredientsList = String(oldIngredients).split(",")
        for (let i=0; i<oldIngredientsList.length; i++){
          if (String(oldIngredientsList[i]) !== String(ingredientToDelete)){
            console.log(oldIngredientsList[i])
            newIngredientsList.push(String(oldIngredientsList[i]))
          }
        }
        console.log(newIngredientsList)
        // set firebase to the new ingredients list 
        updateIngredients(userId, newIngredientsList.toString())
      }
    
  )

}

function retrieveIngredients(userId: String){
  const newURL = 'http://localhost:3232/database?function=viewIngredients&userID='+userId
  var lookupArray = []
  var nestedArray = []
  return new Promise((resolve) =>{
    return fetch(newURL)
    .then(response => response.json())
    .then(nextResponse =>{
      lookupArray = Object.entries(nextResponse)
      nestedArray = lookupArray[0]
    //  console.log(nestedArray[1])
      resolve(nestedArray[1])
    })
  })
}

function retrieveRatingsMap(userID: String){
  // returns the ratings hashmap 
  const newURL = 'http://localhost:3232/database?function=viewRatings&userID='+userID
  var lookupArray = []
  var nestedArray = []
  return new Promise((resolve) =>{
    return fetch(newURL)
    .then(response => response.json())
    .then(nextResponse =>{
      lookupArray = Object.entries(nextResponse)
      nestedArray = lookupArray[0]
      console.log(nestedArray[1])
      resolve(JSON.stringify(nestedArray[1]))
    })
  })
}

function unsaveRecipe(userId: String, recipeID: String){
  const newURL = 'http://localhost:3232/database?function=unsave&userID='+userId+'&recipe='+recipeID
  fetch(newURL)
}

function editSpecificRating(userId: String, recipeID: String, rating: any){
  const newURL = 'http://localhost:3232/database?function=editRatings&userID='+userId+'&recipe='+recipeID+'&rating='+rating
  fetch(newURL)
}

// NOTE: not implemented yet! lmk if we want a logout button <3
const logout = () => {
  signOut(auth);
};

export {
  auth,
  db,
  signInWithGoogle,
  addUser,
  logout,
  getUID,
  retrieveIngredients, 
  updateIngredients,
  deleteIngredient,
  retrieveRatingsMap,
  unsaveRecipe,
  editSpecificRating
};