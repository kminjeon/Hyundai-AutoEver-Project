import { useState, useEffect } from "react";
import './BookItem.css'
import axios from 'axios';

const BookItem = ({ book }) => {
    const [showHeart, setShowHeart] = useState();
    const [heart, setHeart] = useState();
    const [rentType, setRentType] = useState();
    const [loveCount, setLoveCount] = useState(book.loveCount);

    const personalId = sessionStorage.getItem('personalId');

    const handleLike = () => {
        if (showHeart) {
          // 좋아요 취소
          axios
            .delete('api/love/delete', {
                params: {
                    personalId : personalId,
                  bookId: book.bookId
                },})
            .then((response) => {
              setShowHeart(false);
              setHeart("img/blank_heart.png");
              setLoveCount(loveCount-1)
              console.log("좋아요 취소 성공");
            })
            .catch((error) => {
              console.error("좋아요 취소 에러", error);
            });
        } else {
          // 좋아요
          axios
            .post('api/love/create', null, {
                params: {
                    personalId : personalId,
                  bookId: book.bookId
            },})
            .then((response) => {
              setShowHeart(true);
              setHeart("img/full_heart.png");
              setLoveCount(loveCount+1)
              console.log("좋아요 성공");
            })
            .catch((error) => {
              console.error("좋아요 에러", error);
            });
        }
      };

    useEffect(() => {
        if (book.heart) {
          setShowHeart(true);
          setHeart("img/full_heart.png");
        } else {
          setShowHeart(false);
          setHeart("img/blank_heart.png");
        }
    
        if (book.rentType) {
          setRentType("대여가능");
        } else {
          setRentType("대여불가");
        }
      }, [book]);

      const handleRent = () => {
        // 대여 처리 로직
        axios.post('api/rent/create', null, {
            params: {
                personalId : personalId,
              bookId: book.bookId
            }})
          .then((response) => {
            // 성공적으로 대여됐을 때 처리할 로직
            console.log('대여 성공');
          })
          .catch(error => {
            // 대여 처리 중 에러가 발생했을 때 처리할 로직
            console.error('대여 에러', error);
          });
          document.location.href = "/main";
      };
      
      const handleReserve = () => {
        // 예약 처리 로직
        axios.post('/api/reserve/create', 
        { personalId : personalId, 
            bookId: book.bookId })
          .then(response => {
            // 성공적으로 예약됐을 때 처리할 로직
            console.log('예약 성공');
          })
          .catch(error => {
            // 예약 처리 중 에러가 발생했을 때 처리할 로직
            console.error('예약 에러', error);
          });
      };
      

  return (
    <li key={book.bookId} className="align separator">
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} />
      <div>
        <p className="title">{book.title}</p>
        <p className="author">{book.author}</p>
        <div className="align-right">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
            <img
            src={heart} 
            alt={showHeart ? "Yes" : "No"}
            className="heart"
            onClick={handleLike}
            />
            <p className="loveCount">{loveCount}</p>
            {book.rentType ? (
                <button className = 'rent-button' onClick={handleRent}>대여하기</button>
                ) : (
                <button className = 'reserve-button' onClick={handleReserve}>예약하기</button>
            )}
        </div>
      </div>
    </li>
  );
};

export default BookItem;