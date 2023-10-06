import { NavBar } from "antd-mobile";
import AppBottom from "../components/AppBottom";
import { Outlet } from "react-router-dom";

const Mobile : React.FC = () => {
    return (
        <>
          <div className="app">
            <div className="top">
              <NavBar>Budgetberg Terminal</NavBar>
            </div>
            <div className="body">
              <Outlet/>
            </div>
            <div className="bottom">
              <AppBottom></AppBottom>
            </div>
          </div>
        </>
      );

}

export default Mobile;