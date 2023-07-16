import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import AdminCategory from '../AdminCategory/AdminCategory';
import AdminHeader from '../Header/AdminHeader';
import './AdminUpdateBook.css'

const AdminBookAdd = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [book, setBook] = useState();
    const [categoryType, setCategoryType] = useState("DEV");
    const [addBook, setAddBook] = useState({
        title: '',
        author:'',
        publisher:'',
        isbn:'',
        description : '',
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



    const onOptionaHandler = (e) => {
        setCategoryType(e.target.value);
        console.log(e.target.value)
    }

    const handleBookAdd = () => {
        if (addBook.title.length == 0 || addBook.author.length == 0 || addBook.publisher.length == 0 ||
            addBook.isbn.length == 0 || addBook.description == 0) {
                alert("도서 정보를 모두 입력해주세요")
                return;
            }
        axios.post('/api/admin/book/add', {
            title: addBook.title,
            author: addBook.author,
            publisher: addBook.publisher,
            categoryType: categoryType,
            isbn: addBook.isbn,
            description : addBook.description
          })
          .then(response => {
              console.log('도서 추가 성공')
              console.log(response)
              alert("도서 추가 완료되었습니다")
              window.location.reload()
          })
          .catch (error => {
          console.log(error);
          console.log('도서 추가 실패')
          });
      }

    return (
        <div>
            <AdminCategory />
            <AdminHeader />
            <div className="profile-bigalign">
            <p className="profile-title">도서 추가</p>
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
                <label>카테고리</label>
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
            <div className='input-container'>
                <label>설명</label>
                <input
                    className='input-description'
                    type="text"
                    id="description"
                    name="description"
                    value={addBook.description}
                    onChange={handleChange}
                />
            </div>
            <button className = 'updateprofile-button' onClick={handleBookAdd}>도서 추가</button>
            </div>
        </div>
    )
}

export default AdminBookAdd;