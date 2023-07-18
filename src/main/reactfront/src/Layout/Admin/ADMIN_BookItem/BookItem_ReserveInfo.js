import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
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
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "도서 예약 삭제 성공",
            text: `해당 도서 예약을 삭제했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        console.log(error);
        Swal.fire({
          icon: "error",
          title: "도서 예약 삭제 실패",
          confirmButtonText: "확인",
      })
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