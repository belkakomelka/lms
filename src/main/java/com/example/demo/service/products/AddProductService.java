//package com.example.demo.service.products;
//
//import com.example.demo.database.entity.Course;
//import com.example.demo.database.repository.ProductRepository;
//import com.example.demo.dto.product.ProductAddRq;
//import com.example.demo.dto.product.ProductGetRs;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class AddProductService {
//
//    private final ProductRepository productRepository;
//
//    private final ObjectMapper objectMapping;
//
//    @Transactional
//    public ResponseEntity<String> addProduct(ProductAddRq productAddRq, MultipartFile image){
//        try {
//            log.info("Принят запрос для сохранения нового продукта " + objectMapping.writeValueAsString(productAddRq));
//            Optional<Course> productOptional = productRepository.findProductByName(productAddRq.getName());
//            Course product;
//            if (productOptional.isPresent()){
//                log.warn("Нельзя сохранить существующий продукт " + objectMapping.writeValueAsString(productAddRq));
//                product = productOptional.get();
//                log.info("Данный продукт уже есть в интернет-магазине " + product.getId());
//            } else {
//                String link = "some_link"; // todo куда-то положить картинку и получить link
//                product = buildProduct(productAddRq, link);
//                productRepository.save(product);
//            }
//            return new ResponseEntity<>(objectMapping.writeValueAsString(buildRs(product)), HttpStatus.OK);
//        } catch (RuntimeException | JsonProcessingException e) {
//            log.error("Внутрення ошибка сервиса " + e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    private ProductGetRs buildRs(Course product){
//        return ProductGetRs.builder()
//                .id(product.getId())
//                .build();
//    }
//
//    private Course buildProduct(ProductAddRq productAddRq, String link){
//        return Course.builder()
//                .name(productAddRq.getName())
//                .description(productAddRq.getDescription())
//                .shortDescription(productAddRq.getShortDescription())
//                .price(productAddRq.getPrice())
//                .linkToPhoto(link)
//                .build();
//    }
//}
