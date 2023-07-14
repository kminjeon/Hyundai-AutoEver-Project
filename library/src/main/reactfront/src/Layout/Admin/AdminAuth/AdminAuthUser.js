import { useState, useEffect } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Switch from "./Switch";

const AdminAuthUser = ({user, index}) => {

  return (
    <>    
    <li key={user.id} className="align separator">
      <div>
            <p className="admin-rentinfo-index">{index}</p>  
            <p>{user.personalId}</p>
            <p>{user.name}</p>
            <div className="app">
            <Switch
                user={user}
                onColor="#06D6A0"
            />
            </div>
      </div>
    </li>
    </>
  );
};

export default AdminAuthUser;