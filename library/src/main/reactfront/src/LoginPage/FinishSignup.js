import './FinishSignup.css'

const FinishSignup = () => {

    const name = sessionStorage.getItem('name');

    return (
        <div className="center">
            <h2>회원가입 완료</h2>
            <br />
            <img className='logo' src='/img/logo.svg' alt='로고'></img>
            <br />
            <label className='ment'>{name}님의 회원가입을 환영합니다!</label>
            <br />
            <button>로그인 페이지로 이동하기</button>
        </div>
    )
}

export default FinishSignup;