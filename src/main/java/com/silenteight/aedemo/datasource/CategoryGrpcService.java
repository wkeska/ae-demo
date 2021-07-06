package com.silenteight.aedemo.datasource;

import com.silenteight.datasource.categories.api.v1.*;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
class CategoryGrpcService extends CategoryServiceImplBase {
  private final ListCategoriesUseCase categoriesUseCase;
  private final GetMatchCategoryValuesUseCase categoryValuesUseCase;

  CategoryGrpcService(ListCategoriesUseCase categoriesUseCase, GetMatchCategoryValuesUseCase categoryValuesUseCase) {
    this.categoriesUseCase = categoriesUseCase;
    this.categoryValuesUseCase = categoryValuesUseCase;
  }

  @Override
  public void listCategories(
      ListCategoriesRequest request,
      StreamObserver<ListCategoriesResponse> responseObserver) {
    var categories = categoriesUseCase.findAllCategories();
    responseObserver.onNext(
        ListCategoriesResponse.newBuilder().addAllCategories(categories).build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request,
      StreamObserver<BatchGetMatchCategoryValuesResponse> responseObserver) {
    var categoryValues = categoryValuesUseCase.getMatchCategoryValues(
        request.getMatchValuesList());
    responseObserver.onNext(
        BatchGetMatchCategoryValuesResponse
            .newBuilder()
            .addAllCategoryValues(categoryValues)
            .build());
    responseObserver.onCompleted();
  }
}
