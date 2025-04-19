package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName (String categoryName);
 }
