package com.jm.online_store.service.impl;

import com.jm.online_store.exception.FavouritesGroupNotFoundException;
import com.jm.online_store.exception.ProductNotFoundException;
import com.jm.online_store.exception.UserNotFoundException;
import com.jm.online_store.model.FavouritesGroup;
import com.jm.online_store.model.Product;
import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.FavouritesGroupProductService;
import com.jm.online_store.service.interf.FavouritesGroupService;
import com.jm.online_store.service.interf.ProductService;
import com.jm.online_store.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavouritesGroupProductServiceImpl implements FavouritesGroupProductService {
    private final UserService userService;
    private final FavouritesGroupService favouritesGroupService;
    private final ProductService productService;

    @Override
    public void deleteProductFromFavouritesGroup(Long idProduct, Long idFavouritesGroup, User currentUser) {

    }

    @Override
    public void addProductToFavouritesGroup(Long idProduct, Long idFavouritesGroup, User currentUser) {
        User user = userService.findById(currentUser.getId()).orElseThrow(UserNotFoundException::new);
        Iterator iterator = user.getFavouritesGroups().iterator();
        FavouritesGroup favouritesGroup = null;
        while (iterator.hasNext()){
            favouritesGroup = (FavouritesGroup)iterator.next();
            if(favouritesGroup.getId() == idFavouritesGroup){
                break;
            };
        }
        Set<Product> productSet = favouritesGroup.getProducts();
        productSet.add(productService.findProductById(idProduct).orElseThrow(ProductNotFoundException::new));
        favouritesGroup.setProducts(productSet);

        //FavouritesGroup favouritesGroup = (FavouritesGroup) user.getFavouritesGroups().;
        //Set<FavouritesGroup> favouritesGroupSet = user.getFavouritesGroups();

        //System.out.println(favouritesGroupSet);

//        Set<Product> favouritesGroupProducts = favouritesGroup.getProducts();
//        favouritesGroupProducts.add(productService.findProductById(idProduct).orElseThrow())
//        favouritesGroup.setProducts(favouritesGroupProducts);
//
//        user.setFavouritesGroups(favouritesGroup);
//        userService.updateUser(user);
    }

    @Override
    public Set<Product> getProductFromFavouritesGroup(Long idFavouritesGroup, User currentUser) {
        return null;
    }
}