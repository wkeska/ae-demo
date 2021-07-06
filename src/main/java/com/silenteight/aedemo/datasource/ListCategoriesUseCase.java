package com.silenteight.aedemo.datasource;

import com.silenteight.datasource.categories.api.v1.Category;
import com.silenteight.datasource.categories.api.v1.CategoryType;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ListCategoriesUseCase {

  public List<Category> findAllCategories() {
    return List.of(
        Category.newBuilder().setName("country")
            .setType(CategoryType.ANY_STRING)
            .setDisplayName("Country")
            .setMultiValue(true)
            .build(),
        Category.newBuilder().setName("customerType")
            .setDisplayName("Customer Type (Individual / Company)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("I", "C"))
            .build(),
        Category.newBuilder().setName("hitType")
            .setDisplayName("Hit Type (PEP / AM / SAN)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("SAN", "PEP", "AM"))
            .build());
  }
}
