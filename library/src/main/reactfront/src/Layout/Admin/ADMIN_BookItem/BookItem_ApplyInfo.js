import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './BookItem_RentInfo.css'

const BookItem_ApplyInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/book/detail/${book.bookId}`);
    };

    
  return (
    <>    
    <li key={book.rentId} className="align separator">
      <div>
        <div className="indexandimg">
        <p className="admin-rentinfo-index">{index}</p>      
        <div>
        <p className="title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <p>신청일 : {book.applyDate}</p>
          <p>출판사 : {book.publisher}</p>
          <p>대여 회원 : {book.personalId}  {book.name}</p>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_ApplyInfo;