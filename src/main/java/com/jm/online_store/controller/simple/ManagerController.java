package com.jm.online_store.controller.simple;

import com.jm.online_store.model.Categories;
import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.CategoriesService;
import com.jm.online_store.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final UserService userService;
    private final CategoriesService categoriesService;

    @GetMapping
    public String getManagerPage() {
        return "manager-page";
    }

    @GetMapping("/news")
    public String getNewsManagementPage() {
        return "news-management";
    }

    @GetMapping("/reports")
    public String getReportsPage() {
        return "reports";
    }

    @GetMapping("/settings")
    public String getSettingsPage (){
        return "manager-settings";
    }

    @GetMapping("/stocks")
    public String getStocks() {
        return "stocks-manager-page";
    }

    @GetMapping("/mailing")
    public String getMailing() {
        return "manager-mailing";
    }

    @GetMapping("/feedback")
    public String getFeedbacks() {
        return "manager-feedback";
    }

    @GetMapping("/shops")
    public String getShops() {
        return "manager-shops";
    }

    @GetMapping("/rating")
    public String getRating(Model model) {
        model.addAttribute("listCategories", categoriesService.getCategoriesWithoutParentCategory().stream().map(Categories::getCategory).collect(Collectors.toList()));

        return "products-rating";
    }

    @GetMapping("/characteristics")
    public String getCharacteristics(Model model) {
        model.addAttribute("listCategories", categoriesService.getCategoriesWithoutParentCategory().stream().map(Categories::getCategory).collect(Collectors.toList()));

        return "characteristics";
    }

    /*
     добавил маппинг для отображения профиля на странице менеджера
     */
    @GetMapping("/profile")
    public String getPersonalInfo(Model model) {
        User user = userService.getCurrentLoggedInUser();
        model.addAttribute("user", user);
        return "manager-profile";
    }
}
