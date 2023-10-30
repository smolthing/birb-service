package com.birb.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.server.GrpcServer;

public class GrpcVerticle extends AbstractVerticle {

  @Override
  public void start() {
    final GrpcServer grpcServer = GrpcServer.server(vertx);

    GetBirbHandler.handle(grpcServer);
    GetBirbByTypeHandler.handle(grpcServer);

    final int port = 9000;
    vertx.createHttpServer().requestHandler(grpcServer)
      .listen(port, grpc-> {
        if (grpc.succeeded()) {
          System.out.println(String.format("gRPC running at port %d", port));
        } else {
          System.out.println(String.format("Failed to start gRPC at port %d", port));
        }
      });
  }

}
