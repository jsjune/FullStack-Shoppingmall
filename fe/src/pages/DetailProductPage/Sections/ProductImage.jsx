import React, { useEffect, useState } from 'react';
import ImageGallery from 'react-image-gallery';

const ProductImage = ({ product }) => {
  const [images, setImages] = useState([]);

  useEffect(() => {
    if (product?.imageUrls?.length > 0) {
      let images = [];

      product.imageUrls.map((imageName) => {
        return images.push({
          original: `${imageName.orgImageUrl}`,
          thumbnail: `${imageName.thumbImageUrl}`,
        });
      });

      setImages(images);
    }
  }, [product]);

  return <ImageGallery items={images} />;
};

export default ProductImage;
