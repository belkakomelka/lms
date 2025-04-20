//package com.example.demo.service.products;
//
//import com.example.demo.database.entity.Course;
//import com.example.demo.database.repository.ProductRepository;
//import com.example.demo.dto.product.ListOfProductsRs;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class GetAllProductService {
//
//    private final ProductRepository productRepository;
//
//    private final ObjectMapper objectMapping;
//
//    // todo допустим я пока отдаю линк, а Влад сделает запрос, чтобы получить все картинки(обсуждаемо)
//    @Transactional
//    public ResponseEntity<String> getAllProducts(){
//        try {
//            log.info("Принят запрос для предоставления всего списка продуктов");
//            List<Course> allProducts = productRepository.findAll();
//            if (allProducts.isEmpty()){
//                log.warn("В базе нет продуктов");
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(objectMapping.writeValueAsString(mapToProductRsList(allProducts)), HttpStatus.OK);
//        } catch (RuntimeException | JsonProcessingException e) {
//            log.error("Внутрення ошибка сервиса " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private ListOfProductsRs mapToProductRsList(List<Course> products) {
//        return ListOfProductsRs.builder()
//                .productRsList(products.stream()
//                        .map(this::mapToProductRs)
//                        .toList())
//                .build();
//    }
//
//    private ListOfProductsRs.ProductRs mapToProductRs(Course product) {
//        return ListOfProductsRs.ProductRs.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .shortDescription(product.getShortDescription())
//                .price(product.getPrice())
//                .link(product.getLinkToPhoto())
//                .build();
//    }
//}
