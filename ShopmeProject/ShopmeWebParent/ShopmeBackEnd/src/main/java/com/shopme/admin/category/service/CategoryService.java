package com.shopme.admin.category.service;

import com.shopme.admin.category.exception.CategoryNotFoundException;
import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    // Cantidad de categorias a mostrar por pagina
    public static final int ROOT_CATEGORIES_PER_PAGE = 4;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber, String sortDir, String keyword){
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")){
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        // menos 1 porque pageNumber es 0 bits
        Pageable pageable = PageRequest.of(pageNumber -1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = null;
        if (keyword != null && !keyword.isEmpty()){
            pageCategories = categoryRepository.search(keyword, pageable);
        }else {
            pageCategories = categoryRepository.findRootCategories(pageable);
        }

        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()){
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult){
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;
        }else {
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
        List<Category> hierarchicalCategories = new ArrayList<>();
        for(Category rootCategory : rootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
            for (Category subCategory : children){
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent,
                                               int subLevel, String sortDir){
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel +1;

        for (Category subCategory : children){
            String name = "";
            for(int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }


    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUserInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB){
            if (category.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(category));
                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children){
                    String name =  "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());
        for (Category subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();;
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException{
        try{
            return categoryRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public String checkUnique(Integer id, String name, String alias){
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        if (isCreatingNew){
            if (categoryByName != null){
                return  "DuplicateName";
            }else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null){
                    return  "DuplicateAlias";
                }
            }
        }else{
            if (categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children){
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                if (sortDir.equals("asc")){
                    return o1.getName().compareTo(o2.getName());
                }else{
                    return o2.getName().compareTo(o1.getName());
                }
            }
        });
        sortedChildren.addAll(children);

        return sortedChildren;
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0){
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
        categoryRepository.deleteById(id);
    }
}
