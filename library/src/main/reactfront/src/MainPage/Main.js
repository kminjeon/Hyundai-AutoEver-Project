import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Main = ({handleLogout}) => {

    const name = sessionStorage.getItem('name');

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