import { Route, Routes } from 'react-router-dom';
import App from './App';
import Login from './LoginPage/Login';
import Main from './MainPage/Main';
function App2(){
    return (
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/main" element={<Main />} />
        </Routes>
      );
}

export default App2;