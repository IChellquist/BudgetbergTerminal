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
            <div className="body" style={{overflowY: 'scroll'}}>
              <Outlet/>
            </div>
            <div className="bottom">
              <AppBottom/>
            </div>
          </div>
        </>
      );

}

export default Mobile;