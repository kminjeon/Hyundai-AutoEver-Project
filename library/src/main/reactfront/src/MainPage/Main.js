import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Main = () => {

    const name = sessionStorage.getItem('name');

    const [isLogin, setIsLogin] = useState(false)
 
  useEffect(() => {
    const personalId = sessionStorage.getItem('personalId');
    
    if (personalId !== null) {
    // sessionStorage에 personalId 값이 존재한다면
      setIsLogin(true);
      console.log('isLogin ?? :: ', isLogin)
    }else{
        window.location.replace("/");
    }
    }, []);

    const handleLogout = () => {
        sessionStorage.removeItem('personalId');
        sessionStorage.removeItem('name');
        setIsLogin(false);
        console.log('로그아웃');
      }
    return (
        <div style={{ 
            display: 'flex', justifyContent: 'center', alignItems: 'center', 
            width: '100%', height: '100vh'
            }}>
            <p>현대오토에버 도서관</p>
            <p>{name}님, 환영합니다!</p>
            <button onClick={handleLogout}>Logout</button>
        </div>
    )
}

export default Main;