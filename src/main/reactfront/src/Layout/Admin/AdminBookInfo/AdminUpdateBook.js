import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import AdminCategory from '../AdminCategory/AdminCategory';
import AdminHeader from '../Header/AdminHeader';
import './AdminUpdateBook.css'

const AdminUpdateBook = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [book, setBook] = useState();
    const { bookId } = useParams();
    const [categoryType, setCategoryType] = useState();
    const [updateBody, setUpdateBody] = useState({
        title: '',
        author:'',
        publisher:'',
        categoryType:'',
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

      useEffect(() => { // 처음 도서 정보 가져오기
        const getBook = async () => {
          try {
            const response = await axios.get(`/api/admin/book/get/${bookId}`);
            setBook(response.data.data);
            console.log(response.data.data)
            setCategoryType(response.data.data.categoryType);
          } catch (error) {
            console.log(error);
          }
        };
        getBook();
      }, [bookId]);

      if (!book) {
        return null;
      }

      const handleChange = (e) => {
        setUpdateBody({ ...updateBody, [e.target.name]: e.target.value });
      };


      const handleBookUpdate = () => {
        axios.put(`/api/admin/book/update/${bookId}`, {
          title: updateBody.title.length == 0? null : updateBody.title,
          author: updateBody.author.length == 0 ? null : updateBody.author,
          publisher: updateBody.publisher.length == 0 ? null : updateBody.publisher,
          categoryType: categoryType == book.categoryType ? null : categoryType,
          isbn:updateBody.isbn.length == 0 ? null : updateBody.isbn,
          description : updateBody.description.length == 0? null : updateBody.description
        })
        .then(response => {
            console.log('도서 수정 성공')
            console.log(response)
            alert("도서 수정 완료되었습니다")
            window.location.reload()
        })
        .catch (error => {
        console.log(error);
        console.log('도서 수정 실패')
        });
      }

      const onOptionaHandler = (e) => {
        setCategoryType(e.target.value);
        console.log(e.target.value)
    }

    return (
        <div>
            <AdminCategory />
            <AdminHeader />
            <div className="profile-bigalign">
            <p className="profile-title">도서 수정</p>
            <hr className="profile-line"/>
            <div className='input-container'>
                <label>도서명</label>
                <input
                    className='input'
                    type="text"
                    id="title"
                    name="title"
                    value={updateBody.title}
                    onChange={handleChange}
                    placeholder={book.title}
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
                    placeholder={book.author}
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
                    placeholder={book.publisher}
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
                    value={updateBody.isbn}
                    onChange={handleChange}
                    placeholder={book.isbn}
                />
            </div>
            <div className='input-container'>
                <label>설명</label>
                <input
                    className='input-description'
                    type="text"
                    id="description"
                    name="description"
                    value={updateBody.description}
                    onChange={handleChange}
                    placeholder={book.description}
                />
            </div>
            <button className = 'updateprofile-button' onClick={handleBookUpdate}>도서 정보 수정</button>
            </div>
        </div>
    )
}

export default AdminUpdateBook;