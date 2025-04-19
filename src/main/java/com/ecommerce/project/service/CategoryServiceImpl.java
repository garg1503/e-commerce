package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        //List<Category>categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ApiException("No categories created till now");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category getCategoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(getCategoryFromDb != null)
            throw new ApiException("Category with the name" + getCategoryFromDb.getCategoryName() + " already exists");
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);

        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                //.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not Found")); - some other developer can send his own message. hence, setting the common
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId)); // custom exception
 
        categoryRepository.delete(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryID) {
        Category category1 = categoryRepository.findById(categoryID)
                //.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not Found"));
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryID)); // custom exception
        Category updatedCategory = modelMapper.map(categoryDTO, Category.class);
        updatedCategory.setCategoryId(categoryID);
        Category savedCategory = categoryRepository.save(updatedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    /*

    CODE BEFORE IMPLEMENTING THE DATABASE JPA Repository

    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        // categories.stream()  -- converts list to streams
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                //.orElse(null);
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

//        if(category == null){
//            return "category not found";
//        }

        categories.remove(category);
        return "Category with id: " + categoryId + " deleted successfully";
    }

    @Override
    public String updateCategory(Category category, Long categoryID) {
        Category existingCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryID))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not found"));

        if(existingCategory != null){
            existingCategory.setCategoryName(category.getCategoryName());
        }
        return "Category with id: " + categoryID + " updated successfully";
    }*/
}