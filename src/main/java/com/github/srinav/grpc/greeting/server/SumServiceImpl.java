package com.github.srinav.grpc.greeting.server;

import com.proto.sum.Add;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        Add add = request.getAdd();

        int firstNum = add.getFirstNum();
        int secondNum = add.getSecondNum();

        int result = firstNum+secondNum;

        SumResponse sumResponse = SumResponse.newBuilder().setResult(result).build();

        responseObserver.onNext(sumResponse);

        responseObserver.onCompleted();
        //super.sum(request, responseObserver);
    }
}
