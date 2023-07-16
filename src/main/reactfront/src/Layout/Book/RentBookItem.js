import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


// /mypage/rentInfo
const RentBookItem = ({book}) => {

  const navigate = useNavigate();

  const handleBookClick = () => {
    navigate(`/book/detail/${book.bookId}`);
  };

    const personalId = sessionStorage.getItem('personalId');

  return (
    <li key={book.bookId}>
      <img src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
        <p onClick={handleBookClick}>{book.title}</p>
        <p>{book.author}</p>
    </li>
  );
};

export default RentBookItem;