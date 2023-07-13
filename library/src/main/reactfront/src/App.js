import React, {useEffect, useState} from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from 'axios';
import './App.css';
import Login from './Layout/LoginPage/Login';
import Main from './Layout/MainPage/Main';
import Signup from './Layout/LoginPage/Signup';
import FinishSignup from './Layout/LoginPage/FinishSignup';
import Mypage from './Layout/Mypage/base/Mypage';
import BookDetail from './Layout/BookDetail/BookDetail';
import CategoryPage from './Layout/CategoryPage/CategoryPage';
import Applyinfo from './Layout/Mypage/ApplyInfo/ApplyInfo';
import LikeBook from './Layout/Mypage/LikeBook/LikeBook';
import Profile from './Layout/Mypage/Profile/Profile';
import RentHistory from './Layout/Mypage/RentHistory/RentHistory';
import RentInfo from './Layout/Mypage/RentInfo/RentInfo';
import ReserveInfo from './Layout/Mypage/ReserveInfo/ReserveInfo';
import ReviewInfo from './Layout/Mypage/ReviewInfo/ReviewInfo';
import Withdraw from './Layout/Mypage/Withdraw/Withdraw'; 
import Search from './Layout/Search/Search';
import FindId from './Layout/LoginPage/FindId';
import FindPassword from './Layout/LoginPage/FindPassword';
import AdminMain from './Layout/Admin/AdminMain/AdminMain';
import AdminRentInfo from './Layout/Admin/AdminRentInfo/AdminRentInfo';

function App () {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/main" element={<Main />} />
      <Route path='/signup' element={<Signup />} />
      <Route path='/signup/finish' element={<FinishSignup />} />
      <Route path='/book/detail/:bookId' element={<BookDetail />} />
      <Route path='/book/category/:categoryType' element={<CategoryPage />} />
      <Route path='/mypage' element={<Mypage />} />
      <Route path='/mypage/rentInfo' element={<RentInfo />} />
      <Route path='/mypage/rentHistory' element={<RentHistory />} />
      <Route path='/mypage/reserveInfo' element={<ReserveInfo />} />
      <Route path='/mypage/applyInfo' element={<Applyinfo />} />
      <Route path='/mypage/profile' element={<Profile />} />
      <Route path='/mypage/reviewInfo' element={<ReviewInfo />} />
      <Route path='/mypage/likeBook' element={<LikeBook />} />
      <Route path='/mypage/withdraw' element={<Withdraw />} />
      <Route path='/search' element={<Search />} />
      <Route path='/FindId' element={<FindId />} />
      <Route path='/FindPassword' element={<FindPassword />} />

      <Route path='/admin/main' element={<AdminMain />} />
      <Route path='/admin/rentInfo' element={<AdminRentInfo />} />
    </Routes>
  );
}
 
export default App;