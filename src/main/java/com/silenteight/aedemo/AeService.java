package com.silenteight.aedemo;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;
import com.silenteight.adjudication.api.v2.StreamRecommendationsRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AeService {

  private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 24802)
      .usePlaintext()
      .build();

  private AlertServiceGrpc.AlertServiceBlockingStub alertStub
      = AlertServiceGrpc.newBlockingStub(channel);

  private DatasetServiceGrpc.DatasetServiceBlockingStub datasetStub =
      DatasetServiceGrpc.newBlockingStub(channel);

  private AnalysisServiceGrpc.AnalysisServiceBlockingStub analysisStub =
      AnalysisServiceGrpc.newBlockingStub(channel);

  private RecommendationServiceGrpc.RecommendationServiceBlockingStub recommendationStub =
      RecommendationServiceGrpc.newBlockingStub(channel);

  private List<Alert> alerts = new ArrayList<>();

  private String analysis;
  private String dataset;

  public String createAlerts() {

    var response = alertStub.batchCreateAlerts(BatchCreateAlertsRequest
        .newBuilder()
        .addAlerts(Alert.newBuilder().setAlertId("1").setName("alert").build())
        .addAlerts(Alert.newBuilder().setAlertId("2").setName("alert2").build())
        .build());
    alerts.add(response.getAlerts(0));
    alerts.add(response.getAlerts(1));
    return response.toString();
  }

  public String createMatches() {

    var matches = List.of(
        Match.newBuilder().setMatchId("1").setName("Match").build(),
        Match.newBuilder().setMatchId("2").setName("Match2").build());

    var response =
        alertStub.batchCreateMatches(BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(
            List.of(
                BatchCreateAlertMatchesRequest
                    .newBuilder()
                    .setAlert(alerts.get(0).getName())
                    .addAllMatches(matches)
                    .build(),
                BatchCreateAlertMatchesRequest
                    .newBuilder()
                    .setAlert(alerts.get(1).getName())
                    .addAllMatches(matches)
                    .build())).build());

    return response.toString();
  }

  public String createDataset() {
    var response = datasetStub.createDataset(CreateDatasetRequest.newBuilder().setNamedAlerts(
        NamedAlerts
            .newBuilder()
            .addAllAlerts(List.of(alerts.get(0).getName(), alerts.get(1).getName()))
            .build()).build());
    dataset = response.getName();
    return response.toString();
  }

  public String createAnalysis() {
    var response = analysisStub.createAnalysis(CreateAnalysisRequest
        .newBuilder()
        .setAnalysis(Analysis.newBuilder().setName("analysis/1").build())
        .build());
    analysis = response.getName();
    var addDataSetResponse = analysisStub.addDataset(
        AddDatasetRequest.newBuilder().setAnalysis(analysis).setDataset(dataset).build());
    return addDataSetResponse.toString();
  }

  String getRecommendation() {
    var reco = recommendationStub
        .streamRecommendations(StreamRecommendationsRequest
            .newBuilder()
            .setRecommendationSource(analysis)
            .buildPartial());
    StringBuilder response = new StringBuilder();
    while (reco.hasNext()) {
      response.append(reco.next().toString());
    }
    return response.toString();
  }
}
