package com.springmvc.demo.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.demo.models.Category;
import com.springmvc.demo.models.Product;
import com.springmvc.demo.repositories.CategoryRepository;
import com.springmvc.demo.repositories.ProductRepository;

@Controller
@RequestMapping(path = "products")
//http:localhost:8081/products/getProductsByCategoryID/C0103
public class ProductController {
	@Autowired
	ProductRepository productRepository;

	@RequestMapping(value = "/getProductsByCategoryID/{categoryID}", method = RequestMethod.GET)
	public String getProductsByCategoryID(ModelMap modelMap, @PathVariable String categoryID) {
		Iterable<Product> products = productRepository.findByCategoryID(categoryID);
		modelMap.addAttribute("products", products);
		return "productList"; // productList.jsp
	}

	// products/changeCategory/${product.getProductID()
	@Autowired // Inject "categoryRepository" - Dependency Injection
	private CategoryRepository categoryRepository;

	@RequestMapping(value = "/changeCategory/{productID}", method = RequestMethod.GET)
	public String changeCategory(ModelMap modelMap, @PathVariable String productID) {
		Iterable<Category> categories = categoryRepository.findAll();
		modelMap.addAttribute("categories", categories);
		modelMap.addAttribute("product", productRepository.findById(productID).get());
		return "updateProduct";// updateProduct.jsp
	}
	
	@RequestMapping(value = "/insertProduct", method = RequestMethod.GET)
	public String insertProduct(ModelMap modelMap) {
		modelMap.addAttribute("product", new Product());
		modelMap.addAttribute("categories", categoryRepository.findAll());
		return "insertProduct";
		
	}
	
	@RequestMapping(value = "/insertProduct", method = RequestMethod.POST)
	public String insertProduct(ModelMap modelMap,
			@Valid @ModelAttribute("product") Product product,
			BindingResult BindingResult //validation
			) {
		if(BindingResult.hasErrors()) {
			return "insertProduct";
		}
		try {
			product.setProductID(UUID.randomUUID().toString());
			productRepository.save(product);
			return "redirect:/categories";
		}catch (Exception e) {
			modelMap.addAttribute("error", e.toString());
			return "insertProduct";
		}
		
	}
	@RequestMapping(value="/deleteProduct/{productID}", method = RequestMethod.POST)
	public String deleteProduct(ModelMap modelMap, @PathVariable String productID) {
		productRepository.deleteById(productID);
		return "redirect:/categories";
		
	}
	
	// products/getProductsByCategoryID/${category.getCategoryID()
	@RequestMapping(value = "/updateProduct/{productID}", method = RequestMethod.POST)
	public String updateProduct(ModelMap modelMap, 
			@Valid @ModelAttribute("product") Product product,
			BindingResult BindingResult, 
			@PathVariable String productID) {
			Iterable<Category> categories = categoryRepository.findAll();
			if (BindingResult.hasErrors()) {
				System.out.println("haha");
				modelMap.addAttribute("categories", categories);
				return "updateProduct";
			}
		
			try {
				if (productRepository.findById(productID).isPresent()) {
					Product foundProduct = productRepository.findById(product.getProductID()).get();
						if (!product.getProductName().trim().isEmpty()) {
						foundProduct.setProductName(product.getProductName());
						}
						if (!product.getCategoryID().isEmpty()) {
						foundProduct.setCategoryID(product.getCategoryID());
						}
						if (!product.getDescription().trim().isEmpty()) {
						foundProduct.setDescription(product.getDescription());
						}
						if (product.getPrice() > 0) {
						foundProduct.setPrice(product.getPrice());
						}
					productRepository.save(foundProduct);
				   }
				
			}catch (Exception e) {
				return "updateProduct";
			}
		return "redirect:/products/getProductsByCategoryID/" + product.getCategoryID();

	}
}
