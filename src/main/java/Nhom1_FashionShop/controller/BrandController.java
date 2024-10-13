package Nhom1_FashionShop.controller;

import Nhom1_FashionShop.model.Brand;
import Nhom1_FashionShop.model.Category;
import Nhom1_FashionShop.service.BrandService;
import Nhom1_FashionShop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class BrandController {
    @Autowired
    private final BrandService brandService;
    @GetMapping("/brands/add")
    public String showAddForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "/brands/add-brand";
    }
    @PostMapping("/brands/add")
    public String addBrand(@Valid Brand brand, BindingResult result) {
        if (result.hasErrors()) {
            return "/brands/add-brand";
        }
        brandService.addBrand(brand);
        return "redirect:/brands";
    }
    // Hiển thị danh sách danh mục
    @GetMapping("/brands")
    public String listBrands(Model model) {
        List<Brand> brands = brandService.getAllBrand();
        model.addAttribute("brands", brands);
        return "/brands/brands-list";
    }
    @GetMapping("/brands/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Brand brand = brandService.getBrandById(id).orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        model.addAttribute("brand", brand);
        return "/brands/update-brand";
    }

    @PostMapping("/brands/update/{id}")
    public String updateBrand(@PathVariable("id") Long id, @Valid Brand brand,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            brand.setId(id);
            return "/brands/update-brand";
        }
        brandService.updateBrand(brand);
        model.addAttribute("brands", brandService.getAllBrand());
        return "redirect:/brands";
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id") Long id, Model model) {
        Brand brand = brandService.getBrandById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand Id:" + id));
        brandService.deleteBrandById(id);
        model.addAttribute("brands", brandService.getAllBrand());
        return "redirect:/brands";
    }
}
