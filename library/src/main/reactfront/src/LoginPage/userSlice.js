export const userSlice = createSlice({
    name: "user",
    initialState: {
      name: "",
      id: "",
      isLoading: false, // optional
      isLogin: null,
    },
    reducers: {
      loginUser: (state, action) => {
        state.name = action.payload.name;
        state.id = action.payload.id;
        state.isLogin = true;
      },
  
      clearUser: (state) => {
        state.name = "";
        state.id = "";
        state.isLogin = false;
      },
    },
  });