import {
  Calendar,
  Checkbox,
  Collapse,
  Selector,
  SelectorOption,
  Space,
} from "antd-mobile";
import { CollapsePanel } from "antd-mobile/es/components/collapse/collapse";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import reportSettingsSlice, {
  reportSettingsActions,
} from "../reduxstore/reportSettings-slice";

const ReportSettingsComponent: React.FC = () => {
  const dispatch = useDispatch();
  const exchangesSlice = useSelector(
    (state: any) => state.reportSettings.exchanges
  );
  const sectorsSlice = useSelector(
    (state: any) => state.reportSettings.sectors
  );
  const reportTypeSlice = useSelector(
    (state: any) => state.reportSettings.reportType
  );
  const selectedDateSlice = useSelector(
    (state: any) => state.reportSettings.dateSelected
  );
  const exchanges = [
    "AMEX",
    "NASD",
    "NYSE",
    "OTCMKT",
  ];
  const sectors = [
    "Communication Services",
    "Consumer Discretionary",
    "Energy",
    "Financial",
    "Health Care",
    "Industrial",
    "Materials",
    "Technology",
    "Not Specified"
  ];

  const reportTypes = [
    {
      label: "Strong Volume Gainers",
      value: "StrongVolumeGainers",
    },
    {
      label: "Strong Volume Decliners",
      value: "StrongVolumeDecliners",
    },
  ];

  return (
    <>
      <Collapse
        defaultActiveKey={["exchanges", "sectors", "reportType", "date"]}
      >
        <CollapsePanel key={"reportType"} title={"Select Report Type"}>
          <Selector
            options={reportTypes}
            value={reportTypeSlice}
            onChange={(selectedReportType) => {
              dispatch(reportSettingsActions.setReportType(selectedReportType));
            }}
          />
        </CollapsePanel>
        <CollapsePanel key={"exchanges"} title={"Select Exchanges"}>
          <Space
            className="Exchanges"
            wrap={true}
            direction={"vertical"}
            style={{ "--gap": "8px" }}
          >
            <Checkbox
              indeterminate={
                exchangesSlice.length > 0 &&
                exchangesSlice.length < exchanges.length
              }
              checked={exchangesSlice.length === exchanges.length}
              onChange={(checked: boolean) => {
                if (checked) {
                  dispatch(reportSettingsActions.setExchanges(exchanges));
                } else {
                  dispatch(reportSettingsActions.setExchanges([]));
                }
              }}
            >
              Select All Exchanges
            </Checkbox>
            <Checkbox.Group
              value={exchangesSlice}
              onChange={(selected) => {
                dispatch(
                  reportSettingsActions.setExchanges(selected as string[])
                );
              }}
            >
              <Space
                className="Exchanges"
                wrap={true}
                direction={"horizontal"}
                style={{ "--gap": "8px" }}
              >
                {exchanges.map((exchange) => (
                  <Checkbox key={exchange} value={exchange}>
                    {exchange}
                  </Checkbox>
                ))}
              </Space>
            </Checkbox.Group>
          </Space>
        </CollapsePanel>
        <CollapsePanel key={"sectors"} title={"Select Sectors"}>
          <Space className="Sectors" direction="vertical">
            <Checkbox
              indeterminate={sectorsSlice.length > 0 && sectorsSlice.length < 8}
              checked={sectorsSlice.length === sectors.length}
              onChange={(checked: boolean) => {
                if (checked) {
                  dispatch(reportSettingsActions.setSectors(sectors));
                } else {
                  dispatch(reportSettingsActions.setSectors([]));
                }
              }}
            >
              Select All Sectors
            </Checkbox>

            <Checkbox.Group
              value={sectorsSlice}
              onChange={(selected) => {
                dispatch(
                  reportSettingsActions.setSectors(selected as string[])
                );
              }}
            >
              <Space
                className="Sectors"
                wrap={true}
                direction={"horizontal"}
                style={{ "--gap": "8px" }}
              >
                {sectors.map((sector) => (
                  <Checkbox key={sector} value={sector}>
                    {sector}
                  </Checkbox>
                ))}
              </Space>
            </Checkbox.Group>
          </Space>
        </CollapsePanel>
        <CollapsePanel key={"date"} title={"Select Date"}>
          <Calendar
            selectionMode="single"
            value={selectedDateSlice}
            onChange={(selectedDate) => {
              dispatch(reportSettingsActions.setDate(selectedDate));
            }}
          />
        </CollapsePanel>
      </Collapse>
    </>
  );
};

export default ReportSettingsComponent;
