import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './LikeSet.css'

const LikeSet = ({book}) => {
    const [loveCount, setLoveCount] = useState();
    const [showHeart, setShowHeart] = useState();
    const [heart, setHeart] = useState();

  const personalId = sessionStorage.getItem('personalId');

    useEffect(() => {
        if (book) {
          setShowHeart(book.heart);
          setLoveCount(book.loveCount);
          setHeart(book.heart ? '/img/full_heart.png' : '/img/blank_heart.png');
        }
      }, [book]);

    const handleLike = () => {
        if (showHeart) {
          // 좋아요 취소
          axios
            .delete('/api/love/delete', {
                params: {
                    personalId : personalId,
                  bookId: book.bookId
                },})
            .then((response) => {
              setShowHeart(false);
              setHeart("/img/blank_heart.png");
              setLoveCount(loveCount-1)
              console.log("좋아요 취소 성공");
            })
            .catch((error) => {
              console.error("좋아요 취소 에러", error);
            });
        } else {
          // 좋아요
          axios
            .post('/api/love/create', null, {
                params: {
                    personalId : personalId,
                  bookId: book.bookId
            },})
            .then((response) => {
              setShowHeart(true);
              setHeart("/img/full_heart.png");
              setLoveCount(loveCount+1)
              console.log("좋아요 성공");
            })
            .catch((error) => {
              console.error("좋아요 에러", error);
            });
        }
      };

    return (
        <div className='LikeSet-heart-div'>
        <img
            src={heart} 
            alt={showHeart ? "Yes" : "No"}
            className="LikeSet-heart-img"
            onClick={handleLike}/>
        <p className="LikeSet-loveCount">{loveCount}</p>
      </div>
    )

}

export default LikeSet;