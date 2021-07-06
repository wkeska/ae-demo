package com.silenteight.aedemo.datasource;

import com.silenteight.datasource.categories.api.v1.CategoryValue;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class GetMatchCategoryValuesUseCase {

  public List<CategoryValue> getMatchCategoryValues(List<String> matchValues) {
    return matchValues.stream()
        .map(GetMatchCategoryValuesUseCase::getCategoryValue)
        .collect(Collectors.toList());
  }

  private static CategoryValue getCategoryValue(String matchValue) {
    var builder = CategoryValue.newBuilder()
        .setName(matchValue);

    if (matchValue.contains("categories/source_system")) {
      builder.setSingleValue("ECDD");
    } else if (matchValue.contains("categories/country")) {
      builder.setSingleValue("PL");
    } else if (matchValue.contains("categories/customer_type")) {
      builder.setSingleValue("I");
    } else if (matchValue.contains("categories/hit_type")) {
      builder.setSingleValue("DENY");
    } else if (matchValue.contains("categories/segment")) {
      builder.setSingleValue("CONSUMER");
    } else if (matchValue.contains("categories/hit_category")) {
      builder.setSingleValue("DENY");
    } else {
      builder.setSingleValue("UNKNOWN");
    }

    return builder.build();
  }
}
