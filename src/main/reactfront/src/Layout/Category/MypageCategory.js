import React from 'react';
import './MypageCategory.css';
import CleanModal from '../Modal/CleanModal';
import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import axios from 'axios';

const MypageCategory = () => {
  const personalId = sessionStorage.getItem('personalId');

  const categories = [
    { id: 1, name: '대여 정보', link: 'rentInfo' },
    { id: 2, name: '대여 기록', link: 'rentHistory' },
    { id: 3, name: '예약 정보', link: 'reserveInfo' },
    { id: 4, name: '작성한 리뷰', link: 'reviewInfo' },
    { id: 5, name: '도서 신청 내역', link: 'applyInfo' },
    { id: 6, name: '관심 도서', link: 'likeBook' },
    { id: 7, name: '개인 정보 변경', link: 'profile' },
    { id: 8, name: '회원 탈퇴', link: 'withdraw' },
  ];

  const handleCategoryClick = (link) => {
    window.location.assign(`/mypage/${link}`);
  };

  const [modalOpen, setModalOpen] = useState(false);
  const [cmodalOpen, setcModalOpen] = useState(false);

  const [bookList, setBookList] = useState([]);
  const openModal = () => {
      setModalOpen(true);
  };
  
  const closeModal = () => {
      setModalOpen(false);
    };

    const copenModal = () => {
      setcModalOpen(true);
  };
  
  const ccloseModal = () => {
      setcModalOpen(false);
    };
  
  
  const handleSubmit = () => {
    axios.delete(`/api/mypage/withdraw?personalId=${personalId}`)
      .then(response => {
        if (response.data.code == 311) {
          console.log("대여 중인 도서 존재")
          closeModal();
          copenModal();
          setBookList(response.data.data)
        } else {
          sessionStorage.removeItem('personalId');
          sessionStorage.removeItem('name');
          sessionStorage.removeItem("email");
          sessionStorage.removeItem("authType");
          Swal.fire({
            icon: "success",
            title: "회원 탈퇴 성공",
            confirmButtonText: "확인",
        }).then(() => {
          window.location.assign('/');
        });
        }
      })
      .catch (error => {
      console.log(error);
      Swal.fire({
        icon: "error",
        title: "회원 탈퇴 실패",
        confirmButtonText: "확인",
    })
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
              handleCategoryClick(category.link)}
            }}>
            {category.name}
          </li>
        ))}
      </ul>
      <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                <p className='withdraw-msg'>회원 탈퇴하시겠습니까?</p>
                <div className='withdraw-align'>
                    <button className='withdraw-button' onClick={handleSubmit}>확인</button>
                    <button className='withdraw-button' onClick={closeModal}>취소</button>
                </div>
                </CleanModal>
        </React.Fragment>
        <React.Fragment>
                <CleanModal open={cmodalOpen} close={ccloseModal}>
                <p className='withdraw-msg'>대여중인 도서가 있습니다. 반납 후 탈퇴 부탁드립니다.</p>
                <div className='a-withdraw-align'>
                  {bookList && bookList.map((book) => {
                      return <p key={book.bookId}>[ {book.title} ]</p>
                  })}
                  <button className='withdraw-button' onClick={ccloseModal}>확인</button>
                </div>
                </CleanModal>
        </React.Fragment>
    </div>
  );
};

export default MypageCategory;
