import { useState, useEffect } from "react";
import axios from "axios";
import MypageCategory from "../../Category/MypageCategory";
import Header from "../../Header/Header";
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import BookItem_User_ReserveInfo from "./BookItem_User_ReserveInfo";



const ReserveInfo = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [page, setPage] = useState(0);
    const [pagination, setPagination] = useState();
    const [reserveList, setReserveList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`/api/mypage/reserve?page=${page}&personalId=${personalId}`);
                setReserveList(response.data.data.reserveList);
                setPagination(response.data.data.pagination);
                console.log(response.data.data);
            } catch (error) {
                console.log(error);
            }
        };
        fetchData();
    }, [page, personalId]); // page와 personalId를 의존성 배열에 포함

    if (!pagination) {
        return null;
    }

    return (
        <>
        <MypageCategory />
        <Header />
        <div className="reviewInfoMargin">
        <ol className='numbered-list'>
          {reserveList && reserveList.map((reserve, index) => {
            return <BookItem_User_ReserveInfo  key={reserve.reserveId} book ={reserve} index={(page * 10 ) + index + 1} /> 
          })}
        </ol>
            <PaginationBox className = 'page'>
            <Pagination 
                activePage={page + 1}
                itemsCountPerPage={1}
                totalItemsCount={pagination.totalElements}
                pageRangeDisplayed={pagination.totalPages}
                onChange={(pageNumber) => setPage(pageNumber - 1)}>
            </Pagination>
            </PaginationBox>
        </div>
        </>
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
export default ReserveInfo;