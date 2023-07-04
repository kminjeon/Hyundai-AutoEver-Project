import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './App.css';
import Login from './LoginPage/Login';
import Main from './MainPage/Main';
 
function App () {
 // 로그인 상태 관리
  const [isLogin, setIsLogin] = useState(false)
 
  useEffect(() => {
    const personalId = sessionStorage.getItem('personalId');
    
    if (personalId !== null) {
    // sessionStorage에 personalId 값이 존재한다면
      setIsLogin(true);
      console.log('isLogin ?? :: ', isLogin)
    }
    }, []);

    const handleLogout = () => {
      sessionStorage.removeItem('personalId');
      sessionStorage.removeItem('name');
      setIsLogin(false);
      console.log('로그아웃');
    }
 
  return (
    <div>
      {isLogin ? (
        <Main handleLogout={handleLogout} /> 
      ): (
      <Login setIsLogin={setIsLogin}/>
        )}
    </div>
  );
}
 
export default App;