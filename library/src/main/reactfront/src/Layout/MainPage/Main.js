import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from '../header/Header';
import Footer from '../Footer/Footer';
import './Main.css'

const Main = () => {

    const [books, setBooks] = useState([]);

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      axios.get('/api/books')
        .then(response => {
          setBooks(response.data);
        })
        .catch(error => {
          console.log(error);
        });
    }, []);
  
    return (
      <div>
        <h2>도서 목록</h2>
        <ul>
          {books.map(book => (
            <li key={book.id}>{book.title}</li>
          ))}
        </ul>
      </div>
    );
};

export default Main;