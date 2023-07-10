import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Main.css'
import Category from '../Category/Category';
import Header from '../header/Header';
import BookItem from '../Book/BookItem';

const Main = () => {

    const personalId = sessionStorage.getItem('personalId');

    const [books, setBooks] = useState([]);

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      axios.get(`/api/book/best?personalId=${personalId}`)
        .then(response => {
          console.log(response.data.data); 
          setBooks(response.data.data);
        })
        .catch(error => {
          console.log(error);
        });
    }, [personalId]);
  
    return (
      <div>
        <Category />
        <Header />
        <div className='headercategoryline'>
        <div className='best-seller'>
          <label>Best Seller</label>
          <label className='autoever'> 10</label>
        </div>
        <ol className='numbered-list'>
          {books.map((book, index) => {
            return <BookItem key={book.bookId} book ={book} index={index + 1} /> 
          })}
        </ol>
        </div>
      </div>
    );
};

export default Main;