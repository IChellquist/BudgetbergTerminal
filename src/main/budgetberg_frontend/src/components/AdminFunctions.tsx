import { Button, List } from "antd-mobile";
import { ListItem } from "antd-mobile/es/components/list/list-item";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loginActions } from "../reduxstore/login-slice";

const AdminFunctions: React.FC = () => {
  const dispatch = useDispatch();
  const [showError, setShowError] = useState(false);

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

  const adminFunctionsList = [
    {
      key: "logoutButton",
      title: "Log Out Button",
      description: "Test the logout functionality",

      children: (
        <Button
          onClick={(event) => {
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("jwtTokenExpiration");
            dispatch(loginActions.logOut({}));
          }}
          color="primary"
          fill="solid"
        >
          Log Out
        </Button>
      ),
    },
    {
      key: "testBackendReportFetch",
      title: "Test Backend Report Fetch",
      description:
        "Test the backend report fetching using the available redux state for part of the test",
      children: (
        <Button
          color={"primary"}
          fill={"solid"}
          onClick={ async (event) => {
            try {
              const response = await fetch("http://localhost:8080" + "/api/admin/testBackendReportFetch", {
                method: "POST",
                body: JSON.stringify({
                  reportType: reportTypeSlice[0],
                  exchanges: exchangesSlice,
                  sectors: sectorsSlice,
                  dateSelected: selectedDateSlice


                }),
                headers: {
                  "Authorization": "Bearer " + localStorage.getItem("jwtToken"),
                  "Content-Type": "application/json" 
                }
              });
            
              if (!response.ok) {
                // Check for an HTTP error status (e.g., 404 Not Found, 500 Internal Server Error)
                throw new Error(`HTTP error! Status: ${response.status}`);
              }
              console.log("Test success!")
            
           
            } catch (error) {
              // Handle any errors that occur during the request
              console.error("An error occurred:", error);
            }
            
          }}
        >
          Test Backend Report Fetch
        </Button>
      ),
    },
  ];

  return (
    <>
      <List>
        {adminFunctionsList.map((adminFunction) => (
          <ListItem
            key={adminFunction.key}
            title={adminFunction.title}
            description={adminFunction.description}
            children={adminFunction.children}
          ></ListItem>
        ))}
      </List>
    </>
  );
};

export default AdminFunctions;
