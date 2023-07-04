import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Outlet, Route, Routes } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';
import NotAuthRoutes from './components/NotAuthRoutes';
import ProtectedRoutes from './components/ProtectedRoutes';
import Footer from './layout/Footer';
import Navbar from './layout/NavBar';
import CartPage from './pages/CartPage';
import DetailProductPage from './pages/DetailProductPage';
import HistoryPage from './pages/HistoryPage';
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import ProtectedPage from './pages/ProtectedPage';
import RegisterPage from './pages/RegisterPage';
import UploadProductPage from './pages/UploadProductPage';
import { setInitialUserState } from './store/userSlice';

function Layout() {
  return (
    <div className="flex flex-col h-screen justify-between">
      <ToastContainer
        position="bottom-right"
        theme="light"
        pauseOnHover
        autoClose={1500}
      />
      <Navbar />
      <main className="mb-auto w-10/12 max-w-4xl mx-auto">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}

function App() {
  const dispatch = useDispatch();
  const isAuth = useSelector((state) => state.user?.isAuth);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      console.log(token);
      dispatch(setInitialUserState());
    }
  }, [dispatch]);

  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<LandingPage />} />

        {/* 로그인한 사람만 갈 수 있는 경로 */}
        <Route element={<ProtectedRoutes isAuth={isAuth} />}>
          <Route path="/protected" element={<ProtectedPage />} />
          <Route path="/product/upload" element={<UploadProductPage />} />
          <Route path="/product/:productId" element={<DetailProductPage />} />
          <Route path="/user/cart" element={<CartPage />} />
          <Route path="/history" element={<HistoryPage />} />
        </Route>

        {/* 로그인한 사람은 갈 수 없는 경로 */}
        <Route element={<NotAuthRoutes isAuth={isAuth} />}>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Route>
      </Route>
    </Routes>
  );
}

export default App;
