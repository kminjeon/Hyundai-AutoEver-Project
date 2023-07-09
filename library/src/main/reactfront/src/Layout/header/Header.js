import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './header.css'

const Header = () => {
    const name = sessionStorage.getItem('name');

    const [isLogin, setIsLogin] = useState(false)
 
  useEffect(() => {
    const personalId = sessionStorage.getItem('personalId');
    
    if (personalId !== null) {
    // sessionStorage에 personalId 값이 존재한다면
      setIsLogin(true);
      console.log('isLogin ?? :: ', isLogin)
    } else {
        window.location.replace("/");
    }
    }, [isLogin]);

    const handleLogout = () => {
        sessionStorage.removeItem('personalId');
        sessionStorage.removeItem('name');
        setIsLogin(false);
        console.log('로그아웃');
        window.location.replace("/");
      }
      
    return (
        <header className="header">
            <div className="header-container">
                <img className='logo' src='/img/logo.svg' alt='로고'></img>
                <div className='search'>
                  <input />
                  <img className='searchimg' src='img/search.png' />
                </div>
                <div className='right-items'>
                  <label className='logout' onClick={handleLogout}>로그아웃</label>
                  <span className='logout'> | </span>
                  <img className='mypage' src='/img/mypage.png' alt='마이페이지'></img>
                </div>
            </div>
        </header>
    )
}

export default Header;
