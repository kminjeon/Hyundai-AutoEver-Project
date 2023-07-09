import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from '../header/Header';
import Footer from '../Footer/Footer';
import './Mypage.css'

const Mypage = () => {

    const name = sessionStorage.getItem('name');
    
    return (
        <div>
              <main>
                    <p>현대오토에버 도서관</p>
                    <p>{name}님, 환영합니다!</p>                
              </main>
        </div>
    )
}

export default Mypage;