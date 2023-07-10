import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Category.css';

const Category = () => {
  const navigate = useNavigate();

  const categories = [
    { id: 1, name: '개발', link: 'DEV' },
    { id: 2, name: '소설', link: 'NOVEL' },
    { id: 3, name: '인문', link: 'HUMANITY' },
    { id: 4, name: '경제', link: 'ECONOMY' },
    { id: 5, name: '과학', link: 'SCIENCE' },
  ];

  const handleCategoryClick = (link) => {
    window.location.replace(`/book/category/${link}`);
  };

  return (
    <div className="category-banner">
      <h2>카테고리</h2>
      <ul className="category-list">
        {categories.map(category => (
          <li key={category.id} onClick={() => handleCategoryClick(category.link)}>
            {category.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Category;
