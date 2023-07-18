import React from 'react';
import {useState, useEffect } from "react";
import axios from 'axios';
import Swal from "sweetalert2";
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
        Swal.fire({
          icon: "warning",
          title: "설명 입력",
          text: `설명을 입력해주세요`,
          confirmButtonText: "확인",
      })
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
          Swal.fire({
            icon: "success",
            title: "도서 추가 성공",
            text: `해당 도서를 추가했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        Swal.fire({
          icon: "error",
          title: "도서 추가 실패",
          confirmButtonText: "확인",
      })
      });
    }

    const handleApplyDelete = () => {
      axios.delete(`/api/admin/apply/delete?applyId=${book.applyId}`)
      .then(response => {
          Swal.fire({
            icon: "success",
            title: "도서 삭제 성공",
            text: `해당 도서 신청을 삭제했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
        Swal.fire({
          icon: "error",
          title: "도서 신청 삭제 실패",
          confirmButtonText: "확인",
      })
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
        <p>신청 {book.applyDate}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <div className='col-his'>
          <p>{book.personalId} / {book.name}</p>
          <div>
          <button className='return-button' onClick={openModal}>추가</button>
          <button className='book-delete-button' onClick={handleApplyDelete} >삭제</button>
          </div>
          </div>
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