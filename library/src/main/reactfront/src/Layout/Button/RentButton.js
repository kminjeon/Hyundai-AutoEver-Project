import React from "react";
import axios from "axios";
import './Button.css'

const RentButton = ({ personalId, bookId }) => {
    const handleRent = () => {
        // 대여 처리 로직
        axios.post('api/rent/create', null, {
            params: {
                personalId : personalId,
              bookId: bookId
            }})
          .then((response) => {
            // 성공적으로 대여됐을 때 처리할 로직
            console.log('대여 성공');
            alert("성공적으로 대여됐습니다")
            window.location.reload();
          })
          .catch(error => {
            // 대여 처리 중 에러가 발생했을 때 처리할 로직
            console.error('대여 에러', error);
          });
      };  
  return (
        <button 
        className = 'rent-button' 
            onClick={handleRent}>
                대여하기</button>
  );
};

export default RentButton;