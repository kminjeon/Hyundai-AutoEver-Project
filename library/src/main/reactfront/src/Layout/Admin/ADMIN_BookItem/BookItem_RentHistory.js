import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './BookItem_RentInfo.css'

const BookItem_RentHistory = ({book, index}) => {

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
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
      
        <div>
        <p className="title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <p>대여일 : {book.rentDate}</p>
          <p>반납일 : {book.returnDate}</p>
          <p>책 ID : {book.bookId}</p>
          <p>대여 회원 : {book.personalId}  {book.name}</p>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_RentHistory;