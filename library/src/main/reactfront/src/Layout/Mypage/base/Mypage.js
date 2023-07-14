import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Mypage.css'
import Header from '../../Header/Header';
import CleanModal from '../../Modal/CleanModal';

const Mypage = () => {
    const personalId = sessionStorage.getItem('personalId');
  
    const [modalOpen, setModalOpen] = useState(false);
    const [cmodalOpen, setcModalOpen] = useState(false);

    const [bookList, setBookList] = useState([]);
    const name = sessionStorage.getItem('name');
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
          if (response.data.code == 304) {
            console.log("대여 중인 도서 존재")
            closeModal();
            copenModal();
            setBookList(response.data.data)
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
            <Header />
            <div>
                <div className='mypage-firstline'>
                    <p className='welcome'>{name}님, 환영합니다!</p>
                    <h2 className='mypage-h2'>마이페이지</h2>
                </div>
                    <hr className='mypageline'/>
                    <div className='box'>
                        <div className='line'>
                            <p className='click' onClick={()=> {window.location.assign("/mypage/rentInfo")}}>대여 정보 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/rentHistory")}}>대여 기록 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/reserveInfo")}}>예약 정보 확인</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/reviewInfo")}}>작성한 리뷰</p>
                        </div>
                        <hr className='hr-margin'/>
                        <div className='line'>
                        <p className='click'onClick={()=> {window.location.assign("/mypage/applyInfo")}}>도서 신청 정보</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/likeBook")}}>관심 도서</p>
                            <p className='click'onClick={()=> {window.location.assign("/mypage/profile")}}>개인정보 변경</p>
                            <p className='click'onClick={openModal}>회원 탈퇴</p>
                        </div>
                    </div>
            </div>
            <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                회원 탈퇴하시겠습니까?
                <button className='code-button' onClick={handleSubmit}>확인</button>
                <button className='code-button' onClick={closeModal}>취소</button>
                </CleanModal>
        </React.Fragment>
        <React.Fragment>
                <CleanModal open={cmodalOpen} close={ccloseModal}>
                대여중인 도서가 있습니다. 반납 후 탈퇴 부탁드립니다.
                {bookList && bookList.map((book) => {
                    return <p key={book.bookId}>{book.title}</p>
                })}
                <button className='code-button' onClick={ccloseModal}>확인</button>
                </CleanModal>
        </React.Fragment>
        </div>
    )
}

export default Mypage;