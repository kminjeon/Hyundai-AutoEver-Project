import { useState, useEffect } from "react";
import Pagination from 'react-js-pagination'
import { useParams, useLocation, useNavigate } from 'react-router';
import axios from 'axios';
import './Search.css'
import PaginationBox from "../Page/PaginationBox";
import BookItem from "../Book/BookItem";
import Category from "../Category/Category";
import Header from "../Header/Header";
import NoNumberBookItem from "../Book/NoNumberBookItem";

const Search = () => {
    const [bookList, setBookList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();

    const location = useLocation(); 
    console.log(location)
    const searchWord = new URLSearchParams(location.search);
    const navigate = useNavigate();

    const personalId = sessionStorage.getItem('personalId');

    useEffect(() => {
        const getBookPage = async () => {
          try {
            const response = await axios.get(`/api/book/search${location.search}`, {
              params: {
                page: page,
                personalId : personalId
              }
            });
            setBookList(response.data.data.bookList);
            setPageInfo(response.data.data.pagination);
            console.log(response.data);
          } catch (error) {
            console.log(error);
          }
        };
        getBookPage();
    }, [page, location.search]);

    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }

    return (
        <div className="headercategoryline">
          <Category />
          <Header />
          <p>'{searchWord.get('searchWord')}'에 대한 {pageInfo.totalElements}개의 검색 결과</p>
          <div>
            <ol className='numbered-list'>
                {bookList&&bookList.map((book) => {
                return <NoNumberBookItem key={book.bookId} book ={book} /> 
                })}
            </ol>
          </div>

            <PaginationBox className = 'page'>
            <Pagination 
                activePage={page + 1}
                itemsCountPerPage={1}
                totalItemsCount={pageInfo.totalElements}
                pageRangeDisplayed={pageInfo.totalPages}
                onChange={(pageNumber) => setPage(pageNumber - 1)}>
            </Pagination>
            </PaginationBox>
        </div>
    )

}

export default Search;