import styled from 'styled-components'

const PaginationBox = styled.div`
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 15px;
  }

  ul {
    list-style: none;
    padding: 0;
  }

  ul.pagination li {
    display: inline-block;
    width: 30px;
    height: 30px;
    border: 1px solid #e2e2e2;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1rem;
    border-radius: 50%; /* 동그라미 모양으로 변경 */
  }

  ul.pagination li:first-child {
    border-radius: 50%; /* 첫 번째 버튼에만 좌측 동그라미 모양 적용 */
  }

  ul.pagination li:last-child {
    border-radius: 50%; /* 마지막 버튼에만 우측 동그라미 모양 적용 */
  }

  ul.pagination li a {
    text-decoration: none;
    color: #337ab7;
    font-size: 1rem;
  }

  ul.pagination li.active a {
    color: white;
  }

  ul.pagination li.active {
    background-color: #337ab7;
  }

  ul.pagination li a:hover,
  ul.pagination li a.active {
    color: blue;
  }
`;


export default PaginationBox;

