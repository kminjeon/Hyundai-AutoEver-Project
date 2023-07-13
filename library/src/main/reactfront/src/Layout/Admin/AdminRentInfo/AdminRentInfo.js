import React, { useEffect, useState } from 'react';
import axios from 'axios';

import BookItem_RentInfo from '../BookItem_RentInfo/BookItem_RentInfo';
import AdminHeader from '../Header/AdminHeader';
import AdminCategory from '../AdminCategory/AdminCategory';
import PaginationBox from '../../Page/PaginationBox';
import Pagination from 'react-js-pagination';
import './AdminRentInfo.css'

const AdminRentInfo = () => {

    const personalId = sessionStorage.getItem('personalId');
    const [rentList, setRentList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();

    const [searchPersonalId, setSearchPersonalId] = useState('');
    const [searchName, setSearchName] = useState('');
    const [searchBookId, setSearchBookId] = useState('');
    const [searchTitle, setSearchTitle] = useState('');
    const [searchWord, setSearchWord] = useState('');

    const [select, setSelect] = useState("title")


    const OPTIONS = [
        { value: "title", name: "도서 제목" },
        { value: "bookId", name: "도서 ID" },
        { value: "name", name: "회원 이름" },
        { value: "personalId", name: "회원 ID" },
    ];


    const [searchList, setSearchList] = useState({
        personalId : '',
        name : '',
        bookId : '',
        title : '',
    });


    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      const getRentPage = async () => {
        try {
            const response = await axios.get(`/api/admin/rent/getPage`, {
        params : {
            page : page,
            personalId : searchList.personalId.length == 0 ? null : searchList.personalId,
            name : searchList.name.length == 0 ? null : searchList.name,
            bookId : searchList.bookId.length == 0 ? null : searchList.bookId,
            title : searchList.title.length == 0 ? null : searchList.title
        }
      });
          console.log(response.data.data); 
          setRentList(response.data.data.rentList);
          setPageInfo(response.data.data.pagination);
        } catch(error) {
          console.log(error);
        }
    };
        getRentPage();
    }, [page, searchList]);
    
    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }



    const handleSearch = () => {
        let updatedState = {};
      
        switch (select) {
          case 'title':
            updatedState = {
                searchTitle: searchWord,
                searchName: '',
                searchBookId: '',
                searchPersonalId: '',
              };
              break;
            case 'bookId':
              updatedState = {
                searchTitle: '',
                searchName: '',
                searchBookId: searchWord,
                searchPersonalId: '',
              };
              break;
            case 'personalId':
              updatedState = {
                searchTitle: '',
                searchName: '',
                searchBookId: '',
                searchPersonalId: searchWord,
              };
              break;
            case 'name':
              updatedState = {
                searchTitle: '',
                searchName: searchWord,
                searchBookId: '',
                searchPersonalId: '',
            };
            break;
          default:
            break;
        }
        setPage(0);
        setSearchList(updatedState);
      };

      const handleInputChange = (e) => {
        setSearchWord(e.target.value);
      };

      const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            console.log("검색 !")
          handleSearch();
        }
      };

      const onOptionaHandler = (e) => {
        setSelect(e.target.value);
        console.log(e.target.value)
    }

    return (
      <div>
        <AdminCategory />
        <AdminHeader />
        <div className='headercategoryline'>
        <div className='admin-search'>
                <input
                  className="admin-rent-info-search-input"
                  value={searchWord}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                />
                <img className='searchimg' src='/img/search.png' />
            <select className='select-box' onChange={onOptionaHandler}> 
			{OPTIONS.map((option) => (
				<option
                    key={option.value}
					value={option.value}
					defaultValue={option.value}
				>
					{option.name}
				</option>
			))}
		</select>
        </div>
        <ol className='numbered-list'>
          {rentList && rentList.map((rent, index) => {
            return <BookItem_RentInfo key={rent.rentId} book ={rent} index={(page * 10 ) + index + 1} /> 
          })}
        </ol>
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
      </div>
    );
};

export default AdminRentInfo;