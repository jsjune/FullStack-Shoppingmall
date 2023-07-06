package com.example.be.product.repository;

import com.example.be.product.dto.ImageDaoDto;
import com.example.be.product.dto.ProductDto;
import com.example.be.product.dto.response.ProductsResponse;
import com.example.be.product.entity.Product;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.be.product.entity.QImageDao.imageDao;
import static com.example.be.product.entity.QProduct.product;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ProductsResponse getTotalProducts(Pageable pageable, List<Integer> continents, List<Integer> price, String searchTerm) {

        List<ProductDto> products = jpaQueryFactory.selectDistinct(Projections.constructor(ProductDto.class,
                        product.id,
                        product.title,
                        product.continents,
                        product.price
                ))
                .from(product)
                .where(
                        continentIn(continents),
                        priceBetween(price),
                        nameContains(searchTerm)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.id.desc())
                .fetch();

        fillProductImages(products);


        Long count = jpaQueryFactory.select(
                        product.count()
                )
                .from(product)
                .fetchOne();
        PageImpl<ProductDto> productList = new PageImpl<>(products, pageable, count);
        return new ProductsResponse(count, productList);
    }

    private void fillProductImages(List<ProductDto> productList) {
        List<Long> productIds = productList.stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<ImageDaoDto>> imageMap  = jpaQueryFactory.select(product.id, Projections.constructor(ImageDaoDto.class,
                        imageDao.orgImageUrl,
                        imageDao.thumbImageUrl))
                .from(product)
                .leftJoin(product.imageUrls, imageDao)
                .where(product.id.in(productIds))
                .transform(GroupBy.groupBy(product.id)
                        .as(GroupBy.list(Projections.constructor(ImageDaoDto.class,
                                imageDao.orgImageUrl,
                                imageDao.thumbImageUrl))));

        productList.forEach(p ->
                p.setImageUrls(imageMap.getOrDefault(p.getId(), new ArrayList<>()))
        );
    }


    private BooleanExpression continentIn(List<Integer> continents) {
        return (continents == null || continents.isEmpty()) ? null : product.continents.in(continents);

    }

    private BooleanExpression priceBetween(List<Integer> price) {
        return (price == null || price.size() != 2) ? null : product.price.between(price.get(0), price.get(1));
    }

    private BooleanExpression nameContains(String searchTerm) {
        return isEmpty(searchTerm) ? null : product.title.contains(searchTerm);
    }

}
