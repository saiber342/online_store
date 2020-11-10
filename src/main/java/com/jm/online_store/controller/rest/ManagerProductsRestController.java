package com.jm.online_store.controller.rest;

import com.jm.online_store.model.Categories;
import com.jm.online_store.model.Product;
import com.jm.online_store.service.interf.CategoriesService;
import com.jm.online_store.service.interf.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для работы с добавлением/изменением товаров
 */
@RestController
@AllArgsConstructor
@Slf4j
public class ManagerProductsRestController {

    private final ProductService productService;
    private final CategoriesService categoriesService;

    /**
     * Метод обрабатывает загрузку файла с товарами на сервер
     * Вызывает соответствующий сервисный метод в зависимости от типа файла(CSV или XML)
     * @param file файл с данными
     * @return
     */
    @PostMapping(value = "/rest/products/uploadProductsFile")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("uploads/import/" + file.getOriginalFilename())));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            log.error("Ошибка сохранения файла");
            e.printStackTrace();
        }
        log.debug("тип файла" + getFileExtension(file.getOriginalFilename()));
        if(getFileExtension(getFileExtension(file.getOriginalFilename())).equals(".xml")){
            productService.importFromXMLFile(file.getOriginalFilename());
        }else {
            productService.importFromCSVFile(file.getOriginalFilename());
        }
        return ResponseEntity.ok("success");
    }

    /**
     * Метод-сепаратор, возвращающий расширение файла
     * @param myFileName имя файла
     */
    private static String getFileExtension(String myFileName) {
        int index = myFileName.indexOf('.');
        return index == -1? null : myFileName.substring(index);
    }

    /**
     * Метод выводит список всех товаров
     * @return List<Product> возвращает список товаров
     */
    @GetMapping(value = "/rest/products/allProducts")
    public List<Product> findAll() {
        return productService.findAll();
    }

    /**
     * Метод возвращает список неудаленых товаров
     * @return List<Product> возвращает список товаров
     */
    @GetMapping(value = "/rest/products/getNotDeleteProducts")
    public List<Product> getNotDeleteProducts() {
        return productService.getNotDeleteProducts();
    }

    /**
     * Метод, ищет акции по id
     * @param productId идентификатор товара
     * @return Optional<Product> возвращает товар
     */
    @GetMapping(value = "/rest/products/{id}")
    public Optional<Product> findProductById(@PathVariable("id") Long productId) {
        return productService.findProductById(productId);
    }

    /**
     * Метод добавляет товар
     * @param product акиця для добавления //// так товар или АКИЦЯ?))
     * @return ResponseEntity<Product> Возвращает добавленную акцию с кодом ответа
     */
    @PostMapping(value = "/rest/products/addProduct/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProductM(@RequestBody Product product, @PathVariable(name = "id") Long catId) {
        productService.saveProduct(product);
        Categories thisCat = categoriesService.getCategoryById(catId).get();
        List<Product> thisCatProducts = thisCat.getProducts();
        thisCatProducts.add(product);
        thisCat.setProducts(thisCatProducts);
        categoriesService.saveCategory(thisCat);
        return ResponseEntity.ok(product);
    }

    /**
     * Редактирует товар
     * @param product товар для редактирования
     * @return ResponseEntity<Product> Возвращает отредактированный товар с кодом ответа
     */
    @PutMapping("/rest/products/editProduct")
    public ResponseEntity<Product> editProductM(@RequestBody Product product) {
        productService.editProduct(product);
        return ResponseEntity.ok(product);
    }

    /**
     * Редактирует товар и его категорию
     * @param product товар для редактирования
     */
    @PutMapping("/rest/products/editProduct/{idOld}/{idNew}")
    public ResponseEntity<Product> editProductAndCategory(@RequestBody Product product,
                                                          @PathVariable Long idOld,
                                                          @PathVariable Long idNew) {
        productService.editProduct(product);
        Categories oldCat = categoriesService.getCategoryById(idOld).get();
        List<Product> oldList = oldCat.getProducts();
        oldList.remove(product);
        oldCat.setProducts(oldList);
        categoriesService.saveCategory(oldCat);
        Categories newCat = categoriesService.getCategoryById(idNew).get();
        List<Product> newList = newCat.getProducts();
        newList.add(product);
        newCat.setProducts(newList);
        categoriesService.saveCategory(newCat);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод удаления товара по идентификатору
     * @param id идентификатор товара
     */
    @DeleteMapping(value = "/rest/products/{id}")
    public ResponseEntity<Long>  deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(id);
    }

    /**
     * Метод восстановления удаленного товара по идентификатору
     * @param id идентификатор товара
     */
    @PostMapping(value = "/rest/products/restoredeleted/{id}")
    public ResponseEntity<Long> restoreProductById(@PathVariable("id") Long id) {
        productService.restoreProduct(id);
        return ResponseEntity.ok(id);
    }
}
