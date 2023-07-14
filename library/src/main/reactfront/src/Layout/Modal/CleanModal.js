import React from 'react';
import './CleanModal.css';

const CleanModal = (props) => {
  // 열기, 닫기, 모달 헤더 텍스트를 부모로부터 받아옴
  const { open, close, header} = props;

  return (
    // 모달이 열릴때 openModal 클래스가 생성된다.
    <div className={open ? 'openModal cmodal' : 'cmodal'}>
      {open ? (
        <section>
        <header>
          {header}
          <button className="close" onClick={close}>
            &times;
          </button>
        </header>
        <main>{props.children}</main>
        </section>
      ) : null}
    </div>
  );
};


export default CleanModal;