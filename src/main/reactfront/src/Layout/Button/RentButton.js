import React from "react";
import axios from "axios";
import Swal from "sweetalert2";
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
            Swal.fire({
              icon: "success",
              title: "대여 성공",
              text: `성공적으로 대여됐습니다`,
              confirmButtonText: "확인",
          }).then(() => {
            window.location.reload();
          });
          })
          .catch(error => {
            // 대여 처리 중 에러가 발생했을 때 처리할 로직
            if (error.response.data.code === 305) {
              Swal.fire({
                icon: "warning",
                title: "대여 횟수 초과",
                confirmButtonText: "확인",
            })
              console.log("대여 횟수를 초과했습니다");
            }
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