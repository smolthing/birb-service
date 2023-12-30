package com.birb.starter;

import com.birb.protobuf.grpc.BirbServiceGrpc;
import com.birb.protobuf.grpc.BirbServiceGrpc.BirbServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseGrpcHandlerTest {
  protected Vertx vertx;
  protected ManagedChannel channel;
  protected BirbServiceBlockingStub birbStub;

  @BeforeEach
  void setUp(Vertx vertx, VertxTestContext testContext){
    this.vertx = vertx;

    vertx.deployVerticle(new GrpcVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    channel = VertxChannelBuilder.forAddress(vertx, "localhost", 9000).usePlaintext().build();
    birbStub = BirbServiceGrpc.newBlockingStub(channel);
  }

  @AfterEach
  void closeChannel() {
    if (channel != null) {
      channel.shutdownNow();
    }
  }

}
