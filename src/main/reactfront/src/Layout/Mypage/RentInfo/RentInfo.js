import { useState, useEffect } from "react";
import axios from "axios";
import MypageCategory from "../../Category/MypageCategory";
import Header from "../../Header/Header";
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import BookItem_User_RentInfo from "./BookItem_User_RentInfo";
import PaginationBox from "../../Page/PaginationBox";


const RentInfo = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [page, setPage] = useState(0);
    const [pagination, setPagination] = useState();
    const [rentList, setRentList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`/api/mypage/rent/getPage?page=${page}&personalId=${personalId}`);
                setRentList(response.data.data.rentList);
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
          {rentList && rentList.map((rent, index) => {
            return <BookItem_User_RentInfo key={rent.rentId} book ={rent} index={(page * 10 ) + index + 1} /> 
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
export default RentInfo;