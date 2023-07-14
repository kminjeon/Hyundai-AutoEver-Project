import React, { useEffect, useState } from 'react';
import axios from 'axios';

import BookItem_RentInfo from '../ADMIN_BookItem/BookItem_RentInfo';
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
    const [searchOnOff, setSearchOnOff] = useState('');

    const OPTIONS = [
        { value: "title", name: "도서 제목" },
        { value: "bookId", name: "도서 ID" },
        { value: "name", name: "회원 이름" },
        { value: "personalId", name: "회원 ID" },
    ];

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      const getRentPage = async () => {

        try {
            const response = await axios.get(`/api/admin/rent/getPage`, {
        params : {
            page : page,
            personalId : searchPersonalId.length === 0 ? null : searchPersonalId,
            name : searchName.length === 0 ? null : searchName,
            bookId : searchBookId.length === 0 ? null : searchBookId,
            title : searchTitle.length === 0 ? null : searchTitle
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
    }, [page, searchOnOff]);
    
    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }



    const handleSearch = () => {      
        switch (select) {
          case 'title':
                setSearchTitle(searchWord);
                setSearchName('');
                setSearchBookId('');
                setSearchPersonalId('');
              break;
            case 'bookId':
                setSearchTitle('');
                setSearchName('');
                setSearchBookId(searchWord);
                setSearchPersonalId('');
              break;
            case 'personalId':
              setSearchTitle('');
              setSearchName('');
              setSearchBookId('');
              setSearchPersonalId(searchWord);
              break;
            case 'name':
              setSearchTitle('');
              setSearchName(searchWord);
              setSearchBookId('');
              setSearchPersonalId('');
            break;
          default:
            break;
        }
        setPage(0);
        setSearchOnOff({select, searchWord});
      };

      const handleInputChange = (e) => {
        if (select == 'bookId') {
            const { value } = e.target
            // value의 값이 숫자가 아닐경우 빈문자열로 replace 해버림.
            const onlyNumber = value.replace(/[^0-9]/g, '')
            setSearchWord(onlyNumber)
        } else {
          setSearchWord(e.target.value);
        }
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
                <img className='searchimg' src='/img/search.png'  onClick={handleSearch}/>
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