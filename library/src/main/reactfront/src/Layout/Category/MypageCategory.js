import React from 'react';
import './MypageCategory.css';

const MypageCategory = () => {

  const categories = [
    { id: 1, name: '대여 정보', link: 'rentInfo' },
    { id: 2, name: '대여 기록', link: 'rentHistory' },
    { id: 3, name: '예약 정보', link: 'reserveInfo' },
    { id: 4, name: '작성한 리뷰', link: 'reviewInfo' },
    { id: 5, name: '도서 신청 내역', link: 'applyInfo' },
    { id: 6, name: '관심 도서', link: 'likeBook' },
    { id: 7, name: '개인 정보 변경', link: 'profile' },
    { id: 8, name: '회원 탈퇴', link: 'withdraw' },
  ];

  const handleCategoryClick = (link) => {
    window.location.assign(`/mypage/${link}`);
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

export default MypageCategory;
