import { useState, useEffect } from "react";
import axios from "axios";
import MypageCategory from "../../Category/MypageCategory";
import Header from "../../Header/Header";
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import PaginationBox from "../../Page/PaginationBox";
import BookItem_User_ApplyInfo from "./BookItem_User_ApplyInfo";



const ApplyInfo = () => {
    const personalId = sessionStorage.getItem('personalId'); // 로그인한 사용자
    const [page, setPage] = useState(0);
    const [pagination, setPagination] = useState();
    const [applyList, setApplyList] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`/api/mypage/apply/list?page=${page}&personalId=${personalId}`);
                setApplyList(response.data.data.applyList);
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
    const handleApplyBook = () => {
      window.location.assign("/mypage/applyInfo/apply");
    }
    return (
        <>
        <MypageCategory />
        <Header />
        <div className="reviewInfoMargin">
        <button className="book-add-button" onClick={handleApplyBook}>
        도서 신청
        </button>

        <ol className='numbered-list'>
          {applyList && applyList.map((apply, index) => {
            return <BookItem_User_ApplyInfo key={apply.applyId} book ={apply} index={(page * 10 ) + index + 1} /> 
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

export default ApplyInfo;