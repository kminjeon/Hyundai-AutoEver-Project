import { useState, useEffect } from "react";
import axios from "axios";
import { useLocation } from "react-router-dom";


const PasswordReset = () => {

    const location = useLocation(); 
    console.log(location)
    const email = new URLSearchParams(location.search).get('email');
    const [password, setPassword] = useState("");
    const [personalId, setPersonalId] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    
    const onPasswordHandler = (e) => {
        setPassword(e.target.value);
    }
    const onConfirmPasswordHandler = (e) => {
        setConfirmPassword(e.target.value);
    }

    useEffect(() => {
        const getUser = async () => {
          try {
            const response = await axios.get('/api/findId', {
              params: {
                email : email
              }
            });
            console.log(response.data.data);
            setPersonalId(response.data.data);
          } catch (error) {
            console.log(error);
          }
        };
        getUser();
    }, []);


    const onSubmitHandler = () => {
        axios.put('/api/findId', null, {
            params : {
                personalId : personalId,
                newPassword : password
            }
        })
        .then(response => {
           console.log(response);
           console.log('비밀번호 재설정 성공')
           window.location.assign("/");
        })
        .catch (error => {
           console.log(error);
           console.log('비밀번호 재설정 실패')
         });
    }


    return (
        <div className='passwordReset-centered-form'>
        <div className='input-container'>

          <label htmlFor="password"> *새 비밀번호</label>
            <input className='input-login' type='password' id ='password' value={password} onChange={onPasswordHandler}/>
         </div>
        <div className='input-container'>
          <label htmlFor="confirmPassword"> *새 비밀번호 확인</label>
              <input className='input-login' type='password' id = 'confirmPassword' value={confirmPassword} onChange={onConfirmPasswordHandler}/>
              </div>
              <button type="submit" className='signin' onClick={onSubmitHandler}>
                  비밀번호 재설정
            </button>
        </div>
    )
}

export default PasswordReset;