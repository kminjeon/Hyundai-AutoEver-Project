import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
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
          Swal.fire({
            icon: "success",
            title: "도서 반납 성공",
            text: `반납되었습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
      console.log(error);
      Swal.fire({
        icon: "error",
        title: "도서 반납 실패",
        text: `반납 실패했습니다`,
        confirmButtonText: "확인",
    })
      });
    }
    
    const handleExtend = () => {
      axios.put(`/api/rent/extend/${book.rentId}`)
      .then(response => {
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "대여 연장 성공",
            text: `도서 대여가 14일 연장되었습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        if (error.response.data.code === 308) {
          Swal.fire({
            icon: "warning",
            title: "대여 연장 횟수 초과",
            text: error.response.data.message,
            confirmButtonText: "확인",
        })
        }
      console.log(error);
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
        <p className="rent-title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        <p>대여 {book.rentDate}</p>
        <p className="red-Latedays"> 반납 {day} ({book.expectedReturnDate} 까지) </p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
        <div className='col-his'>
          <p>{book.personalId} /  {book.name}</p>
          <div>
          <button className='return-button' onClick={handleReturn} >반납</button>
          <button className='book-delete-button' onClick={handleExtend} >연장</button>
          </div>
        </div>
      </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_RentInfo;