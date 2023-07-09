import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Main.css'

const Main = () => {

    const [books, setBooks] = useState([]);

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      axios.get('/api/book/best')
        .then(response => {
          setBooks(response.data.data);
          console.log(books);
        })
        .catch(error => {
          console.log(error);
        });
    }, []);

    useEffect(() => {
        console.log(books);
      }, [books]); 
  
    return (
      <div>
        <h2>도서 목록</h2>
        <ul>
          {books.map(book => (
            <li key={book.bookId}>{book.title}</li>
          ))}
        </ul>
      </div>
    );
};

export default Main;