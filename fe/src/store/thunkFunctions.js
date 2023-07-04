import { createAsyncThunk } from '@reduxjs/toolkit';
import axiosInstance from '../utils/axios';
import axios from 'axios';

export const registerUser = createAsyncThunk(
  'user/registerUser',
  async (body, thunkAPI) => {
    try {
      // const response = await axios.post(`http://shopping-mall.ap-northeast-2.elasticbeanstalk.com/users/signup`, body);
      const response = await axios.post(`http://localhost:5000/users/signup`, body);

      return response.data;
    } catch (error) {
      console.log(error);
      return thunkAPI.rejectWithValue(error.response.data || error.message);
    }
  }
);

export const loginUser = createAsyncThunk(
  'user/loginUser',
  async (body, thunkAPI) => {
    try {
      // const response = await axios.post(`http://shopping-mall.ap-northeast-2.elasticbeanstalk.com/users/login`, body);
      const response = await axios.post(`http://localhost:5000/users/login`, body);

      return response.data;
    } catch (error) {
      console.log(error);
      return thunkAPI.rejectWithValue(error.response.data || error.message);
    }
  }
);

export const logoutUser = createAsyncThunk(
  'user/logoutUser',
  async (_, thunkAPI) => {
    try {
      const response = await axiosInstance.post(`/users/logout`);

      return response.data;
    } catch (error) {
      console.log(error);
      return thunkAPI.rejectWithValue(error.response.data || error.message);
    }
  }
);
