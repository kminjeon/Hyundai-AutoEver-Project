import React from "react";
import axios from "axios";
import './Button.css'

const ReserveButton = ({ personalId, bookId }) => {
  const handleReserve = () => {
    // 예약 처리 로직
    axios
      .post(`/api/reserve/create?personalId=${personalId}&bookId=${bookId}`)
      .then(() => {
          console.log("예약 성공");
          alert("성공적으로 예약됐습니다")
          window.location.reload();
      })
      .catch((error) => {
        if (error.response.status === 409) {
          console.log("동일한 예약이 존재합니다");
          alert("동일한 예약이 존재합니다.")
        }
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