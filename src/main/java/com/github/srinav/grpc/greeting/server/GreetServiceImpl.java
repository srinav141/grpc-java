package com.github.srinav.grpc.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //Extract the fields needed
        Greeting greeting =  request.getGreeting();
        String firstName = greeting.getFirstName();

        // Create Response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //Send response
        responseObserver.onNext(response);


        //Complete RPC call
        responseObserver.onCompleted();

        //super.greet(request, responseObserver);
    }
}
