import { useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";


const FindId = () => {

    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");

    const onEmailHandler  = (e) => {
        setEmail(e.target.value);
    }

    const onSubmitHandler = () => {
        axios.get(`/api/findId?email=${email}`)
        .then(response => {
           console.log(response);
            console.log('아이디 찾기 성공')
            setMessage(`아이디는 ${response.data.data} 입니다`);
            
        })
        .catch (error => {
            if (error.response.data.code === 404) {
                Swal.fire({
                    icon: "warning",
                    title: "아이디 없음",
                    text: '해당하는 유저가 없습니다',
                    confirmButtonText: "확인",
                })
                console.log('아이디 없음')
                setMessage('해당하는 유저가 없습니다')
            }
           console.log(error);
           console.log('아이디 찾기 실패')
         });
    }

    return (
        <div className='centered-form'>
            <img className='login-logo ' src='/img/logo.svg' alt='로고'></img>
            <h2 className='login-h2'>Forgot your Login?</h2>
            <input className='input-login'value={email} onChange={onEmailHandler} placeholder='Email'/>
            <button type="submit" className='signin' onClick={onSubmitHandler}>
                  아이디 확인
            </button>
            <p className='login-find-under' onClick={() => window.location.assign('/')}> {'<'} 로그인 화면으로 돌아가기 </p>
            <p className="find-message">{message}</p>
        </div>
    )
   
}

export default FindId;