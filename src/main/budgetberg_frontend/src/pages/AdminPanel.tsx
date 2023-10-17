import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loginActions } from "../reduxstore/login-slice";
import Login from "../components/Login";
import AdminFunctions from "../components/AdminFunctions";

let isInitialLoad = true;

const AdminPanel: React.FC = () => {
  const isLoggedIn = useSelector((state: any) => state.login.isLoggedIn);
  const dispatch = useDispatch();

  useEffect(() => {
    if (isInitialLoad) {
      isInitialLoad = false;
      const jwtToken = localStorage.getItem("jwtToken");
      const jwtTokenExpiration = localStorage.getItem("jwtTokenExpiration");

      if (jwtToken && Number(jwtTokenExpiration) >= Date.now()) {
        dispatch(loginActions.logIn({}));
      }
    }
  });

  return <div className="col-md-12">{isLoggedIn ? <AdminFunctions /> : <Login />}</div>;
};

export default AdminPanel;
