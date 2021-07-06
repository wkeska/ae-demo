package com.silenteight.aedemo.datasource;

import com.silenteight.datasource.comments.api.v1.CommentInput;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class CommentsInputUseCase {

  public List<CommentInput> getCommentInputs(List<String> alerts) {
    return alerts.stream().map(alert ->
        CommentInput.newBuilder()
            .setAlert(alert)
            .setAlertCommentInput(Struct.newBuilder()
                .putFields("field1", Value.newBuilder().setStringValue("test1-" + alert).build())
                .putFields("field2", Value.newBuilder().setNumberValue(2).build())
                .build())
            .build()
    ).collect(Collectors.toList());
  }
}
