import React from 'react';
import {useState, useEffect } from "react";
import axios from 'axios';
import Swal from "sweetalert2";
import CleanModal from "../../Modal/CleanModal";

const BookItem_User_ApplyInfo = ({book, index}) => {


    const personalId = sessionStorage.getItem('personalId');

    const [updateBody, setUpdateBody] = useState({
        title: book.title,
        author: book.author,
        publisher: book.publisher,
        isbn: book.isbn,
  })

    const handleUpdateApply = () => {
        if (updateBody.title.length == 0 && updateBody.author.length == 0 && updateBody.publisher.length == 0 && updateBody.isbn.length == 0) {
            Swal.fire({
              icon: "warning",
              title: "수정 내역 필요",
              text: `수정 내역이 없습니다`,
              confirmButtonText: "확인",
          })
            return;
        }
      axios.put(`/api/apply/${book.applyId}`, {
        title: updateBody.title.length == 0? null : updateBody.title,
        author: updateBody.author.length == 0 ? null : updateBody.author,
        publisher: updateBody.publisher.length == 0 ? null : updateBody.publisher,
        isbn:updateBody.isbn.length == 0 ? null : updateBody.isbn,
      })
      .then(response => {
          console.log('도서 신청 수정 성공')
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "수정 성공",
            text: `해당 도서 신청을 수정했습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch (error => {
      console.log(error);
      Swal.fire({
        icon: "error",
        title: "수정 실패",
        text: `해당 도서 신청을 수정을 실패했습니다`,
        confirmButtonText: "확인",
    })
      console.log('도서 신청 수정 실패')
      });
    }

    const handleApplyDelete = () => {
      axios.delete(`/api/admin/apply/delete?applyId=${book.applyId}`)
      .then(response => {
          console.log('도서 신청 삭제 성공')
          console.log(response)
          Swal.fire({
            icon: "success",
            title: "삭제 성공",
            text: `해당 도서 신청을 삭제했습니다`,
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
          text: `해당 도서 신청 삭제를 실패했습니다`,
          confirmButtonText: "확인",
      })
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

  const handleChange = (e) => {
    setUpdateBody({ ...updateBody, [e.target.name]: e.target.value });
  };

  return (
    <>    
    <li key={book.rentId} className="align separator">
      <div>
        <div className="indexandimg">
        <p className="admin-rentinfo-index">{index}</p>      
        <div>
        <p className="rent-title">{book.title}</p>
        <p className="author">{book.author}</p>
        <p>신청 {book.applyDate}</p>

        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          <div>
          <button className='return-button' onClick={openModal}>수정</button>
          <button className='book-delete-button' onClick={handleApplyDelete} >삭제</button>
          </div>
        </div>
      </div>
      <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                  <div className='apply-center-align'>
                <p>도서 신청 수정</p>
                <div className='input-container'>
                <label>도서명</label>
                <input
                    className='input'
                    type="text"
                    id="title"
                    name="title"
                    value={updateBody.title}
                    onChange={handleChange}
                />
            </div>
            <div className='input-container'>
                <label>저자</label>
                <input
                    className='input'
                    type="text"
                    id="author"
                    name="author"
                    value={updateBody.author}
                    onChange={handleChange}
                />
            </div>
            <div className='input-container'>
                <label>출판사</label>
                <input
                    className='input'
                    type="text"
                    id="publisher"
                    name="publisher"
                    value={updateBody.publisher}
                    onChange={handleChange}
                />
            </div>
                <div className='input-container'>
                <label>isbn</label>
                <input
                    className='input'
                    type="text"
                    id="isbn"
                    name="isbn"
                    value={updateBody.isbn}
                    onChange={handleChange}
                />
            </div>
                <button className='apply-button' onClick={handleUpdateApply}>수정</button>
                </div>
                </CleanModal>
        </React.Fragment>
    </li>
    </>
  );
};

export default BookItem_User_ApplyInfo;