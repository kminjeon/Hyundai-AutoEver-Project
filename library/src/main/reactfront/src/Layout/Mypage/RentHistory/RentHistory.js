import { useState, useEffect } from "react";
import axios from "axios";
import MypageCategory from "../../Category/MypageCategory";
import Header from "../../Header/Header";
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import PaginationBox from "../../Page/PaginationBox";
import BookItem_User_RentHistory from "./BookItme_User_RentHistory";



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
        <ol className='numbered-list'>
          {historyList && historyList.map((history, index) => {
            return <BookItem_User_RentHistory key={history.rentId} book ={history} index={(page * 10 ) + index + 1} /> 
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

export default RentHistory;