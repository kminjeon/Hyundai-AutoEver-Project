import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './login.css'
import Signup from './Signup';
import Swal from "sweetalert2";

function Login() {
  const [personalId, setPersonalId] = useState(""); // personalId
  const [password, setPassword] = useState(""); // password

  const [error, setError] = useState({ // 입력 오류시 error
    personalId : "",
    password : "",
  })

  const onPersonalIdHandler = (e) => {
      setPersonalId(e.target.value);
  }
  const onPasswordHandler = (e) => {
      setPassword(e.target.value);
  }

  const validateLogin = () => { 
    let isValid = true;
    
    // personalId 유효성 검사
    if (personalId.trim() === "") {
      setError(prevState => ({
        ...prevState,
        personalId: "아이디를 입력해주세요."
      }));
      isValid = false;
    } else {
      setError(prevState => ({
        ...prevState,
        personalId: ""
      }));
    }

    // password 유효성 검사
    if (password.trim() === "") {
      setError(prevState => ({
        ...prevState,
        password: "비밀번호를 입력해주세요."
      }));
      isValid = false;
    } else {
      setError(prevState => ({
        ...prevState,
        password: ""
      }));
    }

    return isValid;
  }

  const onSubmitHandler = (e) => {
    e.preventDefault();
    console.log("click login");
  
    if (validateLogin()) {
      console.log("personalId : ", personalId);
      console.log("password : ", password);
  
      axios.post('api/login', {
        personalId : personalId,
        password : password
      })
        .then((response) => {
          console.log(response);
         if (response.data.code === 200) {
            // id, pw 모두 일치
            console.log("로그인 성공");
            console.log(response.data);
            sessionStorage.setItem("personalId", personalId); // sessionStorage에 id를 personalId라는 key 값으로 저장
            sessionStorage.setItem("name", response.data.data.name); // sessionStorage에 이름을 name key 값으로 저장
            sessionStorage.setItem("email", response.data.data.email);
            sessionStorage.setItem("authType", response.data.data.authType);
            // 작업 완료 되면 페이지 이동(새로고침)
            if (response.data.data.authType === 'ADMIN')
            {
              document.location.href = "/admin/main";
            } else {
              document.location.href = "/main";
            }
          }
        })
        .catch(error => {
            if (error.response.data.code === -2) {
                // 아이디 없음
                Swal.fire({
                  icon: "warning",
                  title: "아이디 오류",
                  text: `해당하는 ID가 없습니다`,
                  confirmButtonText: "확인",
                })
            } else if (error.response.data.code === -1) {
              // id 있지만 pw 다른 경우
              console.log(
                "입력하신 비밀번호가 일치하지 않습니다."
              );
              Swal.fire({
                icon: "warning",
                title: "비밀번호 오류",
                text: `입력하신 비밀번호가 일치하지 않습니다`,
                confirmButtonText: "확인",
            })
          } else {
                Swal.fire({
                  icon: "error",
                  title: "서버 연결 필요",
                  confirmButtonText: "확인",
              })
          }
          console.log(error);
        })
    }
  };


  const handleReload = () => {
    window.location.reload();
  }

  return (
      <div>
          <form onSubmit={onSubmitHandler} className='centered-form'>
              <img className='login-logo ' src='/img/logo.svg' alt='로고' onClick={handleReload}></img>
              <h2 className='login-h2'>HYUNDAI LIBRARY</h2>
              <input className='input-login' type='personalId' id ='personalId' value={personalId} onChange={onPersonalIdHandler} placeholder='Login'/>
              <div className='error-msg'>{error.personalId}</div>
              <br />
              <input className='input-login' type='password' id = 'password' value={password} onChange={onPasswordHandler} placeholder='Password'/>
              <div className='error-msg'>{error.password}</div>
              <br />
              <div className='find'>
                <label className = 'login-find' onClick={() => {window.location.assign('/FindId');}}>아이디 찾기</label>
                <span> | </span>
                <label className = 'login-find' onClick={() => {window.location.assign('/FindPassword');}}>비밀번호 찾기</label>
              </div>
              <button type="submit" className='signin'>
                  SIGN IN
              </button>
              <div>
                  <label>계정이 없으신가요? </label>
                  <label className='signup' onClick={() => {window.location.assign('/signup');}}>회원가입</label> 
              </div>
          </form>
      </div>
  )
}

export default Login;