import { useState, useEffect } from "react";
import './BookItem.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import LikeSet from "../Heart/LikeSet";
import RentButton from "../Button/RentButton";
import ReserveButton from "../Button/ReserveButton";

const NoNumberBookItem = ({book}) => {

  const navigate = useNavigate();

    const [rentType, setRentType] = useState();

    const personalId = sessionStorage.getItem('personalId');

    const handleBookClick = () => {
      navigate(`/book/detail/${book.bookId}`);
    };

      useEffect(() => {
        if (book) {
          setRentType(book.rentType ? '대여가능' : '대여불가');
        }
      }, [book]);
      

  return (
    <>    
    <li key={book.bookId} className="align separator">
      <div>
        <div className="indexandimg">
      <img className='book-img' src={`/img/book/${book.isbn}.jpg`} alt={book.title} onClick={handleBookClick}/>
        <div>
        <p className="rent-title" onClick={handleBookClick}>{book.title}</p>
        <p className="author">{book.author}</p>
        </div>
        </div>
        </div>
        <div>
        <div className="align-right">
          
            <div className="rent">
            <p className={book.rentType ? "blue" : "red"}>{rentType}</p>
            {book.rentType ? (
                  <RentButton personalId={personalId} bookId={book.bookId} />
                ) : 
                  (
                    <ReserveButton personalId={personalId} bookId={book.bookId} />
                  )}
            </div>
            <LikeSet book={book} />
        </div>
      </div>
    </li>
    </>
  );
};

export default NoNumberBookItem;