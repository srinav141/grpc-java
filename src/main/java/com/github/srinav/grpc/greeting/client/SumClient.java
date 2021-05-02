package com.github.srinav.grpc.greeting.client;

import com.proto.sum.Add;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
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
    }
}
