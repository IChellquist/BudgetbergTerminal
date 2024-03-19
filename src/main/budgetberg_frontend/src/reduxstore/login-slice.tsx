import { configureStore, createSlice, Slice } from "@reduxjs/toolkit";

const loginSlice: Slice = createSlice({
    name: 'login',
    initialState: { isLoggedIn: false, userRole: null },
    reducers: {
        logIn(state, actions) {
            const jwtLoginInfo = {
                jwtToken: actions.payload.jwtToken,
                jwtTokenExpiration: actions.payload.jwtTokenExpiration,
                userRoles: actions.payload.userRoles
            }
            localStorage.setItem("jwtLoginInfo", JSON.stringify(jwtLoginInfo));
            state.isLoggedIn = true;
            state.userRole = jwtLoginInfo.userRoles[0];
        },
        initialLoadLoginCheck(state, actions){
            state.isLoggedIn = true;
            state.userRole = actions.payload.userRoles[0];
        },
        logOut(state) {
            state.isLoggedIn = false; state.userRoles = null;
            localStorage.removeItem("jwtLoginInfo");
        }

    }
});

export const loginActions = loginSlice.actions;
export default loginSlice;

