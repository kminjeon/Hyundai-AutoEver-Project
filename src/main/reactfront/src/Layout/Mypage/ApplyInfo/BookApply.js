import React, {useState } from 'react';
import axios from 'axios';
import Swal from "sweetalert2";
import MypageCategory from '../../Category/MypageCategory';
import Header from '../../Header/Header';

const BookApply = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [addBook, setAddBook] = useState({
        title: '',
        author:'',
        publisher:'',
        isbn:'',
  })

  const OPTIONS = [
    { value: "DEV", name: "개발" },
    { value: "NOVEL", name: "소설" },
    { value: "SCIENCE", name: "과학" },
    { value: "ECONOMY", name: "경제" },
    { value: "HUMANITY", name: "인문" },

    ];

      const handleChange = (e) => {
        setAddBook({ ...addBook, [e.target.name]: e.target.value });
      };

    const handleBookAdd = () => {
        if (addBook.title.length == 0 || addBook.author.length == 0 || addBook.publisher.length == 0 ||
            addBook.isbn.length == 0) {
                Swal.fire({
                    icon: "warning",
                    title: "도서 정보 필요",
                    text: "도서 정보를 모두 입력해주세요",
                    confirmButtonText: "확인",
                })
                return;
            }
        axios.post('/api/apply', {
            title: addBook.title,
            author: addBook.author,
            publisher: addBook.publisher,
            isbn: addBook.isbn,
            personalId : personalId,
          })
          .then(response => {
              console.log('도서 신청 성공')
              console.log(response)
              Swal.fire({
                icon: "success",
                title: "도서 신청 성공",
                text : '도서 신청이 완료되었습니다',
                confirmButtonText: "확인",
            }).then(() => {
                window.location.reload();
              });
          })
          .catch (error => {
          console.log(error);
          Swal.fire({
            icon: "error",
            title: "도서 신청 실패",
            confirmButtonText: "확인",
        })
          console.log('도서 신청 실패')
          });
      }

    return (
        <div>
            <MypageCategory />
            <Header />
            <div className="profile-bigalign">
            <p className="profile-title">도서 신청</p>
            <hr className="profile-line"/>
            <div className='input-container'>
                <label>도서명</label>
                <input
                    className='input'
                    type="text"
                    id="title"
                    name="title"
                    value={addBook.title}
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
                    value={addBook.author}
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
                    value={addBook.publisher}
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
                    value={addBook.isbn}
                    onChange={handleChange}
                />
            </div>
            <button className = 'updateprofile-button' onClick={handleBookAdd}>도서 신청</button>
            </div>
        </div>
    )
}

export default BookApply;