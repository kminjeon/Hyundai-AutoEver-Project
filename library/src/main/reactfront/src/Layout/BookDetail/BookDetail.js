import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import "./BookDetail.css"
import Category from '../Category/Category';
import Modal from '../Modal/Modal';
import ReserveButton from "../Button/ReserveButton";
import RentButton from "../Button/RentButton";
import Header from '../Header/Header';
import LikeSet from '../Heart/LikeSet';


const BookDetail = () => {

    const { bookId } = useParams();
  const [reviewList, setReviewList] = useState([]);
  const [rentType, setRentType] = useState();
  const [book, setBook] = useState(null);

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
        console.log(response.data.data)
      } catch (error) {
        console.log(error);
      }
    };
    getBookDetail();
  }, [bookId, personalId]);


  useEffect(() => {
    if (book) {
      setRentType(book.rentType ? '대여가능' : '대여불가');
    }
  }, [book]);
  
  if (!book) {
    return null; 
}

  
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
                    <LikeSet book={book} />
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
            <div className='BookDetail-marginbox'/>
            </div>
                <div className="BookDeatil-rent">
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
      <hr className="divider" />
      <div className='description'>
        <p className = 'intro'>책 소개</p>
        <p className='text'>
        {book.description.split(".").map((sentence, index, array) => (
        <React.Fragment key={index}>
          {sentence.trim()}
          {index !== array.length - 1 && <span>.<br /></span>}
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
          <hr className="divider-review" />
        </div>
      ))}
        </div>
      </div>
    </div>
  );
};

export default BookDetail;
