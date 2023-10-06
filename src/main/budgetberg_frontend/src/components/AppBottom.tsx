import { TabBar } from "antd-mobile";
import {
  FolderOutline,
  UnlockOutline,
  UnorderedListOutline,
} from "antd-mobile-icons";
import { useNavigate, useLocation } from "react-router-dom";

const Bottom: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { pathname } = location;

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
      {tabs.map((item) => (
        <TabBar.Item key={item.key} icon={item.icon} title={item.title} />
      ))}
    </TabBar>
  );
};

export default Bottom;
