import { createSlice } from "@reduxjs/toolkit";

const reportSettingsSlice  = createSlice({
    name: 'reportSettings',
    initialState: {
        exchanges: ["NASD"],
        sectors: ["Health Care"]
    },
    reducers: {
        setExchanges(state, actions){
            state.exchanges = actions.payload;
        },

        setSectors(state, actions){
            state.sectors = actions.payload;
        }


    }

});

export const reportSettingsActions = reportSettingsSlice.actions;
export default reportSettingsSlice;