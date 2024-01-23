import { createSlice } from "@reduxjs/toolkit";

//init date to last previous weekday
let date = new Date();
date.setDate(date.getDate() - 1);
 if (date.getDay() === 0 || date.getDay() === 6){
    if (date.getDay() === 0){date.setDate(date.getDate() - 2)}
    else {date.setDate(date.getDate() - 1)}
  }

const reportSettingsSlice  = createSlice({
    name: 'reportSettings',
    initialState: {
        exchanges: ["NASD"],
        sectors: ["Health Care"],
        reportType: ["StrongVolumeGainers"],
        dateSelected: date
        
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