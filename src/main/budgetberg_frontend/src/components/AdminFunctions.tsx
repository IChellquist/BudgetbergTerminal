import { Button, List } from "antd-mobile";
import { ListItem } from "antd-mobile/es/components/list/list-item";
import { useDispatch } from "react-redux";
import { loginActions } from "../reduxstore/login-slice";

const AdminFunctions: React.FC = () => {
  const dispatch = useDispatch();
  const adminFunctionsList = [
    {
      key: "LogoutButton",
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
  ];

  return (
    <>
      <List >
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
