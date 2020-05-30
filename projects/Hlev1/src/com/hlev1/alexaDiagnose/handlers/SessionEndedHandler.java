package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.SessionEndedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import java.util.Optional;

public class SessionEndedHandler implements SessionEndedRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, SessionEndedRequest request) {
        return true;
    }


    @Override
    public Optional<Response> handle(HandlerInput handlerInput, SessionEndedRequest request) {
        // any cleanup logic goes here
        return handlerInput.getResponseBuilder()
                .withSpeech("Ending session, bye.")
                .build();
    }
}
