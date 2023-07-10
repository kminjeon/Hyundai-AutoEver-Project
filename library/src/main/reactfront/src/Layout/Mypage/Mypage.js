import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Footer from '../Footer/Footer';
import './Mypage.css'
import Header from '../header/Header';
import Category from '../Category/Category';

const Mypage = () => {

    const name = sessionStorage.getItem('name');
    
    return (
        <div className="headercategoryline">
            <Category />
            <Header />
              <main>
                    <p>현대오토에버 도서관</p>
                    <p>{name}님, 환영합니다!</p>                
              </main>
        </div>
    )
}

export default Mypage;