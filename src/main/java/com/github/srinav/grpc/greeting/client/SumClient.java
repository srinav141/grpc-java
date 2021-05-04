package com.github.srinav.grpc.greeting.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {

    public static void main(String[] args) {
        System.out.println("I'm sum client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext().build();

        SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);

        Add add = Add.newBuilder().setFirstNum(3).setSecondNum(10).build();

        SumRequest sumRequest = SumRequest.newBuilder().setAdd(add).build();

        SumResponse sumResponse = sumClient.sum(sumRequest);
        System.out.println(sumResponse.getResult());

    //Stream
        int num = 567890;


        sumClient.primeDecomp(PrimeDecompRequest.newBuilder().setNum(num).build()).forEachRemaining(primeDecompResponse -> {

            System.out.println(primeDecompResponse.getPrimeFactor());
        });

        //Error handling

        try {
            sumClient.squareRoot(SquareRootRequest.newBuilder().setNum(-1).build());
        }catch (RuntimeException e){
            System.out.println("Got Exception for square root");
            e.printStackTrace();
        }


    }
}
