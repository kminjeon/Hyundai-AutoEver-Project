import React from 'react';
import { useState, useEffect } from "react";
import axios from 'axios';
import CleanModal from '../../Modal/CleanModal';


const AdminCategory = () => {

  const categories = [
    { id: 1, name: '대여 현황 관리', link: 'rentInfo' },
    { id: 2, name: '대여 기록 관리', link: 'rentHistory' },
    { id: 3, name: '도서 예약 관리', link: 'reserveInfo' },
    { id: 4, name: '도서 신청 관리', link: 'applyInfo' },
    { id: 5, name: '도서 관리', link: 'bookInfo' },
    { id: 6, name: '권한 관리', link: 'authInfo' },
    { id: 7, name: '개인 정보 변경', link: 'profile' },
    { id: 8, name: '회원 탈퇴', link: 'withdraw' },
  ];
  const personalId = sessionStorage.getItem('personalId');
  
  const [modalOpen, setModalOpen] = useState(false);

  const handleCategoryClick = (link) => {
    window.location.assign(`/admin/${link}`);
  };

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
    <div className="mypagecategoryalign">
      <ul className="mycategory-list">
        {categories.map(category => (
          <li key={category.id} onClick={() => {
            if (category.id === 8) {
              openModal();
            } else {
              handleCategoryClick(category.link)
            }
          }}>
            {category.name}
          </li>
        ))}
      </ul>
      <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                회원 탈퇴하시겠습니까?
                <button className='code-button' onClick={handleSubmit}>확인</button>
                <button className='code-button' onClick={closeModal}>취소</button>
                </CleanModal>
      </React.Fragment>
    </div>
  );
};

export default AdminCategory;
