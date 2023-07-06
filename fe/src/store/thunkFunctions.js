import { createAsyncThunk } from '@reduxjs/toolkit';
import axiosInstance from '../utils/axios';
import axios from 'axios';

export const registerUser = createAsyncThunk(
  'user/registerUser',
  async (body, thunkAPI) => {
    try {
      // const response = await axios.post(`http://shopping-mall.ap-northeast-2.elasticbeanstalk.com/users/signup`, body);
      const response = await axios.post(
        `http://localhost:5000/users/signup`,
        body
      );

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
      const response = await axios.post(
        `http://localhost:5000/users/login`,
        body
      );

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

export const addToCart = createAsyncThunk(
  'user/addToCart',
  async (body, thunkAPI) => {
    try {
      const response = await axiosInstance.post(`/users/cart`, body);

      return response.data;
    } catch (error) {
      console.log(error);
      return thunkAPI.rejectWithValue(error.response.data || error.message);
    }
  }
);

export const getCartItems = createAsyncThunk(
  'user/getCartItems',
  async ({ cartItemIds, userCart }, thunkAPI) => {

    console.log("cartItemIds "+cartItemIds);
    try {
      const response = await axiosInstance.get(
        `/products/cart/${cartItemIds}?type=array`
      );
      userCart.forEach((cartItem) => {
        response.data.data.products.forEach((productDetail, index) => {
          if (cartItem.productId === productDetail.id) {
            response.data.data.products[index].quantity = cartItem.quantity;
          }
        });
      });

      return response.data;
    } catch (error) {
      console.error(error)
      return thunkAPI.rejectWithValue(error.response.data || error.message);
    }
  }
);

export const removeCartItem = createAsyncThunk(
  "user/removeCartItem",
  async (productId, thunkAPI) => {
      try {
          const response = await axiosInstance.post(
              `/users/cart/${productId}`
          );

          // response.data.cart.forEach(cartItem => {
          //     response.data.productInfo.forEach((productDetail, index) => {
          //         if (cartItem.id === productDetail._id) {
          //             response.data.productInfo[index].quantity = cartItem.quantity;
          //         }
          //     })
          // })

          return response.data;
      } catch (error) {
          console.log(error);
          return thunkAPI.rejectWithValue(error.response.data || error.message);
      }
  }
)
