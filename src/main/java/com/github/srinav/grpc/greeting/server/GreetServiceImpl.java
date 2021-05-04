package com.github.srinav.grpc.greeting.server;

import com.proto.greet.*;
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


    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        try {
        for (int i=0; i<10;i++){
            String result = "Hello "+firstName+ " Response# "+i;
            GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();

            responseObserver.onNext(response);

                Thread.sleep(1000L);
            }
        }catch (InterruptedException e) {
                e.printStackTrace();

        }finally {
            responseObserver.onCompleted();
        }

        //super.greetManyTimes(request, responseObserver);
    }


    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> streamObserverOfRequest = new StreamObserver<LongGreetRequest>() {
            String result = "";
            @Override
            public void onNext(LongGreetRequest value) {
                //Client Sends message
                result += "Hello "+ value.getGreeting().getFirstName()+"! ";

            }

            @Override
            public void onError(Throwable t) {
                //client sends error

            }

            @Override
            public void onCompleted() {
                // client is done
                //this is when we want to return a response
                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();

            }
        };
        return streamObserverOfRequest;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {

       StreamObserver<GreetEveryoneRequest> streamObserver = new StreamObserver<GreetEveryoneRequest>() {
           @Override
           public void onNext(GreetEveryoneRequest value) {
               String response = "Hello "+value.getGreeting().getFirstName();
               GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder().
                                                                    setResult(response).build();
               responseObserver.onNext(greetEveryoneResponse);
           }

           @Override
           public void onError(Throwable t) {

           }

           @Override
           public void onCompleted() {
               responseObserver.onCompleted();
           }
       };
        return streamObserver;
    }
}
