import { configureStore, createSlice, Slice } from "@reduxjs/toolkit";

const loginSlice : Slice = createSlice({
    name: 'login',
    initialState: {isLoggedIn: false, userRole : "None"},
    reducers: {
        logIn(state){
            state.isLoggedIn = true;
            
        },
        logOut(state){
            state.isLoggedIn = false;
        }

    }
});

export const loginActions = loginSlice.actions;
export default loginSlice;

