import { configureStore, createSlice, Slice } from "@reduxjs/toolkit";

import loginSlice from "./login-slice";
import reportSettingsSlice from "./reportSettings-slice";

const store = configureStore({
    reducer: {login: loginSlice.reducer, reportSettings: reportSettingsSlice.reducer}
});

export default store;

