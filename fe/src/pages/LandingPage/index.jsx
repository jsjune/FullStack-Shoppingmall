import React from 'react';
import CheckBox from './Sections/CheckBox';
import RadioBox from './Sections/RadioBox';
import SearchInput from './Sections/SearchInput';
import CardItem from './Sections/CardItem';

const LandingPage = () => {
  return (
    <section>
      <div className='text-center m-7'>
        <h2 className='text-2xl'>여행 상품 사이트</h2>
      </div>

      {/* Filter */}
      <div className="flex gap-3">
        <div className="w-1/2">
          <CheckBox />
        </div>
        <div className="w-1/2">
          <RadioBox />
        </div>
      </div>
      {/* Search */}
      <div className='flex justify-end'>
        <SearchInput/>
      </div>

      {/* Cart */}
      <div className='grid grid-cols-2 gap-4 sm:grid-cols-4'>
        <CardItem/>
      </div>

      {/* LoadMore */}
      <div className='flex justify-center mt-5'>
        <button
          className='px-4 py-2 mt-5 text-white bg-black rounded-md hover:bg-gray-500'
        >
          더 보기
        </button>
      </div>
    </section>
  );
};

export default LandingPage;
