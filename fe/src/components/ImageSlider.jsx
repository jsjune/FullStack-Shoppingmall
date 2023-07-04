import React from 'react';
import "react-responsive-carousel/lib/styles/carousel.min.css"; 
import { Carousel } from 'react-responsive-carousel';

const ImageSlider = ({ images }) => {
  const imageUrls = images.map((image) => image.orgImageUrl);
  return (
    <Carousel autoPlay showThumbs={false} infiniteLoop>
      {imageUrls.map((imageUrl) => (
        <div key={imageUrl}>
          <img
            src={`${imageUrl}`}
            alt={imageUrl}
            className="w-full max-h-[150px]"
          />
        </div>
      ))}
    </Carousel>
  );
};

export default ImageSlider;
