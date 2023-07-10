import { useState, useEffect } from "react";
import Pagination from 'react-js-pagination'
import { useParams } from 'react-router';
import axios from 'axios';
import styled from 'styled-components'
import Header from "../header/Header";
import Category from "../Category/Category";
import BookItem from "../Book/BookItem";
import './CategoryPage.css'

const CategoryPage = () => {
    const { categoryType } = useParams();
    const [bookList, setBookList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();

    useEffect(() => {
        const getBookPage = async () => {
          try {
            const response = await axios.get(`/api/book/category/${categoryType}`, {
              params: {
                page: page
              }
            });
            setBookList(response.data.data.simpleBookDtoList);
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
            {bookList.map((book) => {
              return <BookItem key={book.bookId} book ={book} /> 
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

const PaginationBox = styled.div`
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 15px;
  }

  ul {
    list-style: none;
    padding: 0;
  }

  ul.pagination li {
    display: inline-block;
    width: 30px;
    height: 30px;
    border: 1px solid #e2e2e2;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1rem;
    border-radius: 50%; /* 동그라미 모양으로 변경 */
  }

  ul.pagination li:first-child {
    border-radius: 50%; /* 첫 번째 버튼에만 좌측 동그라미 모양 적용 */
  }

  ul.pagination li:last-child {
    border-radius: 50%; /* 마지막 버튼에만 우측 동그라미 모양 적용 */
  }

  ul.pagination li a {
    text-decoration: none;
    color: #337ab7;
    font-size: 1rem;
  }

  ul.pagination li.active a {
    color: white;
  }

  ul.pagination li.active {
    background-color: #337ab7;
  }

  ul.pagination li a:hover,
  ul.pagination li a.active {
    color: blue;
  }
`;


export default CategoryPage;

