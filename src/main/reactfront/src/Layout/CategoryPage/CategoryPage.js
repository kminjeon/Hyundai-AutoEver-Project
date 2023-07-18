import { useState, useEffect } from "react";
import Pagination from 'react-js-pagination'
import { useParams } from 'react-router';
import axios from 'axios';
import Header from "../Header/Header";
import Category from "../Category/Category";
import BookItem from "../Book/BookItem";
import './CategoryPage.css'
import PaginationBox from "../Page/PaginationBox";
import NoNumberBookItem from "../Book/NoNumberBookItem";
import CategoryBookItem from "../Book/CategoryBookItem";

const CategoryPage = () => {
    const { categoryType } = useParams();
    const [bookList, setBookList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();
    const personalId = sessionStorage.getItem('personalId');


    useEffect(() => {
        const getBookPage = async () => {
          try {
            const response = await axios.get(`/api/book/category/${categoryType}`, {
              params: {
                page: page,
                personalId : personalId
              }
            });
            setBookList(response.data.data.bookList);
            setPageInfo(response.data.data.pagination);
            console.log(response.data.data);
          } catch (error) {
            console.log(error);
          }
        };
        getBookPage();
    }, [page]);

    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }

    return (
        <div className="headercategoryline">
          <Category />
          <Header />
          <div>
          <ol className='numbered-list'>
            {bookList&&bookList.map((book, index) => {
              return <CategoryBookItem key={book.bookId} book ={book} index={(page * 5) + index + 1}/> 
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

export default CategoryPage;

