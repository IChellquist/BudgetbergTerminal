import { TabBar } from "antd-mobile";
import {
  FolderOutline,
  UnlockOutline,
  UnorderedListOutline,
} from "antd-mobile-icons";
import { useNavigate, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";


const Bottom: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { pathname } = location;
  const isLoggedIn = useSelector((state: any) => state.login.isLoggedIn);
  const userRole = useSelector((state:any) => state.login.userRole);
  const dispatch = useDispatch();

  const tabs = [
    {
      key: "/",
      title: "Reports",
      icon: <FolderOutline />,
    },
    {
      key: "/reportsettings",
      title: "Report Settings",
      icon: <UnorderedListOutline />,
    },
    {
      key: "/admin",
      title: "Admin Panel",
      icon: <UnlockOutline />,
    },
  ];

  return (
    <TabBar activeKey={pathname} onChange={(key) => navigate(key)}>
      <TabBar.Item title={"Reports"} key={"/"} icon={<FolderOutline/>}/>
      <TabBar.Item title={"Report Settings"} key={"/reportsettings"} icon={<UnorderedListOutline/>}/>
      {!isLoggedIn ?? <TabBar.Item/ >}
     {/*  {tabs.map((item) => (
        <TabBar.Item key={item.key} icon={item.icon} title={item.title} />
      ))} */}
    </TabBar>
  );
};

export default Bottom;
