import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './Header.css'

const Header = () => {
    const name = sessionStorage.getItem('name');

    const [isLogin, setIsLogin] = useState(false)
    const [searchWord, setSearchWord] = useState('');

  useEffect(() => {
    const personalId = sessionStorage.getItem('personalId');
    
    if (personalId !== null) {
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
        setIsLogin(false);
        console.log('로그아웃');
        window.location.assign("/");
      }


    const handleSearch = () => {
        window.location.assign(`/search?searchWord=${searchWord}&page=0`);
      };

      const handleInputChange = (e) => {
        setSearchWord(e.target.value);
      };

      const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
          handleSearch();
        }
      };
      
    return (
        <header className="header">
            <div className="header-container">
                <img className='header-logo' src='/img/logo.svg' alt='로고' onClick={()=> {window.location.assign("/main");}}></img>
                <div className='search'>
                <input
                  className="header-input"
                  value={searchWord}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                />
                  <img className='searchimg' src='/img/search.png' />
                </div>
                <div className='right-items'>
                  <label className='logout' onClick={handleLogout}>로그아웃</label>
                  <span className='logout'> | </span>
                  <img className='mypage' src='/img/mypage.png' alt='마이페이지' onClick={() => {window.location.assign("/mypage");}}></img>
                </div>
            </div>
        </header>
    )
}

export default Header;
