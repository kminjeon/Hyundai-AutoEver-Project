import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import "./BookDetail.css"
import Category from '../Category/Category';
import Header from '../header/Header';
import Modal from '../Modal/Modal';
import ReserveButton from "../Button/ReserveButton";
import RentButton from "../Button/RentButton";

const BookDetail = () => {

    const { bookId } = useParams();
  const [reviewList, setReviewList] = useState([]);
  const [rentType, setRentType] = useState();
  const [heart, setHeart] = useState();
  const [book, setBook] = useState(null);
  const [loveCount, setLoveCount] = useState();
  const [showHeart, setShowHeart] = useState();

  const [showPopup, setShowPopup] = useState(false); // 팝업 창 표시 여부


  // 현재 로그인한 사용자
  const personalId = sessionStorage.getItem('personalId');
  
  useEffect(() => {
    const getBookDetail = async () => {
      try {
        const response = await axios.get(`/api/book/detail/${bookId}`, {
          params: {
            personalId: personalId
          }
        });
        setBook(response.data.data);
        setReviewList(response.data.data.reviewList);
        setLoveCount(response.data.data.loveCount);
        console.log(response.data.data)
      } catch (error) {
        console.log(error);
      }
    };
    getBookDetail();
  }, [bookId, personalId]);


  useEffect(() => {
    if (book) {
      setShowHeart(book.heart);
      setHeart(book.heart ? '/img/full_heart.png' : '/img/blank_heart.png');
      setRentType(book.rentType ? '대여가능' : '대여불가');
    }
  }, [book]);
  
  if (!book) {
    return null; 
}


  const handleLike = () => {
    if (showHeart) {
      // 좋아요 취소
      axios
        .delete('/api/love/delete', {
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
        .post('/api/love/create', null, {
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
  
  return (
    <div>
        <Category />
        <Header />
    <div className= 'headercategoryline'>
        <div className= "book-detail">
            <img className='bookdetailimg' src={`/img/book/${book.isbn}.jpg`} alt={book.title} />
            <div className='book-info'>
                <div className = 'align-detail'>
                  <div className='titleandheart'>
                    <p className = 'title'>{book.title}</p>
                    <div className='heart-div'>
                      <img
                          src={heart} 
                          alt={showHeart ? "Yes" : "No"}
                          className="heart-img"
                          onClick={handleLike}/>
                      <p className="loveCount">{loveCount}</p>
                    </div>
                    </div>
                  <div className="flex-container">
                <div className='margin-rignt-40'>
                    <p>저자</p>
                    <p>출판사</p>
                    <p>카테고리</p>
                    <p>ISBN</p>
                </div>
                <div className='gray'>
                    <p>{book.author}</p>
                    <p>{book.publisher}</p>
                    <p>{book.categoryType}</p>
                    <p>{book.isbn}</p>
                </div>
            </div>
            <div className="rent">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
            {book.rentType ? (
                  <RentButton personalId={personalId} bookId={book.bookId} />
                ) : 
                  (
                    <ReserveButton personalId={personalId} bookId={book.bookId} />
                  )}
            </div>
                </div>
            </div>
      </div>
      <hr className="divider" />
      <div className='description'>
        <p className = 'intro'>책 소개</p>
        <p className='text'>
        {book.description.split(".").map((sentence, index) => (
          <React.Fragment key={index}>
            {sentence.trim()}
            {index !== book.description.length - 1 && <br />}
          </React.Fragment>
        ))}
      </p>
        </div>
        <hr className="divider" />
        <div className='review-div'>
        <p className = 'intro'>리뷰</p>
      {reviewList.map((review) => (
        <div key={review.reviewId}>
          <div className='firstline'>
          <p className='autoever margin-rignt-20'>{review.nickname}</p>
          <span className='margin-rignt-20'> | </span>
          <p>{review.createDate}</p>
          </div>
          <p>{review.content}</p>
        </div>
      ))}
        </div>
      </div>
    </div>
  );
};

export default BookDetail;
