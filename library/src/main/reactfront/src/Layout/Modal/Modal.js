import React from 'react';
import './Modal.css';

const Modal = ({ onClose, onConfirm }) => {
  const handleConfirm = () => {
    onConfirm(); // 예약 처리를 수행하는 함수 호출
    onClose(); // 모달 닫기
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>예약</h3>
        <p>예약하시겠습니까?</p>
        <div className="modal-buttons">
          <button className="modal-confirm-btn" onClick={handleConfirm}>확인</button>
          <button className="modal-cancel-btn" onClick={onClose}>취소</button>
        </div>
      </div>
    </div>
  );
};

export default Modal;