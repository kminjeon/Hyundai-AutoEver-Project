import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
import './Admin_BookItem.css'

const BookItem_BookInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/admin/book/detail/${book.bookId}`);
    };

    const OPTIONS = [
      { value: "DEV", name: "개발" },
      { value: "NOVEL", name: "소설" },
      { value: "SCIENCE", name: "과학" },
      { value: "ECONOMY", name: "경제" },
      { value: "HUMANITY", name: "인문" },
    ];

    const [rentType, setRentType] = useState();
    const selectedOption = OPTIONS.find(option => option.value === book.categoryType);

      useEffect(() => {
        if (book) {
          setRentType(book.rentType ? '대여가능' : '대여불가');
        }
      }, [book]);
    
      const handleBookDelete = () => {
        axios.delete(`/api/admin/book/delete?bookId=${book.bookId}`)
        .then(response => {
            Swal.fire({
              icon: "warning",
              title: "도서 삭제 성공",
              text: "도서가 삭제되었습니다",
              confirmButtonText: "확인",
          }).then(() => {
            window.location.reload();
          });
        })
        .catch (error => {
        Swal.fire({
          icon: "error",
          title: "도서 삭제 실패",
          confirmButtonText: "확인",
      })
        });
      }

      const handleBookUpdate = () => {
          window.location.assign(`/admin/bookInfo/update/${book.bookId}`);
        
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
        <div className="bookInfo-flex">
        <p>카테고리 
        <span className="category-type-color">{selectedOption.name}</span>
        </p>
        <div className="rent">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
          </div>
          </div>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <button className='return-button' onClick={handleBookUpdate} >수정</button>
          <button className='book-delete-button' onClick={handleBookDelete} >삭제</button>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_BookInfo;