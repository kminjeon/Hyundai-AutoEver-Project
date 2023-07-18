import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Admin_BookItem.css'

const BookItem_ReserveInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/admin/book/detail/${book.bookId}`);
    };

    const handleReserveCancle = () => {
      axios.delete(`/api/mypage/reserve/${book.reserveId}`)
      .then(response => {
          console.log('도서 예약 삭제 성공')
          console.log(response)
          alert("해당 도서 예약을 삭제했습니다")
          window.location.reload()
      })
      .catch (error => {
        console.log(error);
        console.log('도서 예약 삭제 실패')
      });
    }

  return (
    <>    
    <li key={book.rentId} className="align separator">
      <div>
        <div className="indexandimg">
        <p className="admin-rentinfo-index">{index}</p>
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
      
        <div>
        <p className="title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        <p>예약 {book.reserveDate}</p>
        <p>대기 {book.waitNumber}번째 </p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <div className="col-his">
          <p>{book.personalId} / {book.name}</p>
          <button className='return-button' onClick={handleReserveCancle}>취소</button>
          </div>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_ReserveInfo;