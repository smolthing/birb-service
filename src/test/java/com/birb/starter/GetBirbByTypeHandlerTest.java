package com.birb.starter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.birb.protobuf.grpc.Birb;
import com.birb.protobuf.grpc.BirbType;
import com.birb.protobuf.grpc.GetBirbByTypeRequest;
import com.birb.protobuf.grpc.GetBirbByTypebResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


@ExtendWith(VertxExtension.class)
public class GetBirbByTypeHandlerTest extends BaseGrpcHandlerTest {

  @Test
  void testGetBirbByTypeHandler(VertxTestContext testContext) {
    GetBirbByTypebResponse response = birbStub.getBirbByType(
      GetBirbByTypeRequest.newBuilder().setType(BirbType.COCKATIEL).build());

    assertEquals(1, response.getBirbCount());

    var expectedBirb = Birb.newBuilder()
      .setId(2L)
      .setName("bibi")
      .setSound("biiiii")
      .setVolume(0.5f)
      .setType(BirbType.COCKATIEL)
      .build();
    assertEquals(expectedBirb, response.getBirbList().get(0));

    testContext.completeNow();
  }

  static Stream<Arguments> testGetBirbTypeHandlerErrorNotFound_Source(){
    return Stream.of(
      Arguments.of(GetBirbByTypeRequest.newBuilder().build()),
      Arguments.of(GetBirbByTypeRequest.newBuilder().setType(BirbType.UNKNOWN).build()),
      Arguments.of(GetBirbByTypeRequest.newBuilder().setTypeValue(-1).build())
    );
  }
  @ParameterizedTest
  @MethodSource("testGetBirbTypeHandlerErrorNotFound_Source")
  void testGetBirbTypeHandlerErrorNotFoundWithMethodSource(GetBirbByTypeRequest request, VertxTestContext testContext) {
    try {
      birbStub.getBirbByType(request);
    } catch (StatusRuntimeException e) {
      assertEquals(Status.NOT_FOUND.getCode(), e.getStatus().getCode());
    }
    testContext.completeNow();
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void testGetBirbTypeHandlerByErrorNotFoundWithValueSource(int type, VertxTestContext testContext) {
    try {
      birbStub.getBirbByType(GetBirbByTypeRequest.newBuilder().setTypeValue(type).build());
    } catch (StatusRuntimeException e) {
      assertEquals(Status.NOT_FOUND.getCode(), e.getStatus().getCode());
    }
    testContext.completeNow();
  }
}
