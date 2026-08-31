package com.recpro.pe.learnsync.units.form;

import com.recpro.pe.learnsync.modules.forum.dto.category.CategoryDTO;
import com.recpro.pe.learnsync.modules.forum.dto.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.forum.model.Category;
import com.recpro.pe.learnsync.modules.forum.repository.CategoryRepository;
import com.recpro.pe.learnsync.modules.forum.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import com.recpro.pe.learnsync.modules.forum.mapper.CategoryMapper;
import com.recpro.pe.learnsync.modules.forum.mapper.TopicMapper;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;

        @Spy private CategoryMapper categoryMapper = new CategoryMapper(new TopicMapper());
    @InjectMocks private CategoryService categoryService;

    private List<Category> categories;

    @BeforeEach
    void setUp() {
        categories = List.of(
                new Category(1, "Technology", new ArrayList<>()),
                new Category(2, "Science", new ArrayList<>()),
                new Category(3, "Art", new ArrayList<>()),
                new Category(4, "Literature", new ArrayList<>()),
                new Category(5, "Music", new ArrayList<>())
        );
    }

    @Test
    void testFindAll() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Category> page = new PageImpl<>(categories.subList(0, 3), pageable, categories.size());

        // When
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        List<CategoryDTO> result = categoryService.listCategory(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdCategory()).isEqualTo(1);
        assertThat(result.get(1).getName()).isEqualTo("Science");
    }

    @Test
    void testCreateCategory() {
        // Given
        CreateCategoryDTO createCategory = new CreateCategoryDTO("Ciclo I");

        // When
        CategoryDTO result = categoryService.createCategory(createCategory);

        // Then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        assertThat(categoryArgumentCaptor.getValue().getName()).isEqualTo("Ciclo I");

        assertThat(result).isNotNull();
    }

    @Test
    void testCreateCategoryWhenCategoryNameExists() {
        // Given
        CreateCategoryDTO categoryDTO = new CreateCategoryDTO("Ciclo I");

        // When
        when(categoryRepository.existsCategoryByName("Ciclo I")).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.createCategory(categoryDTO));

        // Then
        assertThat(ex.getMessage()).isEqualTo("La categoría Ciclo I existe");
    }

    @Test
    void testGetCategory() {
        // Given
        String name = "Technology";
        Category category = categories.getFirst();

        // When
        when(categoryRepository.findByName(name)).thenReturn(Optional.of(category));
        Category result = categoryService.getCategory(name);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdCategory()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Technology");
    }

    @Test
    void testGetCategoryWhenCategoryNotExists() {
        // Given
        String name = "CATEGORY_NOT_EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> categoryService.getCategory(name));

        // Then
        assertThat(ex.getMessage()).isEqualTo("La categoria CATEGORY_NOT_EXISTS no existe");
    }
}
