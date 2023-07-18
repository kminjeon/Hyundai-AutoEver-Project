import React, { useState } from "react";
import Modal from "../Modal/Modal";
import Swal from "sweetalert2";
import axios from "axios";

const FindPassword = () => {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const onPasswordHandler = (e) => {
        setPassword(e.target.value);
    }
    const onConfirmPasswordHandler = (e) => {
        setConfirmPassword(e.target.value);
    }

    const [personalId, setPersonalId] = useState("");

    const [modalOpen, setModalOpen] = useState(false);

    const [code, setCode] = useState();
    const [inputCode, setInputCode] = useState('');

    const [resetModalOpen, setResetModalOpen] = useState(false);

    const onPersonalIdHandler  = (e) => {
        setPersonalId(e.target.value);
    }

    const onInputCodeHandler  = (e) => {
        setInputCode(e.target.value);
    }

    const closeModal = () => {
        setModalOpen(false);
      };

      const closeResetModal = () => {
        setResetModalOpen(false);
      };

    const openModal = () => {
        axios.get(`/api/check/personalId/${personalId}`)
          .then((response) => {
            console.log(response)
            if (response.data.code !== 409) {
                Swal.fire({
                    icon: "warning",
                    title: "사용자 없음",
                    html : `[${personalId}] <br/> 해당하는 사용자가 없습니다`,
                    confirmButtonText: "확인",
                })
                return;
            } else {
                axios.post('api/email', null, {
                    params: {
                        personalId : personalId,
                    }})
                    .then((response) => {
                    console.log('이메일 전송 성공');
                    setCode(response.data.data);
                    console.log(response.data.data);
                    setModalOpen(true);
                    })
                    .catch(error => {
                    console.error('이메일 전송 실패', error);
                    });
            }
            console.log('아이디 확인');
          })
          .catch(error => {
            console.error('아이디 확인 실패', error);
          });
        
    };


    const codeConfirm = () => {
        if (code === inputCode)
        {
            setModalOpen(false)
            setResetModalOpen(true);   
        } else {
            Swal.fire({
                icon: "warning",
                title: "확인 코드 오류",
                text : `확인 코드가 다릅니다`,
                confirmButtonText: "확인",
            })
        }
    }


    const onSubmitHandler = () => {
        if (password !== confirmPassword) {
            console.log("비밀번호 틀림")
            Swal.fire({
                icon: "warning",
                title: "비밀번호 오류",
                text : `비밀번호가  다릅니다`,
                confirmButtonText: "확인",
            })
            return;
        }
        axios.put('/api/passwordReset', {
                personalId : personalId,
                newPassword : password
            })
        .then(response => {
           console.log(response);
           Swal.fire({
            icon: "success",
            title: "비밀번호 재설정 성공",
            confirmButtonText: "확인",
        })
           console.log('비밀번호 재설정 성공')
           window.location.assign("/");
        })
        .catch (error => {
           console.log(error);
           Swal.fire({
            icon: "error",
            title: "비밀번호 재설정 실패",
            confirmButtonText: "확인",
        })
           console.log('비밀번호 재설정 실패')
         });
    }

    return (
        <div className='centered-form'>
            <img className='login-logo ' src='/img/logo.svg' alt='로고'></img>
            <h2 className='login-h2'>Forgot your password?</h2>
            <input className='input-login'value={personalId} onChange={onPersonalIdHandler} placeholder='Login'/>
            <button type="submit" className='signin' onClick={openModal}>
                  비밀번호 재설정
            </button>
            <p className='login-find-under' onClick={() => window.location.assign('/')}> {'<'} 로그인 화면으로 돌아가기 </p>
            
            
            <React.Fragment>
                <Modal open={modalOpen} close={closeModal} header="이메일 인증코드">
                메일로 인증코드를 발송했습니다. 인증코드를 입력해주세요.
                <div className="little-move"></div>
                <input className='input-login' id ='inputCode' value={inputCode} onChange={onInputCodeHandler} placeholder='인증코드'/>
                <button className='code-button' onClick={codeConfirm}>확인</button>
                </Modal>
            </React.Fragment>


            <React.Fragment>
                <Modal open={resetModalOpen} close={closeResetModal} header="비밀번호 재설정">
                <label htmlFor="password"> *새 비밀번호</label>
                    <input className='input-login' type='password' id ='password' value={password} onChange={onPasswordHandler}/>
                <div className="little-move"></div>
                <label htmlFor="confirmPassword" > *새 비밀번호 확인</label>
                    <input className='input-login' type='password' id = 'confirmPassword' value={confirmPassword} onChange={onConfirmPasswordHandler}/>
                    <button type="submit" className='reset-pass' onClick={onSubmitHandler}>
                        비밀번호 재설정
                    </button>
                </Modal>
            </React.Fragment>
        </div>
    )
}

export default FindPassword;