import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Admin_BookItem.css'

const BookItem_RentInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/admin/book/detail/${book.bookId}`);
    };

    const [day, setDay] = useState();

    useEffect (() => {
        if (book.lateDays < 0) {
            setDay(`D+${book.lateDays * -1}`)
        } else if (book.lateDays == 0) {
          setDay("D-day")
        } else {
          setDay(`D-${book.lateDays}`)
        }
    }, [])

    const handleReturn = () => {
      axios.put(`/api/rent/return?rentId=${book.rentId}`)
      .then(response => {
          console.log('도서 반납 성공')
          console.log(response)
          alert("반납되었습니다")
          window.location.reload()
      })
      .catch (error => {
      console.log(error);
      console.log('도서 반납 실패')
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
          <p>대여일 : {book.rentDate}</p>
          <div>
            <p className="red-Latedays"> {day}</p>
            <p>반납 예정일 : {book.expectedReturnDate}</p>
          </div>
          <p>책 ID : {book.bookId}</p>
          <p>회원 Id: {book.personalId}</p>
          <p>회원 이름: {book.name}</p>
          <button className='return-button' onClick={handleReturn} >반납</button>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_RentInfo;