import React, { useState } from 'react';
import './Signup.css'
import axios from 'axios';

const Signup = () => { 

    const [formData, setFormData] = useState({
        name: '',
        personalId: '',
        password: '',
        confirmPassword: '',
        email: '',
        part : "MOBIS",
        nickname: '',
        agree : false, // 이용약관 동의 여부
      });


    const [dupData, setDupData] = useState ({
      personalId : false,
      email : false,
      nickname : false
    })

    const OPTIONS = [
      { value: "MOBIS", name: "모비스연구소시스템" },
      { value: "PLATFORM", name: "차량SW플랫폼" },
      { value: "FACTORY", name: "스마트팩토리" },
      { value: "NAVIGATION", name: "내비게이션" },
  ];
  
  const onOptionaHandler = (e) => {
    setFormData(e.target.value);
    console.log(e.target.value)
}

      const [showPassword, setShowPassword] = useState(false);
      const [eyeIcon, setEyeIcon] = useState('img/eye_close.png');
      const [passwordMismatch, setPasswordMismatch] = useState(false);
    
      const handleChange = (e) => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
        setFormData({ ...formData, [e.target.name]: value });
        if (e.target.name === 'personalId' || e.target.name === 'email' || e.target.name === 'nickname') {
          setDupData({...dupData, [e.target.name]: false });
      }
      };
    
      const handleCheckIdDuplicate = () => {
        if (formData.personalId == '') 
        {
          alert("아이디를 입력해주세요")
          return;
        }
        // 아이디 중복확인 로직
        axios
        .get(`/api/check/personalId/${formData.personalId}`)
        .then((response) => {
          if (response.data.code == 409) {
            console.log("아이디 중복");
          } else {
            alert("아이디 중복확인 완료")
            console.log("아이디 성공");
            setDupData({...dupData, 'personalId': true})
          }
        })
        .catch((error) => {
          console.error("아이디 중복 확인 에러", error);
        });
      };

      const handleCheckEmailDuplicate = () => {
        if (formData.email == '') 
        {
          alert("이메일을 입력해주세요")
          return;
        }
        // 이메일 중복확인 로직
        axios
        .get(`/api/check/email/${formData.email}`)
        .then((response) => {
          if (response.data.code == 409) {
            console.log("이메일 중복");
          } else {
            alert("이메일 중복확인 완료")
            console.log("이메일 성공");
            setDupData({...dupData, 'email' : true})
          }
        })
        .catch((error) => {
          console.error("이메일 중복 확인 에러", error);
        });
      };

      const handleCheckNicknameDuplicate = () => {
        if (formData.nickname == '') 
        {
          alert("닉네임을 입력해주세요")
          return;
        }
        // 닉네임 중복확인 로직
        axios
        .get(`/api/check/nickname/${formData.nickname}`)
        .then((response) => {
          if (response.data.code == 409) {
            console.log("닉네임 중복");
          } else {
            alert("닉네임 중복확인 완료")
            console.log("닉네임 성공");
            setDupData({nickname : true})
            setDupData({...dupData, 'nickname' : true});
          }
        })
        .catch((error) => {
          console.error("닉네임 중복 확인 에러", error);
        });
      };
      const handleSubmit = (e) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
          alert("비밀번호가 동일하지 않습니다")  
          console.log("비밀번호 틀림")
            setPasswordMismatch(true);
            return;
        } else {
          setPasswordMismatch(false);
          console.log(formData);
        }
        if (!dupData.personalId || !dupData.email || !dupData.nickname) {
          alert("중복확인은 필수입니다")
          console.log("중복확인 안함")
          console.log(dupData)
          return;
        }
        axios
            .post('/api/join', {
                name : formData.name,
                personalId : formData.personalId,
                password : formData.password,
                email : formData.email,
                partType : formData.part,
                nickname : formData.nickname
                })
            .then((response) => {
              console.log("회원가입 성공");
              alert("회원가입 되었습니다")
            sessionStorage.setItem("personalId", formData.personalId); // sessionStorage에 이름을 name key 값으로 저장
            sessionStorage.setItem("name", formData.name); // sessionStorage에 이름을 name key 값으로 저장
            sessionStorage.setItem("email", formData.email);
            sessionStorage.setItem("authType", 'USER');  
            window.location.assign('/signup/finish');
            })
            .catch((error) => {
              console.error("회원가입 에러", error);
            });
      };

      const isButtonDisabled = !formData.agree; // agree 값이 false일 때 버튼 비활성화

    
      return (
        <div className='join-centered-form'>
        <h2 className='join-h2'>Sign up</h2>
        <div className='input-container'>
        <label htmlFor="name"> *이름</label>
            <input
            className='input'
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
            />
          </div>
          <div className='input-container'>
          <label htmlFor="personalId"> *아이디</label>
          <div className='align-dup'>
            <input
            className='input'
              type="text"
              id="personalId"
              name="personalId"
              value={formData.personalId}
              onChange={handleChange}
            />
             <button className = 'dup' type='button' onClick={handleCheckIdDuplicate}>
              중복확인
            </button>
            </div>
          </div>
          <div className='input-container'>
          <label htmlFor="password"> *비밀번호</label>
          <div className='password-input'>
            <input
            className='input'
            type={showPassword ? 'text' : 'password'}
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
            <img
                src ={eyeIcon}
                alt = {showPassword ? "숨기기" : "보이기"}
                className='eye-icon-button'
                onClick={() => {
                    setShowPassword(!showPassword);
                    setEyeIcon(showPassword ? 'img/eye_open.png' :'img/eye_close.png')
                    }} />
            </div>
          </div>
          <div className='input-container'>
          <label htmlFor="password"> *비밀번호 확인</label>
            <input
            className='input'
            type='password'
            id="confirmPassword"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            />
          {passwordMismatch && <p className="error-message">비밀번호가 일치하지 않습니다.</p>}
          </div>
          <div className='input-container'>
          <label htmlFor="email"> *이메일</label>
          <div className='align-dup'>
            <input
            className='input'
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
            <button className = 'dup' vtype='button' onClick={handleCheckEmailDuplicate}>
              중복확인
            </button>
            </div>
          </div>
          <div className='input-container'>
                <label>*부서</label>
            <select className='update-book-select-box' onChange={onOptionaHandler} defaultValue={formData.part}> 
                        {OPTIONS.map((option) => (
                            <option
                                key={option.value}
                                value={option.value}
                            >
                                {option.name}
                            </option>
                        ))}
                </select>
            </div>
          <div className='input-container'>
          <label htmlFor="nickname"> *닉네임</label>
          <div className='align-dup'>
            <input
                className='input'
              type="nickname"
              id="nickname"
              name="nickname"
              value={formData.nickname}
              onChange={handleChange}
            />
            <button className = 'dup' type='button' onClick={handleCheckNicknameDuplicate}>
              중복확인
            </button>
            </div>
          </div>
          <div>
        <label>
          <input
            type="checkbox"
            name="agree"
            checked={formData.agree}
            onChange={handleChange}
          /> 이용약관에 모두 동의합니다.
        </label>
      </div>
          <button className='join-button' onClick={handleSubmit} disabled={isButtonDisabled}>회원가입</button>
        </div>
      );
};

export default Signup;