package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

public class ErrorHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final String speechOutput = "Sorry, I can't understand the command. Please say again.";
        return handlerInput.getResponseBuilder()
                .withSpeech(speechOutput)
                .withReprompt(speechOutput)
                .build();
    }
}
