import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
import './Admin_BookItem.css'

const BookItem_ReserveInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/admin/book/detail/${book.bookId}`);
    };

    const handleReserveRent = () => {
      // 대여 처리 로직
      axios.post(`/api/reserveRent/create?reserveId=${book.reserveId}`)
        .then((response) => {
          Swal.fire({
            icon: "success",
            title: "대여 성공",
            text: `성공적으로 대여됐습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
        })
        .catch(error => {
          if (error.response.data.code === 305) {
            Swal.fire({
              icon: "warning",
              title: "대여 횟수 초과",
              confirmButtonText: "확인",
          })
            console.log("대여 횟수를 초과했습니다");
          }
          console.error('대여 에러', error);
        });
    }; 

    const handleReserveCancle = () => {
      axios.delete(`/api/mypage/reserve/${book.reserveId}`)
      .then(response => {
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "도서 예약 삭제 성공",
            text: `해당 도서 예약을 삭제했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        console.log(error);
        Swal.fire({
          icon: "error",
          title: "도서 예약 삭제 실패",
          confirmButtonText: "확인",
      })
      });
    }
    
    const [reserveFisrt, setReserveFirst] = useState('');
    const [canRent, setCanRent] = useState(false);

    useEffect (() => {
      if (book.waitNumber == 0) {
        setReserveFirst("대여가능")
        setCanRent(true);
      } else {
        setReserveFirst(`대기 ${book.waitNumber}번째 `);
      }
  }, [])
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
        <p>예약 {book.reserveDate}</p>
        <p className="reserve-color">{reserveFisrt}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <div className="col-his">
          <p>{book.personalId} / {book.name}</p>
          <div>
          <button className='return-button' onClick={handleReserveCancle}>취소</button>
          {canRent && <button 
        className = 'reserve-rent-button' 
            onClick={handleReserveRent}>
                대여</button>}
             </div>
          </div>
        </div>
      </div>
    </li>
    </>
  );
};

export default BookItem_ReserveInfo;