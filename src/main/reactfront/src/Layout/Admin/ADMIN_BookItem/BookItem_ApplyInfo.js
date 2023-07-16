import React from 'react';
import {useState, useEffect } from "react";
import axios from 'axios';
import './Admin_BookItem.css'
import CleanModal from "../../Modal/CleanModal";

const BookItem_ApplyInfo = ({book, index}) => {


    const personalId = sessionStorage.getItem('personalId');
    const [categoryType, setCategoryType] = useState('DEV');
    const [description, setDescription] = useState('');

    const OPTIONS = [
      { value: "DEV", name: "개발" },
      { value: "NOVEL", name: "소설" },
      { value: "SCIENCE", name: "과학" },
      { value: "ECONOMY", name: "경제" },
      { value: "HUMANITY", name: "인문" },
      ];
  
    const handleBookAdd = () => {
      if (description.length == 0 ) {
        alert("설명을 입력해주세요")
        return;
      }
      axios.post('/api/admin/book/add', {
         title : book.title,
        author: book.author,
        publisher : book.publisher,
         isbn : book.isbn,
         categoryType : categoryType,
         description: description,
      })
      .then(response => {
          console.log('도서 추가 성공')
          console.log(response)
          alert("해당 도서를 추가했습니다")
          window.location.reload()
      })
      .catch (error => {
      console.log(error);
      console.log('도서 추가 실패')
      });
    }

    const handleApplyDelete = () => {
      axios.delete(`/api/admin/apply/delete?applyId=${book.applyId}`)
      .then(response => {
          console.log('도서 신청 삭제 성공')
          console.log(response)
          alert("해당 도서 신청을 삭제했습니다")
          window.location.reload()
      })
      .catch (error => {
        console.log(error);
        console.log('도서 신청 삭제 실패')
      });
    }

    const [modalOpen, setModalOpen] = useState(false);

    const openModal = () => {
      setModalOpen(true);
    };
  
  const closeModal = () => {
      setModalOpen(false);
    };

    const onOptionaHandler = (e) => {
      setCategoryType(e.target.value);
  }

  const handleDescription = (e) => {
    setDescription(e.target.value);
  };

  return (
    <>    
    <li key={book.rentId} className="align separator">
      <div>
        <div className="indexandimg">
        <p className="admin-rentinfo-index">{index}</p>      
        <div>
        <p className="title">{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <p>신청일 : {book.applyDate}</p>
          <p>출판사 : {book.publisher}</p>
          <p>회원 Id: {book.personalId}</p>
          <p>회원 이름: {book.name}</p>
          <button className='return-button' onClick={openModal}>추가</button>
          <button className='book-delete-button' onClick={handleApplyDelete} >삭제</button>
        </div>
      </div>
      <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                  <div className='apply-center-align'>
                <p>카테고리와 도서 설명을 입력해주세요</p>
                    <select className='update-book-select-box' onChange={onOptionaHandler} defaultValue={categoryType}> 
                  {OPTIONS.map((option) => (
                    <option
                      key={option.value}
                      value={option.value}
                    >
                      {option.name}
                    </option>
                  ))}
                </select>
                <div className='input-apply-container'>
                <label className='apply-desc'>설명</label>
                <input
                    className='input-description'
                    type="text"
                    id="description"
                    name="description"
                    value={description}
                    onChange={handleDescription}
                />
            </div>
                <button className='apply-button' onClick={handleBookAdd}>확인</button>
                </div>
                </CleanModal>
        </React.Fragment>
    </li>
    </>
  );
};

export default BookItem_ApplyInfo;