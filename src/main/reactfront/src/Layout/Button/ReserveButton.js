import React from "react";
import axios from "axios";
import Swal from "sweetalert2";
import './Button.css'

const ReserveButton = ({ personalId, bookId }) => {
  const handleReserve = () => {
    // 예약 처리 로직
    axios
      .post(`/api/reserve/create?personalId=${personalId}&bookId=${bookId}`)
      .then(() => {
          console.log("예약 성공");
          Swal.fire({
            icon: "success",
            title: "예약 성공",
            text: `성공적으로 예약됐습니다`,
            confirmButtonText: "확인",
        }).then(() => {
          window.location.reload();
        });
      })
      .catch((error) => {
        if (error.response.data.code === 409) {
          console.log("동일한 예약이 존재합니다");
          Swal.fire({
            icon: "warning",
            title: "동일 예약",
            text: `동일한 예약이 존재합니다`,
            confirmButtonText: "확인",
        })
        } else if (error.response.data.code === 306){
          console.log("이미 대여중인 도서입니다");
          Swal.fire({
            icon: "warning",
            title: "대여 중인 도서",
            text: `이미 대여중인 도서입니다`,
            confirmButtonText: "확인",
        })
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