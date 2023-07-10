import { useState, useEffect } from "react";
import './BookItem.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const NoNumberBookItem = ({book}) => {

  const navigate = useNavigate();


    const [showHeart, setShowHeart] = useState();
    const [heart, setHeart] = useState();
    const [rentType, setRentType] = useState();
    const [loveCount, setLoveCount] = useState(book.loveCount);

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/book/detail/${book.bookId}`);
    };
    
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
              setHeart("/img/blank_heart.png");
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
              setHeart("/img/full_heart.png");
              setLoveCount(loveCount+1)
              console.log("좋아요 성공");
            })
            .catch((error) => {
              console.error("좋아요 에러", error);
            });
        }
      };

      useEffect(() => {
        if (book) {
          setShowHeart(book.heart);
          setHeart(book.heart ? '/img/full_heart.png' : '/img/blank_heart.png');
          setRentType(book.rentType ? '대여가능' : '대여불가');
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
        axios.post(`/api/reserve/create?personalId=${personalId}&bookId=${book.bookId}`)
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
    <>    
    <li key={book.bookId} className="align separator">
      <div>
        <div className="indexandimg">
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
        <div>
        <p className="title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          
            <div className="rent">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
            {book.rentType ? (
                <button className = 'rent-button' onClick={handleRent}>대여하기</button>
                ) : (
                <button className = 'reserve-button' onClick={handleReserve}>예약하기</button>
            )}
            </div>
            <div className="heart-div">
            <img
            src={heart} 
            alt={showHeart ? "Yes" : "No"}
            className="heart"
            onClick={handleLike}
            />
            <p className="loveCount">{loveCount}</p>
            </div>
        </div>
      </div>
    </li>
    </>
  );
};

export default NoNumberBookItem;