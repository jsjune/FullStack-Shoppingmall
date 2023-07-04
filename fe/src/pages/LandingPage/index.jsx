import React, { useEffect, useState } from 'react';
import CheckBox from './Sections/CheckBox';
import RadioBox from './Sections/RadioBox';
import SearchInput from './Sections/SearchInput';
import CardItem from './Sections/CardItem';
import axiosInstance from '../../utils/axios';
import { continents, prices } from '../../utils/filterData';
import qs from 'qs';

const LandingPage = () => {
  const size = 4;
  const [searchTerm, setSearchTerm] = useState('');
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);
  const [filters, setFilters] = useState({
    continents: [],
    price: [],
  });

  useEffect(() => {
    fetchProducts({ page: page, size: size });
  }, []);

  const fetchProducts = async ({
    page,
    size,
    loadMore = false,
    filters = {},
    searchTerm = '',
  }) => {
    const params = {
      page,
      size,
      searchTerm,
    };

    // filters를 쿼리 파라미터로 변환
    if (filters.continents && filters.continents.length > 0) {
      params.continents = filters.continents;
    }
    if (filters.price && filters.price.length > 0) {
      params.price = filters.price;
    }

    try {
      const response = await axiosInstance.get('/products', {
        params,
        paramsSerializer: (params) =>
          qs.stringify(params, { arrayFormat: 'repeat' }), 
      });
      if (loadMore) {
        setProducts([...products, ...response.data.data.productList]);
      } else {
        setProducts(response.data.data.productList);
      }
      setHasMore(response.data.data.productList.length >= size);
    } catch (error) {
      console.log(error);
    }
  };

  const handleLoadMore = () => {
    const nextPage = page + 1; // 다음 페이지 번호 계산
    fetchProducts({
      page: nextPage,
      size,
      loadMore: true,
      filters,
      searchTerm,
    });
    setPage(nextPage);
  };

  const handleFilters = (newFilteredData, category) => {
    const newFilters = { ...filters };
    newFilters[category] = newFilteredData;
    if (category === 'price') {
      const priceValues = handlePrice(newFilteredData);
      newFilters[category] = priceValues;
    }
    showFilteredResults(newFilters);
    setFilters(newFilters);
  };

  const handlePrice = (value) => {
    let array = [];

    for (let key in prices) {
      if (prices[key]._id === parseInt(value, 10)) {
        array = prices[key].array;
      }
    }
    return array;
  };

  const showFilteredResults = (filters) => {
    console.log(filters);
    const body = {
      page: 0,
      size,
      filters,
      searchTerm,
    };

    fetchProducts(body);
    setPage(0);
  };

  const handleSearchTerm = (event) => {
    const body = {
      page: 0,
      size,
      filters,
      searchTerm: event.target.value,
    };
    setPage(0);
    setSearchTerm(event.target.value);
    fetchProducts(body);
  };

  return (
    <section>
      <div className="text-center m-7">
        <h2 className="text-2xl">여행 상품 사이트</h2>
      </div>

      {/* Filter */}
      <div className="flex gap-3">
        <div className="w-1/2">
          <CheckBox
            continents={continents}
            checkedContinents={filters.continents}
            onFilters={(filters) => handleFilters(filters, 'continents')}
          />
        </div>
        <div className="w-1/2">
          <RadioBox
            prices={prices}
            checkedPrice={filters.price}
            onFilters={(filters) => handleFilters(filters, 'price')}
          />
        </div>
      </div>
      {/* Search */}
      <div className="flex justify-end">
        <SearchInput searchTerm={searchTerm} onSearch={handleSearchTerm} />
      </div>

      {/* Cart */}
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-4">
        {products.map((product) => (
          <CardItem product={product} key={product.id} />
        ))}
      </div>

      {/* LoadMore */}
      {hasMore && (
        <div className="flex justify-center mt-5">
          <button
            onClick={handleLoadMore}
            className="px-4 py-2 mt-5 text-white bg-black rounded-md hover:bg-gray-500"
          >
            더 보기
          </button>
        </div>
      )}
    </section>
  );
};

export default LandingPage;
