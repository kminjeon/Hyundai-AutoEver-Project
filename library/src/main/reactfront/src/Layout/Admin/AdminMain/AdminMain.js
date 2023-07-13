import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AdminHeader from '../Header/AdminHeader';

const AdminMain = () => {

    const name = sessionStorage.getItem('name');
    
    return (
        <div className="margin-header">
            <AdminHeader />
            <div>
                <div className='mypage-firstline'>
                    <p className='welcome'>{name}님, 환영합니다!</p>
                    <h2 className='mypage-h2'>ADMIN PAGE</h2>
                </div>
                    <hr className='mypageline'/>
                    <div className='box'>
                        <div className='line'>
                            <p className='click' onClick={()=> {window.location.assign("/admin/rentInfo")}}>대여 현황 관리</p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/rentHistory")}}>대여 기록 관리</p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/reserveInfo")}}>도서 예약 관리 </p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/applyInfo")}}>도서 신청 관리</p>
                        </div>
                        <hr className='hr-margin'/>
                        <div className='line'>
                        <p className='click'onClick={()=> {window.location.assign("/admin/bookInfo")}}>도서 관리</p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/auth")}}>권한 관리</p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/profile")}}>개인정보 변경</p>
                            <p className='click'onClick={()=> {window.location.assign("/admin/withdraw")}}>회원 탈퇴</p>
                        </div>
                    </div> 
            </div>
        </div>
    )
}

export default AdminMain;