import React, {useEffect, useState} from 'react';
import axios from 'axios';

const AdminHeader = () => {
    const name = sessionStorage.getItem('name');

    const [isLogin, setIsLogin] = useState(false)
    const [searchWord, setSearchWord] = useState('');

  useEffect(() => {
    const personalId = sessionStorage.getItem('personalId');
    const authType = sessionStorage.getItem('authType');

    if (personalId !== null && authType == 'ADMIN') {
    // sessionStorage에 personalId 값이 존재한다면
      setIsLogin(true);
      console.log('isLogin ?? :: ', isLogin)
    } else {
        window.location.assign("/");
    }
    }, [isLogin]);

    const handleLogout = () => {
        sessionStorage.removeItem('personalId');
        sessionStorage.removeItem('name');
        sessionStorage.removeItem("email");
        sessionStorage.removeItem("authType");
        setIsLogin(false);
        console.log('로그아웃');
        window.location.assign("/");
      }
      
    return (
        <header className="header">
            <div className="header-container">
                <img className='header-logo' src='/img/logo.svg' alt='로고' onClick={()=> {window.location.assign("/admin/main");}}></img>
                <div className='right-items'>
                  <label className='logout' onClick={handleLogout}>로그아웃</label>
                </div>
            </div>
        </header>
    )
}

export default AdminHeader;
