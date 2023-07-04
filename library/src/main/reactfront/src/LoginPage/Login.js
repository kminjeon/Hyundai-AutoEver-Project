import React, {useEffect, useState} from 'react';
import axios from 'axios';

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
  
      axios({
        method: 'get',
        url:'api/login',
        params: {
          personalId : personalId,
          password : password
        }})
        .then((response) => {
          console.log(response);
          if (response.data.code === -2) {
            // 아이디 없음
  
            console.log("=============================", response.data.msg);
            alert("입력하신 id 가 일치하지 않습니다.");
          } else if (response.data.code === -1) {
            // id 있지만 pw 다른 경우
            console.log(
              "======================",
              "입력하신 비밀번호 가 일치하지 않습니다."
            );
            alert("입력하신 비밀번호 가 일치하지 않습니다.");
          } else if (response.data.code === 200) {
            // id, pw 모두 일치
            console.log("======================", "로그인 성공");
            sessionStorage.setItem("personalId", personalId); // sessionStorage에 id를 personalId라는 key 값으로 저장
            sessionStorage.setItem("name", response.data.data.name); // sessionStorage에 이름을 name key 값으로 저장
  
            // 작업 완료 되면 페이지 이동(새로고침)
          }
            document.location.href = "/";
        })
        .catch(error => {
          console.log(error);
        })
    }
  };

  return (
      <div style={{ 
          display: 'flex', justifyContent: 'center', alignItems: 'center', 
          width: '100%', height: '100vh'
          }}>
          <form onSubmit={onSubmitHandler} style={{ display: 'flex', flexDirection: 'column'}}>
              <label>아이디</label>
              <input type='personalId' value={personalId} onChange={onPersonalIdHandler}/>
              <label>{error.personalId}</label>
              <label>비밀번호</label>
              <input type='password' value={password} onChange={onPasswordHandler}/>
              <br />
              <label>{error.password}</label>
              <button type="submit">
                  Login
              </button>
          </form>
      </div>
  )
}

export default Login;