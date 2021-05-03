package com.github.srinav.grpc.greeting.server;

import com.proto.sum.*;
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
}
