package com.springmvc.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.demo.models.Category;
import com.springmvc.demo.repositories.CategoryRepository;

@Controller
@RequestMapping(path = "categories") // de truy cap vao controller thi phai di qua duong dan nay
// tuong duong vs duong dan //http:localhost:8081/categories
public class CategoryController {
	@Autowired //Inject "categoryRepository" - Dependency Injection
	private CategoryRepository categoryRepository;
												
	@RequestMapping(value = "", method = RequestMethod.GET)
//truy cap tu phia trinh duyet gưi len su dung phuong thuc get
//tra ve ten cua jsp file
	public String getAllCategories(ModelMap modelMap) {
		
//		modelMap.addAttribute("name", "hoang");
//		modelMap.addAttribute("age", 18);
		Iterable<Category> categories = categoryRepository.findAll();
		modelMap.addAttribute("categories", categories);
		System.out.println("haha");
		return "category";
		
	}
}
