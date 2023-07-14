import React from 'react';

const AdminCategory = () => {

  const categories = [
    { id: 1, name: '대여 현황 관리', link: 'rentInfo' },
    { id: 2, name: '대여 기록 관리', link: 'rentHistory' },
    { id: 3, name: '도서 예약 관리', link: 'reserveInfo' },
    { id: 4, name: '도서 신청 관리', link: 'applyInfo' },
    { id: 5, name: '도서 관리', link: 'bookInfo' },
    { id: 6, name: '권한 관리', link: 'authInfo' },
    { id: 7, name: '개인 정보 변경', link: 'profile' },
    { id: 8, name: '회원 탈퇴', link: 'withdraw' },
  ];

  const handleCategoryClick = (link) => {
    window.location.assign(`/admin/${link}`);
  };

  return (
    <div className="mypagecategoryalign">
      <ul className="mycategory-list">
        {categories.map(category => (
          <li key={category.id} onClick={() => handleCategoryClick(category.link)}>
            {category.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AdminCategory;
