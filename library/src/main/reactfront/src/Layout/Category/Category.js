import React from 'react';
import { Link } from 'react-router-dom';
import './Category.css';

const Category = () => {
  const categories = [
    { id: 1, name: '개발', link: '/dev' },
    { id: 2, name: '소설', link: '/novel' },
    { id: 3, name: '인문', link: '/humanity' },
    { id: 4, name: '경제', link: '/economy' },
    { id: 5, name: '과학', link: '/science' },

    // ... 카테고리 데이터 추가
  ];

  return (
    <div className="category-banner">
      <h2>카테고리</h2>
      <ul className="category-list">
        {categories.map(category => (
          <li key={category.id}>
            <Link to={category.link}>{category.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Category;
