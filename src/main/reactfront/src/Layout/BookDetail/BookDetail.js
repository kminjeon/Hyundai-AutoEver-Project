import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import "./BookDetail.css"
import Category from '../Category/Category';
import Modal from '../Modal/Modal';
import Swal from "sweetalert2";
import ReserveButton from "../Button/ReserveButton";
import RentButton from "../Button/RentButton";
import Header from '../Header/Header';
import LikeSet from '../Heart/LikeSet';
import CleanModal from '../Modal/CleanModal';

const BookDetail = () => {

    const { bookId } = useParams();
  const [reviewList, setReviewList] = useState([]);
  const [rentType, setRentType] = useState();
  const [book, setBook] = useState(null);

  const [modalOpen, setModalOpen] = useState(false);

  const [content, setContent] = useState('');
  const OPTIONS = [
    { value: "DEV", name: "개발" },
    { value: "NOVEL", name: "소설" },
    { value: "SCIENCE", name: "과학" },
    { value: "ECONOMY", name: "경제" },
    { value: "HUMANITY", name: "인문" },
  ];

  // 현재 로그인한 사용자
  const personalId = sessionStorage.getItem('personalId');
  const [cate, setCate] = useState('');
  
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
        setCate(OPTIONS.find(option => option.value === response.data.data.categoryType))
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
const openModal = () => {
  setModalOpen(true);
};

const closeModal = () => {
  setModalOpen(false);
};

const handleWrite = () => {
  axios.post(`/api/review/${bookId}`, {
    content : content,
    personalId : personalId
  })
  .then(response => {
      console.log(response);
      console.log('리뷰 등록 성공')
      Swal.fire({
        icon: "success",
        title: "리뷰 등록 성공",
        text : `성공적으로 리뷰를 등록했습니다`,
        confirmButtonText: "확인",
    }).then(() => {
      closeModal();
      window.location.reload();
    });
  })
  .catch (error => {
  console.log(error);
  console.log('리뷰 등록 실패')
  Swal.fire({
    icon: "error",
    title: "리뷰 등록 실패",
    confirmButtonText: "확인",
})
  });
}

const handleInputChange = (e) => {
  setContent(e.target.value);
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
                    <p>{cate.name}</p>
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
          <div className='detail-reivew-align'>
        <p className = 'intro'>리뷰</p>
        <div className='write-dup'>
        <button className='write-reivew-button' onClick={openModal}>리뷰 작성</button>
        <img className='write-img' src={'/img/write.png'} alt={'작성'} />
        </div>
        </div>
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
      <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                  <div className="modal-book-detail">
                <img className='review-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} />
                <div className='modal-line'>
                <p className='review-title'>{book.title}</p>
                <div className='modal-flex'>
                <div className='margin-rignt-20'>
                    <p className='review-width'>저자</p>
                    <p className='review-width'>출판사</p>
                </div>
                <div className='gray'>
                    <p>{book.author}</p>
                    <p>{book.publisher}</p>
                </div>
                </div>
                </div>
                </div>
                <p>*리뷰 작성</p>
                <input className= 'review-input' onChange={handleInputChange}></input>
                <div className='withdraw-align'>
                    <button className='withdraw-button' onClick={handleWrite}>등록</button>
                </div>
                </CleanModal>
        </React.Fragment>
    </div>
  );
};

export default BookDetail;
