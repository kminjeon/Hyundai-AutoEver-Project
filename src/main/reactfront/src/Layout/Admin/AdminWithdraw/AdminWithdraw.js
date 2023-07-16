import CleanModal from "../../Modal/CleanModal";

const AdminWithdraw = () => {
    const [modalOpen, setModalOpen] = useState(false);

    const openModal = () => {
        setModalOpen(true);
    };

    const closeModal = () => {
        setModalOpen(false);
      };


    return (
        <div>
            <React.Fragment>
                <CleanModal open={modalOpen} close={closeModal}>
                회원 탈퇴하시겠습니까?
                <button className='code-button' onClick={codeConfirm}>확인</button>
                <button className='code-button' onClick={closeModal}>취소</button>
                </CleanModal>
            </React.Fragment>
        </div>
    )
}

export default AdminWithdraw;