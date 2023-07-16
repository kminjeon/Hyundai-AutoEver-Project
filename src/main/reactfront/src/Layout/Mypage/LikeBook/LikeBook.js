import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from '../../Header/Header';
import MypageCategory from '../../Category/MypageCategory';
import './LikeBook.css'
import LikeBookItem from './LikeBookItem';

const LikeBook = () => {

    const [likeBookList, setLikeBookList] = useState([]);
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자

    useEffect(() => {
         axios.get(`/api/love/get?personalId=${personalId}`)
         .then(response => {
            setLikeBookList(response.data.data);
            //console.log(response.data.data)
         })
         .catch (error => {
            console.log(error);
          });
      }, [personalId]);



    return (
        <>
        <MypageCategory />
        <Header />
        <div className='likeheadermargin'>

            <h2 className='likebook-h2'>관심 도서</h2>
            <hr />
            <div className='box-margin'>
                {likeBookList.map((book) => {
                return <LikeBookItem key={book.bookId} personalId = {personalId} book ={book}/> 
            })}
            </div>
        </div>
        </>
    )
}

export default LikeBook;