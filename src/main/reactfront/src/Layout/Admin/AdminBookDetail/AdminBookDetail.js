import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import Swal from "sweetalert2";
import AdminCategory from '../AdminCategory/AdminCategory';
import AdminHeader from '../Header/AdminHeader';

const AdminBookDetail = () => {

    const { bookId } = useParams();
  const [reviewList, setReviewList] = useState([]);
  const [rentType, setRentType] = useState();
  const [book, setBook] = useState(null);
  const OPTIONS = [
    { value: "DEV", name: "개발" },
    { value: "NOVEL", name: "소설" },
    { value: "SCIENCE", name: "과학" },
    { value: "ECONOMY", name: "경제" },
    { value: "HUMANITY", name: "인문" },
  ];
  const [cate, setCate] = useState('');

  // 현재 로그인한 사용자
  const personalId = sessionStorage.getItem('personalId');
  
  useEffect(() => {
    const getBookDetail = async () => {
      try {
        const response = await axios.get(`/api/admin/book/detail/${bookId}`);
        setBook(response.data.data);
        setReviewList(response.data.data.reviewList);
        console.log(response.data.data)
        setCate(OPTIONS.find(option => option.value === response.data.data.categoryType))
      } catch (error) {
        console.log(error);
      }
    };
    getBookDetail();
  }, [bookId]);


  useEffect(() => {
    if (book) {
      setRentType(book.rentType ? '대여가능' : '대여불가');
    }
  }, [book]);
  
  if (!book) {
    return null; 
}

const handleReviewDelete = (reviewId) => {
  axios.delete(`/api/mypage/review/delete/${reviewId}`)
  .then(response => {
      console.log(response)
      Swal.fire({
        icon: "success",
        title: "삭제 성공",
        text: `해당 도서 리뷰을 삭제했습니다`,
        confirmButtonText: "확인",
    }).then(() => {
      window.location.reload();
    });
  })
  .catch (error => {
    console.log(error);
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: `해당 도서 리뷰 삭제를 실패했습니다`,
      confirmButtonText: "확인",
  })
  });
}

  return (
    <div>
        <AdminCategory />
        <AdminHeader />
    <div className= 'headercategoryline'>
        <div className= "book-detail">
            <img className='bookdetailimg' src={`/img/book/${book.isbn}.jpg`} alt={book.title} />
            <div className='book-info'>
                <div className = 'align-detail'>
                  <div className='titleandheart'>
                    <p className = 'title'>{book.title}</p>
                    <p className="LikeSet-loveCount">{book.loveCount}</p>
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
        </div>
      {reviewList.map((review) => (
        <div key={review.reviewId}>
          <div className='xandfirst'>
            <div className='firstline'>
          <p className='autoever margin-rignt-20'>{review.nickname}</p>
          <span className='margin-rignt-20'> | </span>
          <p>{review.createDate}</p>
          </div>
          <img className='x mar-r' src='/img/x.png' alt='x' onClick={() => handleReviewDelete(review.reviewId)} />
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

export default AdminBookDetail;
