import React, { useEffect, useState } from 'react';
import axios from 'axios';

import AdminHeader from '../Header/AdminHeader';
import AdminCategory from '../AdminCategory/AdminCategory';
import PaginationBox from '../../Page/PaginationBox';
import Pagination from 'react-js-pagination';
import BookItem_ApplyInfo from '../ADMIN_BookItem/BookItem_ApplyInfo';

const AdminApplyInfo = () => {

    const personalId = sessionStorage.getItem('personalId');
    const [applyList, setApplyList] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState();

    const [searchPersonalId, setSearchPersonalId] = useState('');
    const [searchName, setSearchName] = useState('');
    const [searchIsbn, setSearchIsbn] = useState('');
    const [searchTitle, setSearchTitle] = useState('');
    const [searchAuthor, setSearchAuthor] = useState('');
    const [searchPublisher, setSearchPublisher] = useState('');
    
    const [searchWord, setSearchWord] = useState('');

    const [select, setSelect] = useState("title")
    const [searchOnOff, setSearchOnOff] = useState('');

    const OPTIONS = [
        { value: "title", name: "도서명" },
        { value: "publisher", name: "출판사" },
        { value: "author", name: "작가" },
        { value: "isbn", name: "isbn" },
        { value: "name", name: "회원 이름" },
        { value: "personalId", name: "회원 ID" },
    ];

    useEffect(() => {
      // 도서 데이터를 가져오는 API 호출
      const getApplyPage = async () => {

        try {
            const response = await axios.get(`/api/admin/apply/list`, {
        params : {
            page : page,
            personalId : searchPersonalId.length === 0 ? null : searchPersonalId,
            name : searchName.length === 0 ? null : searchName,
            title : searchTitle.length === 0 ? null : searchTitle,
            author : searchAuthor.length === 0 ? null : searchAuthor,
            publisher : searchPublisher.length === 0 ? null : searchPublisher,
            isbn : searchIsbn.length === 0 ? null : searchIsbn,
        }
      });
          console.log(response.data.data); 
          setApplyList(response.data.data.applyList);
          setPageInfo(response.data.data.pagination);
        } catch(error) {
          console.log(error);
        }
    };
      getApplyPage();
    }, [page, searchOnOff]);
    
    if (!pageInfo) {
        return null; // pageInfo가 없을 때 null을 반환하여 Pagination 컴포넌트를 렌더링하지 않음
    }



    const handleSearch = () => {      
        switch (select) {
          case 'title':
                setSearchPersonalId('');
                setSearchName('');
                setSearchTitle(searchWord);
                setSearchAuthor('');
                setSearchIsbn('');
                setSearchPublisher('')
              break;
            case 'author':
              setSearchPersonalId('');
              setSearchName('');
              setSearchTitle('');
              setSearchAuthor(searchWord);
              setSearchIsbn('');
              setSearchPublisher('')
              break;
            case 'personalId':
              setSearchPersonalId(searchWord);
              setSearchName('');
              setSearchTitle('');
              setSearchAuthor('');
              setSearchIsbn('');
              setSearchPublisher('')
              break;
            case 'name':
              setSearchPersonalId('');
              setSearchName(searchWord);
              setSearchTitle('');
              setSearchAuthor('');
              setSearchIsbn('');
              setSearchPublisher('')
            case 'publisher':
              setSearchPersonalId('');
              setSearchName('');
              setSearchTitle('');
              setSearchAuthor('');
              setSearchIsbn('');
              setSearchPublisher(searchWord)
            case 'isbn':
              setSearchPersonalId('');
              setSearchName('');
              setSearchTitle('');
              setSearchAuthor('');
              setSearchIsbn(searchWord);
              setSearchPublisher('')
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
          {applyList && applyList.map((reserve, index) => {
            return <BookItem_ApplyInfo key={reserve.reserveId} book ={reserve} index={(page * 10 ) + index + 1} /> 
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

export default AdminApplyInfo;