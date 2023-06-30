import { createSlice } from '@reduxjs/toolkit';
import { loginUser, logoutUser, registerUser } from './thunkFunctions';
import { toast } from 'react-toastify';

const initialState = {
  userData: {
    id: '',
    email: '',
    name: '',
    role: '',
    image: '',
  },
  isAuth: false,
  isLoading: false,
  error: '',
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setInitialUserState: (state) => {
      state.userData = initialState.userData;
      state.isAuth = false;
      state.isLoading = false;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(registerUser.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(registerUser.fulfilled, (state) => {
        state.isLoading = false;
        toast.info('회원가입을 성공했습니다.');
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
        toast.error(action.payload.error.message);
      })

      .addCase(loginUser.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.userData = action.payload.data;
        state.isAuth = true;
        localStorage.setItem('accessToken', action.payload.data.accessToken);
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
        toast.error(action.payload);
      })

      .addCase(logoutUser.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(logoutUser.fulfilled, (state, action) => {
        state.isLoading = false;
        state.userData = initialState.userData;
        state.isAuth = false;
        localStorage.removeItem('accessToken');
      })
      .addCase(logoutUser.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload;
        toast.error(action.payload);
      });
  },
});

export default userSlice.reducer;
export const { setInitialUserState } = userSlice.actions;
