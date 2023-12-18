import React from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "./components/Login";
import MobileLayout from "./layouts/MobileLayout";

import "./App.css";
import { ConfigProvider, NavBar } from "antd-mobile";
import AppBottom from "./components/AppBottom";
import Reports from "./pages/Reports";
import ReportSettings from "./pages/ReportSettings";
import AdminPanel from "./pages/AdminPanel";
import enUS from "antd-mobile/es/locales/en-US";
import { Provider } from "react-redux";
import store from "./reduxstore/index";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MobileLayout />,
    children: [
      { path: "/", element: <Reports /> },
      { path: "/reportsettings", element: <ReportSettings /> },
      { path: "/admin", element: <AdminPanel /> },
    ],
  },
]);

const App: React.FC = () => {
  return (
    <ConfigProvider locale={enUS}>
      <Provider store={store}>
        <RouterProvider router={router} />;
      </Provider>
    </ConfigProvider>
  );
};

export default App;
