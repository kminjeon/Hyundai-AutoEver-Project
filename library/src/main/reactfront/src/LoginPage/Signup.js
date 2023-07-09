import React, { useState } from 'react';
import './Signup.css'

const Signup = () => { 

    const [formData, setFormData] = useState({
        name: '',
        personalId: '',
        password: '',
        confirmPassword: '',
        email: '',
        part: '',
        nickname: '',
        agree : false, // 이용약관 동의 여부
      });

      const [showPassword, setShowPassword] = useState(false);
      const [eyeIcon, setEyeIcon] = useState('img/eye_close.png');
      const [passwordMismatch, setPasswordMismatch] = useState(false);
    
      const handleChange = (e) => {
        const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
        setFormData({ ...formData, [e.target.name]: value });
      };
    
      const handleSubmit = (e) => {
        e.preventDefault();
        // 폼 데이터를 처리하는 로직을 추가하세요.
        if (formData.password !== formData.confirmPassword) {
            setPasswordMismatch(true);
            return;
        }
        setPasswordMismatch(false);
        console.log(formData);
      };

      const isButtonDisabled = !formData.agree; // agree 값이 false일 때 버튼 비활성화

    
      return (
        <div className='scroll-container'>
        <form onSubmit={handleSubmit} className='centered-form'>
        <h2>Sign up</h2>
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
            <input
            className='input'
              type="text"
              id="personalId"
              name="personalId"
              value={formData.personalId}
              onChange={handleChange}
            />
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
            <input
            className='input'
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
          </div>
          <div className='input-container'>
          <label htmlFor="part"> *부서</label>
            <input
            className='input'
              type="part"
              id="part"
              name="part"
              value={formData.part}
              onChange={handleChange}
            />
          </div>
          <div className='input-container'>
          <label htmlFor="nickname"> *닉네임</label>
            <input
                className='input'
              type="nickname"
              id="nickname"
              name="nickname"
              value={formData.nickname}
              onChange={handleChange}
            />
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
          <button type="submit"  disabled={isButtonDisabled}>회원가입</button>
        </form>
        </div>
      );
};

export default Signup;