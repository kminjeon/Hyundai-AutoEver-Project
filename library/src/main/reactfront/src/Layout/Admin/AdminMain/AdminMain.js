import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AdminHeader from '../Header/AdminHeader';
import CleanModal from '../../Modal/CleanModal';

const AdminMain = () => {

    const name = sessionStorage.getItem('name');
    const personalId = sessionStorage.getItem('personalId');
  
    const [modalOpen, setModalOpen] = useState(false);

    const openModal = () => {
        setModalOpen(true);
    };
    
    const closeModal = () => {
        setModalOpen(false);
      };
    
    const handleSubmit = () => {
      axios.delete(`/api/mypage/withdraw?${personalId}`)
        .then(response => {
          if (response.data.code == 304) {
            console.log("대여 중인 도서 존재")
          } else {
            console.log(response);
            console.log('회원 탈퇴 성공')
          }
        })
        .catch (error => {
        console.log(error);
        console.log('회원 탈퇴 실패')
        });
    }
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
                            <p className='click' onClick={()=> {window.location.assign("/admin/rentHistory")}}>대여 기록 관리</p>
                            <p className='click' onClick={()=> {window.location.assign("/admin/reserveInfo")}}>도서 예약 관리 </p>
                            <p className='click' onClick={()=> {window.location.assign("/admin/applyInfo")}}>도서 신청 관리</p>
                        </div>
                        <hr className='hr-margin'/>
                        <div className='line'>
                        <p className='click' onClick={()=> {window.location.assign("/admin/bookInfo")}}>도서 관리</p>
                            <p className='click' onClick={()=> {window.location.assign("/admin/authInfo")}}>권한 관리</p>
                            <p className='click' onClick={()=> {window.location.assign("/admin/profile")}}>개인정보 변경</p>
                            <p className='click' onClick={openModal}>회원 탈퇴</p>
                        </div>
                    </div> 
            </div>
            <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                <p className='withdraw-msg'>회원 탈퇴하시겠습니까?</p>
                <div className='withdraw-align'>
                    <button className='withdraw-button' onClick={handleSubmit}>확인</button>
                    <button className='withdraw-button' onClick={closeModal}>취소</button>
                </div>
                </CleanModal>
            </React.Fragment>
        </div>
    )
}

export default AdminMain;