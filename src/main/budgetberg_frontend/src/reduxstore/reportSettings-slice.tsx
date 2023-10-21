import { createSlice } from "@reduxjs/toolkit";

const reportSettingsSlice  = createSlice({
    name: 'reportSettings',
    initialState: {
        exchanges: ["NASD"],
        sectors: ["Health Care"],
        reportType: ["StrongVolumeGainers"],
        dateSelected: new Date()
        
    },
    reducers: {
        setExchanges(state, actions){
            state.exchanges = actions.payload;
        },

        setSectors(state, actions){
            state.sectors = actions.payload;
        },
        setReportType(state, actions){
            state.reportType = actions.payload;
        },
        setDate(state, actions){
            state.dateSelected = actions.payload;
        }


    }

});

export const reportSettingsActions = reportSettingsSlice.actions;
export default reportSettingsSlice;