import { useNavigate } from 'react-router-dom';
import Swal from "sweetalert2";
import './LikeBookItem.css'
import axios from 'axios';

const LikeBookItem = ({personalId, book}) => {

  const navigate = useNavigate();

    const handleBookClick = () => {
        navigate(`/book/detail/${book.bookId}`);
      };

    const handelX = () => {
        axios.delete(`/api/love/delete?personalId=${personalId}&bookId=${book.bookId}`)
         .then(response => {
            console.log('좋아요 삭제 성공')
            Swal.fire({
              icon: "success",
              title: "삭제 성공",
              text :  `좋아요를 삭제했습니다`,
              confirmButtonText: "확인",
          }).then(() => {
            window.location.reload();
          });
         })
         .catch (error => {
            console.log(error);
            console.log('좋아요 삭제 실패')
            Swal.fire({
              icon: "error",
              title: "좋아요 삭제 실패",
              confirmButtonText: "확인",
          })
          });
    }

    return (
        <div className='like-book-box'>
            <div className='like-front'>
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
      <div>
      <p className="title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        <img className='x' src='/img/x.png' alt='x' onClick={handelX} />
        </div>
    )
}

export default LikeBookItem;