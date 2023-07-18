import { useState, useEffect } from "react";
import axios from 'axios';
import AdminCategory from "../AdminCategory/AdminCategory";
import AdminHeader from "../Header/AdminHeader";

const AdminProfile = () => {

    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [profile, setProfile] = useState();
    const [showPassword, setShowPassword] = useState(false);
    const [eyeIcon, setEyeIcon] = useState('/img/eye_close.png');
    const [passwordMismatch, setPasswordMismatch] = useState(false);

    const [formData, setFormData] = useState({
        password: '',
        confirmPassword: '',
        newPassword: '',
        email: '',
        partType : '',
        nickname: '',
      });

      const [dupData, setDupData] = useState ({
        email : true,
        nickname : true
      })
      const OPTIONS = [
        { value: "MOBIS", name: "모비스연구소시스템" },
        { value: "PLATFORM", name: "차량SW플랫폼" },
        { value: "FACTORY", name: "스마트팩토리" },
        { value: "NAVIGATION", name: "내비게이션" },
    ];
    
    const onOptionaHandler = (e) => {
      setFormData({ ...formData, [e.target.name]: e.target.value });
      console.log(e.target.value)
  }

      const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        if (e.target.name === 'email' || e.target.name === 'nickname') {
            setDupData({...dupData, [e.target.name]: false });
        }
      };

    //이메일 , 닉네임, 비밀번호 변경 가능
    
    useEffect(() => { // 처음 프로필 가져오기
        const getProfile = async () => {
          try {
            const response = await axios.get(`/api/mypage/profile/${personalId}`);
            console.log(response.data.data)
            setProfile(response.data.data)
            setFormData({ ...formData, "partType": response.data.data.partType })
          } catch (error) {
            console.log(error);
          }
        };
        getProfile();
      }, []);

      if (!profile) {
        return null;
      }

      const handleCheckEmailDuplicate = () => {
        if (formData.email == '') 
        {
          return;
        }
        // 이메일 중복확인 로직
        axios
        .get(`/api/check/email/${formData.email}`)
        .then((response) => {
          if (response.data.code == 409) {
            console.log("이메일 중복");
          } else {
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
        return;
        }
        // 닉네임 중복확인 로직
        axios
        .get(`/api/check/nickname/${formData.nickname}`)
        .then((response) => {
        if (response.data.code == 409) {
            console.log("닉네임 중복");
        } else {
            console.log("닉네임 성공");
            setDupData({nickname : true})
            setDupData({...dupData, 'nickname' : true});
        }
        })
        .catch((error) => {
        console.error("닉네임 중복 확인 에러", error);
        });
    };


    const handleSubmit = () => {
      console.log(formData)
      if (formData.password.length == 0) {
        alert("프로필 변경은 비밀번호 필수 입력값입니다")
        return ;
      }
        if (formData.newPassword !== formData.confirmPassword) {
            alert("비밀번호가 틀렸습니다.")
            console.log("비밀번호 틀림")
            setPasswordMismatch(true);
            return;
        } else {
            setPasswordMismatch(false);
            console.log(formData);
        }
        axios.put('/api/mypage/profile/update', {
            
            personalId : personalId,
            password :formData.password,
            newPassword : formData.newPassword.length == 0 ? null : formData.newPassword,
            email : formData.email.length == 0 ? null : formData.email,
            partType : formData.partType == profile.part ? null : formData.partType, 
            nickname : formData.nickname.length == 0 ? null : formData.nickname,
        })
         .then(response => {
            console.log(response.data)
            if (response.data.code === -1) {
                console.log('비밀번호가 틀렸습니다')
            } else {
                console.log('프로필 수정 성공')
                window.location.reload();
            }
         })
         .catch (error => {
            console.log(error);
          });
    }
    const isButtonDisabled = () => {

        if (formData.password == '')
        {
            return true;
        }
    }
    return (
        <div>
            <AdminCategory />
            <AdminHeader />
            <div className="profile-bigalign">
            <p className="profile-title">Profile</p>
            <hr className="profile-line"/>
            <div className='input-container'>
                <label>이름</label>
                <input className='input' placeholder={profile.name} disabled></input>
                </div>
                <div className='input-container'>
                <label>아이디</label>
                <input className='input'placeholder={profile.personalId} disabled></input>
                </div>
                <div className='input-container'>
          <label htmlFor="password">*현재 비밀번호</label>
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
                    setEyeIcon(showPassword ? '/img/eye_open.png' :'/img/eye_close.png')
                    }} />
            </div>
          </div>
          <div className='input-container'>

          <label htmlFor="newPassword"> 새 비밀번호</label>
            <input
            className='input'
            type='password'
            id="newPassword"
            name="newPassword"
            value={formData.newPassword}
            onChange={handleChange}
            />
            </div>
            <div className='input-container'>
        <label htmlFor="confirmPassword"> 새 비밀번호 확인</label>
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
                
                <label>이메일</label>
                <input
                    className='input'
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder={profile.email}
                />
                </div>
                <div className='input-container'>
                <label>*부서</label>
            <select className='update-book-select-box' name="partType" onChange={onOptionaHandler} defaultValue={profile.partType}> 
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
                <label>닉네임</label>
                <input
                    className='input'
                    type="text"
                    id="nickname"
                    name="nickname"
                    value={formData.emanicknameil}
                    onChange={handleChange}
                    placeholder={profile.nickname}
                />
                </div>
            <button className = 'updateprofile-button' onClick={handleSubmit}>회원 정보 수정</button>
            </div>
        </div>
    )
    }

export default AdminProfile;