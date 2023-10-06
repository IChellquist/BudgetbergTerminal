import { configureStore, createSlice, Slice } from "@reduxjs/toolkit";

import loginSlice from "./login-slice";

const store = configureStore({
    reducer: {login: loginSlice.reducer}
});

export default store;

