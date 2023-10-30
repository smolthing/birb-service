package com.birb.starter;

import com.birb.protobuf.grpc.Birb;
import com.birb.protobuf.grpc.BirbServiceGrpc;
import com.birb.protobuf.grpc.BirbType;
import com.birb.protobuf.grpc.GetBirbByTypebResponse;
import com.birb.protobuf.grpc.GetBirbResponse;
import io.vertx.grpc.common.GrpcStatus;
import io.vertx.grpc.server.GrpcServer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetBirbByTypeHandler {

  private static final Map<Long, Birb> listOfBirb = new HashMap<>();

  static {
    listOfBirb.put(1L, build(1L, "ah", "ahhhhh", 0.1f, BirbType.PIGEON));
    listOfBirb.put(2L, build(2L, "bibi", "biiiii", 0.5f, BirbType.COCKATIEL));
    listOfBirb.put(3L, build(3L, "caca", "caaacaaaa", 0.3f, BirbType.PIGEON));
    listOfBirb.put(4L, build(4L, "didi", "didodido", 0.8f, BirbType.MYNAH));
    listOfBirb.put(5L, build(5L, "eheh", "eheheheh", 0.3f, BirbType.PIGEON));

  }
  private static Birb build(long id, String name, String sound, float volume, BirbType type) {
    return Birb.newBuilder()
      .setId(id)
      .setName(name)
      .setSound(sound)
      .setVolume(volume)
      .setType(type)
      .build();
  }

  public static void handle(GrpcServer grpcServer) {
    grpcServer.callHandler(BirbServiceGrpc.getGetBirbByTypeMethod(), request -> {
      request.handler( req -> {
        final BirbType type = req.getType();
        List<Birb> filteredBirb = listOfBirb.values().stream()
          .filter(birb -> birb.getType() == type).collect(
            Collectors.toList());

        if (!filteredBirb.isEmpty()) {
          request.response().end(GetBirbByTypebResponse.newBuilder().addAllBirb(filteredBirb).build());
        } else {
          request.response().status(GrpcStatus.NOT_FOUND).end();
        }
      });
    });
  }
}
