import { NavBar } from "antd-mobile";
import AppBottom from "./MobileLayoutComponents/AppBottom";
import { Outlet } from "react-router-dom";
import { TabBar } from "antd-mobile";
import {
  FolderOutline,
  UnlockOutline,
  UnorderedListOutline,
  UserOutline,
} from "antd-mobile-icons";
import { useNavigate, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

const Mobile: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { pathname } = location;
  const isLoggedIn = useSelector((state: any) => state.login.isLoggedIn);
  const userRole = useSelector((state: any) => state.login.userRole);
  const dispatch = useDispatch();
  console.log(isLoggedIn);

  return (
    <>
      <div className="app">
        <div className="top">
          <NavBar>Budgetberg Terminal</NavBar>
        </div>
        <div className="body" style={{ overflowY: 'scroll' }}>
          <Outlet />
        </div>
        <div className="bottom">
         

            <TabBar activeKey={pathname} onChange={(key) => navigate(key)}>
            <TabBar.Item title={"Reports"} key={"/"} icon={<FolderOutline />} />
            <TabBar.Item title={"Report Settings"} key={"/reportsettings"} icon={<UnorderedListOutline />} />
            {!isLoggedIn ? (<TabBar.Item title={"Login"} key={"/login"} icon={<UserOutline/>}/>) : null}
            
          </TabBar>
        </div>
      </div>
    </>
  );

}

export default Mobile;