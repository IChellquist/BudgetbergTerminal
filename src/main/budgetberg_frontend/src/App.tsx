import React, { useEffect } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MobileLayout from "./layouts/MobileLayout";
import "./App.css";
import { ConfigProvider, NavBar } from "antd-mobile";
import Reports from "./pages/Reports";
import ReportSettings from "./pages/ReportSettings";
import enUS from "antd-mobile/es/locales/en-US";
import Login from "./pages/Login";
import User from "./pages/User";
import { useDispatch } from "react-redux";
import { loginActions } from "./reduxstore/login-slice";
import Admin from "./pages/Admin";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MobileLayout />,
    children: [
      { path: "/", element: <Reports /> },
      { path: "/reportsettings", element: <ReportSettings /> },
      { path: "/login", element: <Login /> },
      { path: "/user", element: <User /> },
      {path: "/admin", element: <Admin/>}
    ],
  },
]);

const App: React.FC = () => {
  const dispatch = useDispatch();
  let isInitialLoad = true;
  

  useEffect(() => {
    if (isInitialLoad){
      let cookieLoginInfo = localStorage.getItem("jwtLoginInfo");
      if (cookieLoginInfo !== null){
        let {jwtTokenExpiration, userRoles}  = JSON.parse(cookieLoginInfo);
        if (Number(jwtTokenExpiration) >= Date.now()){
          dispatch(loginActions.initialLoadLoginCheck({userRoles}));
        }
      }
    }
    isInitialLoad = false; 
  })



  return (
    <ConfigProvider locale={enUS}>
      <RouterProvider router={router} />;
    </ConfigProvider>
  );
};

export default App;
