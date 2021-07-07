package com.silenteight.aedemo.datasource;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
class CommentInputGrpcService extends CommentInputServiceImplBase {

  private final CommentsInputUseCase commentsInputUseCase;

  CommentInputGrpcService(CommentsInputUseCase commentsInputUseCase) {
    this.commentsInputUseCase = commentsInputUseCase;
  }

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    var commentInputs = commentsInputUseCase
        .getCommentInputs(request.getAlertsList());
    commentInputs.stream().forEach(responseObserver::onNext);

    responseObserver.onCompleted();
  }
}
