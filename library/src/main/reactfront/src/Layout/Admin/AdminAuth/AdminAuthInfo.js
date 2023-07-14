import React, { useEffect, useState } from 'react';
import axios from 'axios';

import AdminHeader from '../Header/AdminHeader';
import AdminCategory from '../AdminCategory/AdminCategory';
import PaginationBox from '../../Page/PaginationBox';
import Pagination from 'react-js-pagination';
import BookItem_RentHistory from '../ADMIN_BookItem/BookItem_RentHistory';
import BookItem_BookInfo from '../ADMIN_BookItem/BookItem_BookInfo';
import AdminAuthUser from './AdminAuthUser';

const AdminAuthInfo = () => {

    const personalId = sessionStorage.getItem('personalId');
    const [userList, setUserList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();

    const [searchPersonalId, setSearchPersonalId] = useState('');
    const [searchName, setSearchName] = useState('');
    
    const [searchWord, setSearchWord] = useState('');

    const [select, setSelect] = useState("personalId")
    const [searchOnOff, setSearchOnOff] = useState('');

    const OPTIONS = [
        { value: "personalId", name: "사용자 ID" },
        { value: "name", name: "이름" },
    ];

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      const getAuthPage = async () => {
        console.log(select, searchPersonalId, searchName)
        try {
            const response = await axios.get('/api/admin/auth/getPage', {
        params : {
            page : page,
            personalId : searchPersonalId.length === 0 ? null : searchPersonalId,
            name : searchName.length === 0 ? null : searchName,

        }
      });
          console.log(response.data.data); 
          setUserList(response.data.data.userList);
          setPageInfo(response.data.data.pagination);
        } catch(error) {
          console.log(error);
        }
    };
    getAuthPage();
    }, [page, searchOnOff]);
    
    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }



    const handleSearch = () => {      
        switch (select) {
          case 'personalId':
              setSearchPersonalId(searchWord);
              setSearchName('');
              break;
            case 'name':
              setSearchPersonalId('');
              setSearchName(searchWord);
              break;
          default:
            break;
        }
        setPage(0);
        setSearchOnOff({select, searchWord});
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
          {userList && userList.map((user, index) => {
            return <AdminAuthUser key={user.id} user ={user} index={(page * 10 ) + index + 1} /> 
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

export default AdminAuthInfo;