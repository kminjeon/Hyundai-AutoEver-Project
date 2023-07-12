import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Footer from '../../Footer/Footer';
import './Mypage.css'
import Header from '../../Header/Header';
import Category from '../../Category/Category';

const Mypage = () => {

    const name = sessionStorage.getItem('name');
    
    return (
        <div className="margin-header">
            <Header />
            <div>
                <div className='mypage-firstline'>
                    <p className='welcome'>{name}님, 환영합니다!</p>
                    <h2 className='mypage-h2'>마이페이지</h2>
                </div>
                    <hr className='mypageline'/>
                    <div className='box'>
                        <div className='line'>
                            <p className='click' onClick={()=> {window.location.assign("/mypage/rentInfo")}}>대출 정보 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/rentHistory")}}>대출 기록 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/reserveInfo")}}>예약 정보 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/reviewInfo")}}>작성한 리뷰</p>
                        </div>
                        <hr className='hr-margin'/>
                        <div className='line'>
                        <p className='click'onClick={()=> {window.location.assign("/mypage/applyInfo")}}>도서 신청 정보</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/likeBook")}}>관심 도서</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/profile")}}>개인정보 변경</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/withdraw")}}>회원 탈퇴</p>
                        </div>
                    </div>
            </div>
        </div>
    )
}

export default Mypage;