import React from 'react';
import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import axios from 'axios';

const Switch = ({user}) => {

    const [value, setValue] = useState(user.authType == 'ADMIN');
    console.log(user);

    const handleToggle = () => {
        axios.put('/api/admin/auth/update', null, {
            params : {
                personalId : user.personalId,
                auth : user.authType === 'ADMIN' ? 'USER' : 'ADMIN'
            }
        })
        .then(response => {
        console.log(response);
        setValue(!value)
        Swal.fire({
          icon: "success",
          title: "권한 수정 성공",
          text: `성공적으로 권한을 수정했습니다`,
          confirmButtonText: "확인",
      })
        })
        .catch (error => {
        console.log(error);
        });
    }

  return (
    <>
      <input
        checked={value}
        onChange={handleToggle}
        className="react-switch-checkbox"
        id={`react-switch-new`}
        type="checkbox"
      />
      <label
        style={{ background: value && '#9DC8F6' }}
        className="react-switch-label"
        htmlFor={`react-switch-new`}
      >
        관리자 권한
        <span className={`react-switch-button`} />
      </label>
    </>
  );
};

export default Switch;