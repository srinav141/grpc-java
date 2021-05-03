package com.github.srinav.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("I'm gRPC client");

        GreetingClient main = new GreetingClient();
        main.run();

    }


    public void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext().build();

        doUnaryCall(channel);
        doServerStreamCall(channel);
        doClientStreamingCall(channel);


        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel){
        System.out.println("Creating Stub");
        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

        //Created Greet Service client (blocking synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        //Unary
        //created protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder().setFirstName("Tester").setLastName("Exp").build();

        //do the same for a greet request
        GreetRequest greetRequest = GreetRequest.newBuilder().setGreeting(greeting).build();

        //call RPC and get Greet response (protocol buffers)
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println(greetResponse.getResult());
    }

    private void doServerStreamCall(ManagedChannel channel){
        System.out.println("Creating Stub");
        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

        //Created Greet Service client (blocking synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder().
                setGreeting(Greeting.newBuilder().setFirstName("Stream-Tester")).
                build();
        //Stream the responses in the blocking manner
        greetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse.getResult());
        });
    }


    private void doClientStreamingCall(ManagedChannel channel){
        //Create Asynchronous Client
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);


        StreamObserver<LongGreetRequest> requestStreamObserver =  asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //we get response from the server

                //onNext will be called only once

                System.out.println("Received response from server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                //we  get an error from the server

            }

            @Override
            public void onCompleted() {
                //Server done sending data
                //onCompleted will be called right after onNext
                System.out.println("Server has completed sending data");
                latch.countDown();

            }
        });

        System.out.println("Sending message-1");
        requestStreamObserver.
                onNext(LongGreetRequest.newBuilder().
                        setGreeting(Greeting.newBuilder().setFirstName("Client-Tester").build()).
                        build());
        System.out.println("Sending message-2");
        requestStreamObserver.
                onNext(LongGreetRequest.newBuilder().
                        setGreeting(Greeting.newBuilder().setFirstName("Client-Tester-2").build()).
                        build());
        System.out.println("Sending message-3");
        requestStreamObserver.
                onNext(LongGreetRequest.newBuilder().
                        setGreeting(Greeting.newBuilder().setFirstName("Client-Tester-3").build()).
                        build());

        requestStreamObserver.onCompleted();


        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
