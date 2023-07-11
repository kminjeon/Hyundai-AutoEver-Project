import { useState, useEffect } from "react";
import './BookItem.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ReserveButton from "../Button/ReserveButton";
import RentButton from "../Button/RentButton";

const BookItem = ({book, index}) => {

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


  return (
    <>    
    <li key={book.bookId} className="align separator">
      <div>
        <div className="indexandimg">
        <p className="index">{index}</p>
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
                  <RentButton personalId={personalId} bookId={book.bookId} />
                ) : 
                  (
                    <ReserveButton personalId={personalId} bookId={book.bookId} />
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

export default BookItem;