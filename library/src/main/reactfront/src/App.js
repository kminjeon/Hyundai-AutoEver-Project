import React, {useEffect, useState} from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from 'axios';
import './App.css';
import Login from './LoginPage/Login';
import Main from './Layout/MainPage/Main';
import Layout from './Layout/Layout';
import Signup from './LoginPage/Signup';
import FinishSignup from './LoginPage/FinishSignup';
 
function App () {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/main" element={<Layout />} />
      <Route path='/signup' element={<Signup />} />
      <Route path='/signup/finish' element={<FinishSignup />} />
    </Routes>
  );
}
 
export default App;