package com.github.srinav.grpc.greeting.server;

import com.proto.sum.*;
import io.grpc.Status;
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


    @Override
    public void primeDecomp(PrimeDecompRequest request, StreamObserver<PrimeDecompResponse> responseObserver) {

        int number = request.getNum();
        int div = 2;

        while(number > 1){
            if(number  % div == 0){
                number = number/div;
                responseObserver.onNext(PrimeDecompResponse.newBuilder().
                        setPrimeFactor(div).build());

            }else{
                div  +=1;
            }

        }
        responseObserver.onCompleted();
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer num = request.getNum();

        if (num >= 0){
            double num_sqrt = Math.sqrt(num);
            responseObserver.onNext(SquareRootResponse.newBuilder().setNumSqrt(num_sqrt).build());

            responseObserver.onCompleted();

        }else{

            responseObserver.onError(Status.INVALID_ARGUMENT.
                    withDescription("Given num is not positive").
                    augmentDescription("Number sent: "+num).
                    asRuntimeException());
        }
    }
}
