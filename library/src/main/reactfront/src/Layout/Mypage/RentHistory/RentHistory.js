import { useState, useEffect } from "react";
import axios from "axios";
import MypageCategory from "../../Category/MypageCategory";
import Header from "../../Header/Header";
import Pagination from 'react-js-pagination'
import styled from 'styled-components'



const RentHistory = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [page, setPage] = useState(0);
    const [pagination, setPagination] = useState();
    const [historyList, setHistroyList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`/api/mypage/rent/history?page=${page}&personalId=${personalId}`);
                setHistroyList(response.data.data.historyList);
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
            <div className="reviewInfo-title">
                <p>도서명</p>
                <p>작가</p>
                <p>출판사</p>
                <p>대여일</p>
                <p>반납일</p>
            </div>
            <hr/>
            {historyList ? (
            historyList.map((history) => (
                <div key={history.rentId} className="reviewInfo-item">
                    <p>{history.title}</p>
                    <p>{history.author}</p>
                    <p>{history.publisher}</p>
                    <p>{history.rentDate}</p>
                    <p>{history.returnDate}</p>
                </div>
            ))
        ) : (
            <p>No historys found.</p>
        )}
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
export default RentHistory;