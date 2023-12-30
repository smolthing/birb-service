package com.birb.starter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.birb.protobuf.grpc.BirbServiceGrpc;
import com.birb.protobuf.grpc.BirbServiceGrpc.BirbServiceFutureStub;
import com.birb.protobuf.grpc.GetBirbRequest;
import com.birb.protobuf.grpc.GetBirbResponse;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


@ExtendWith(VertxExtension.class)
public class GetBirbHandlerTest extends BaseGrpcHandlerTest {

  private static final long BIRB_ID = 1L;

  @Test
  @DisplayName("should return response successfully using blocking stub")
  void testBirbHandlerWithBlockingStub(VertxTestContext testContext) {
    GetBirbResponse response = birbStub.getBirb(GetBirbRequest.newBuilder().setId(BIRB_ID).build());
    assertEquals(BIRB_ID, response.getBirb().getId());

    testContext.completeNow();
  }

  @Test
  @DisplayName("should return response successfully using stub")
  void testBirbHandlerWithStub(VertxTestContext testContext) {
    BirbServiceGrpc.BirbServiceStub birbStub = BirbServiceGrpc.newStub(channel);

    StreamObserver<GetBirbResponse> responseObserver = new StreamObserver<GetBirbResponse>() {
      @Override
      public void onNext(GetBirbResponse response) {
        assertEquals(BIRB_ID, response.getBirb().getId());
        testContext.completeNow();
      }

      @Override
      public void onError(Throwable t) {
        testContext.failNow(t);
      }

      @Override
      public void onCompleted() {
        // Do nothing
      }
    };

    birbStub.getBirb(GetBirbRequest.newBuilder().setId(BIRB_ID).build(), responseObserver);
  }

  @Test
  @DisplayName("should return response successfully using future stub")
  void testBirbHandlerWithFutureStub(VertxTestContext testContext)
    throws ExecutionException, InterruptedException {
    BirbServiceFutureStub birbStub = BirbServiceGrpc.newFutureStub(channel);

    ListenableFuture<GetBirbResponse> future = birbStub.getBirb(
      GetBirbRequest.newBuilder().setId(BIRB_ID).build());

    GetBirbResponse response = future.get();

    assertEquals(BIRB_ID, response.getBirb().getId());
    testContext.completeNow();
  }

  static Stream<Arguments> testGetBirbHandlerErrorNotFound_Source(){
    return Stream.of(
      Arguments.of(GetBirbRequest.newBuilder().build()),
      Arguments.of(GetBirbRequest.newBuilder().setId(-1).build()),
      Arguments.of(GetBirbRequest.newBuilder().setId(100).build())
    );
  }
  @ParameterizedTest(name = "should return NOT FOUND error for request {0}")
  @MethodSource("testGetBirbHandlerErrorNotFound_Source")
  void testGetBirbHandlerErrorNotFound(GetBirbRequest request, VertxTestContext testContext) {
    StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
      birbStub.getBirb(request);
    }, "Exception occurred while invoking birbStub.getBirb");

    assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
    testContext.completeNow();
  }
}
