import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Admin_BookItem.css'

const BookItem_BookInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/admin/book/detail/${book.bookId}`);
    };

    const [rentType, setRentType] = useState();
    const [updateBody, setUpdateBody] = useState({
          title: '',
          author:'',
          publisher:'',
          categoryType:'',
          isbn:'',
          description : '',
    })
    
      useEffect(() => {
        if (book) {
          setRentType(book.rentType ? '대여가능' : '대여불가');
        }
      }, [book]);
    
      const handleBookDelete = () => {
        axios.delete(`/api/admin/book/delete?bookId=${book.bookId}`)
        .then(response => {
            console.log('도서 삭제 성공')
            console.log(response)
            window.location.reload()
        })
        .catch (error => {
        console.log(error);
        console.log('도서 삭제 실패')
        });
      }

      const handleBookUpdate = () => {
        axios.put(`/api/admin/book/update/${book.bookId}`, {
          title: updateBody.title.length == 0? null : updateBody.title,
          author: updateBody.author.length == 0 ? null : updateBody.author,
          publisher: updateBody.publisher.length == 0 ? null : updateBody.publisher,
          categoryType: updateBody.categoryType.length == 0? null : updateBody.categoryType,
          isbn:updateBody.isbn.length == 0 ? null : updateBody.isbn,
          description : updateBody.description.length == 0? null : updateBody.description
        })
        .then(response => {
            console.log('도서 삭제 성공')
            console.log(response)
            window.location.reload()
        })
        .catch (error => {
        console.log(error);
        console.log('도서 삭제 실패')
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
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <p>도서 ID : {book.bookId}</p>
          <p>카테고리 : {book.categoryType} </p>
          <div className="rent">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
          </div>
          <button className='return-button' onClick={handleBookUpdate} >수정</button>
          <button className='book-delete-button' onClick={handleBookDelete} >삭제</button>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_BookInfo;