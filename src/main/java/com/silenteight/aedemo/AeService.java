package com.silenteight.aedemo;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.Analysis.NotificationFlags;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;
import com.silenteight.adjudication.api.v2.StreamRecommendationsWithMetadataRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AeService {

  private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 24801)
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

    var alerts = IntStream.range(0, 25000).mapToObj(i -> Alert
        .newBuilder()
        .setAlertId("alertinio" + i)
        .setName("alert")
        .addMatches(Match
            .newBuilder()
            .setMatchId("bla" + i)
            .setName("Match")
            .build())
        .build()).collect(Collectors.toList());

    var response = alertStub.batchCreateAlerts(BatchCreateAlertsRequest
        .newBuilder()
        .addAllAlerts(alerts)
        .build());
    this.alerts = new ArrayList<>();
    this.alerts.add(response.getAlerts(0));
    this.alerts.add(response.getAlerts(1));
    return response.toString();
  }

  public String createMatches() {

    var matches = IntStream.range(0, 25000).mapToObj(i -> Match
        .newBuilder()
        .setMatchId("bla" + String.valueOf(i))
        .setName("Match")
        .build()).collect(Collectors.toList());

    for (int i = 0; i < 25000; i++) {
      alertStub.batchCreateMatches(BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(
          List.of(
              BatchCreateAlertMatchesRequest
                  .newBuilder()
                  .setAlert(alerts.get(0).getName())
                  .addMatches(Match
                      .newBuilder()
                      .setMatchId("bla" + String.valueOf(i))
                      .setName("Match")
                      .build())
                  .build())).build());
    }

    var response =
        alertStub.batchCreateMatches(BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(
            List.of(
                BatchCreateAlertMatchesRequest
                    .newBuilder()
                    .setAlert(alerts.get(0).getName())
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
        .setAnalysis(Analysis.newBuilder()
            .setName("analysis/444")
            .addAllFeatures(List.of(Feature
                .newBuilder()
                .setFeature("features/name")
                .setAgentConfig("agents/name/versions/1.0.0/configs/1")
                .build()))
            .setNotificationFlags(
                NotificationFlags
                    .newBuilder()
                    .setAttachMetadata(true)
                    .setAttachRecommendation(true)
                    .build()).build())
        .build());
    analysis = response.getName();
    var addDataSetResponse = analysisStub.addDataset(
        AddDatasetRequest.newBuilder().setAnalysis(analysis).setDataset(dataset).build());
    return addDataSetResponse.toString();
  }

  public String addDataSet() {
    var response = analysisStub.addDataset(
        AddDatasetRequest.newBuilder().setDataset(dataset).setAnalysis(analysis).build());
    return response.toString();
  }

  String getRecommendation() {
    var reco = recommendationStub
        .streamRecommendationsWithMetadata(StreamRecommendationsWithMetadataRequest
            .newBuilder()
            .setRecommendationSource(analysis)
            .buildPartial());
    StringBuilder response = new StringBuilder();
    while (reco.hasNext()) {
      var recommendation = reco.next().toString();
      log.info(recommendation);
      response.append(recommendation);
    }
    return response.toString();
  }

  public static int randInt(int min, int max) {
    Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }
}
