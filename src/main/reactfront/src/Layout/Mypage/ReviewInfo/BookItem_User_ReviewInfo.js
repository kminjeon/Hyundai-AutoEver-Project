import React, { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
import CleanModal from "../../Modal/CleanModal";

const BookItem_User_ReviewInfo = ({book, index}) => {

    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');
    const [modalOpen, setModalOpen] = useState(false);

    const [content, setContent] = useState('');
    
    const handleBookClick = () => {
      navigate(`/book/detail/${book.bookId}`);
    };

    const handleReviewDelete = () => {
      axios.delete(`/api/mypage/review/delete/${book.reviewId}`)
      .then(response => {
          console.log('도서 리뷰 삭제 성공')
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "리뷰 삭제 성공",
            text: `해당 도서 리뷰을 삭제했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        console.log(error);
        console.log('도서 리뷰 삭제 실패')
        Swal.fire({
          icon: "error",
          title: "도서 리뷰 삭제 실패",
          confirmButtonText: "확인",
      })
      });
    }

    const openModal = () => {
        setModalOpen(true);
      };
      
      const closeModal = () => {
        setModalOpen(false);
      };
      
      const handleReviewUpdate = () => {
        if ( content.length == 0) {
          Swal.fire({
            icon: "warning",
            title: "수정 내용 필요",
            text: `수정된 내용이 없습니다`,
            confirmButtonText: "확인",
        })
            return;
        }
        axios.put(`/api/mypage/review/${book.reviewId}`, {
          content :content,
        })
        .then(response => {
            console.log(response);
            console.log('리뷰 수정 성공')
            Swal.fire({
              icon: "success",
              title: "리뷰 수정 성공",
              text: `성공적으로 리뷰를 수정했습니다`,
              confirmButtonText: "확인",
          }).then(() => {
            closeModal();
            window.location.reload();
          });
        })
        .catch (error => {
        console.log(error);
        console.log('리뷰 수정 실패')
        });
      }
      
      const handleInputChange = (e) => {
        setContent(e.target.value);
      };

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
          <p>리뷰 작성일 : {book.reviewDate}</p>
          <button className='return-button' onClick={openModal}>수정</button>
          <button className='book-delete-button' onClick={handleReviewDelete}>삭제</button>
        </div>
      </div>
    </li>
    <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                  <div className="modal-book-detail">
                <img className='review-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} />
                <div className='modal-line'>
                <p className='review-title'>{book.title}</p>
                <div className='modal-flex'>
                <div className='margin-rignt-40'>
                    <p>저자</p>
                    <p>출판사</p>
                </div>
                <div className='gray'>
                    <p>{book.author}</p>
                    <p>{book.publisher}</p>
                </div>
                </div>
                </div>
                </div>
                <p>*리뷰 작성</p>
                <input className= 'review-input' onChange={handleInputChange} placeholder={book.content}></input>
                <div className='withdraw-align'>
                    <button className='withdraw-button' onClick={handleReviewUpdate}>수정</button>
                </div>
                </CleanModal>
        </React.Fragment>
    </>
  );
};

export default BookItem_User_ReviewInfo;