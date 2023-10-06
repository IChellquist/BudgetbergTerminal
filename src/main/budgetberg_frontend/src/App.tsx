import React from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "./components/Login";
import MobileLayout from "./layouts/MobileLayout";

import "./App.css";
import { NavBar } from "antd-mobile";
import AppBottom from "./components/AppBottom";
import Reports from "./pages/Reports";
import ReportSettings from "./pages/ReportSettings";
import AdminPanel from "./pages/AdminPanel";

const router = createBrowserRouter([
  { path: "/", 
  element: <MobileLayout />, 
  children: [
    {path: '/', element: <Reports/>},
    {path: '/reportsettings', element: <ReportSettings/>},
    {path: '/admin', element: <AdminPanel/>}

  ] },
  { path: "/login", element: <Login /> },
]);

const App: React.FC = () => {
  return <RouterProvider router={router} />;
};

export default App;
