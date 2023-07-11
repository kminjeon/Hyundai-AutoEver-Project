import React from "react";
import axios from "axios";
import './Button.css'

const ReserveButton = ({ personalId, bookId }) => {
  const handleReserve = () => {
    // 예약 처리 로직
    axios
      .post(`/api/reserve/create?personalId=${personalId}&bookId=${bookId}`)
      .then((response) => {
        // 성공적으로 예약됐을 때 처리할 로직
        if (response.data.code === 409) {
          console.log("동일한 예약이 존재합니다");
        } else {
          console.log("예약 성공");
        }
      })
      .catch((error) => {
        // 예약 처리 중 에러가 발생했을 때 처리할 로직
        console.error("예약 에러", error);
      });
  };

  return (
    <button className="reserve-button" onClick={handleReserve}>
      예약하기
    </button>
  );
};

export default ReserveButton;