package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;
import java.util.ResourceBundle;

public class ExitIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("AMAZON.StopIntent") ||
                intentRequest.getIntent().getName().equals("AMAZON.CancelIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");
        return handlerInput.getResponseBuilder()
                .withSpeech(messages.getString("STOP_MESSAGE"))
                .build();
    }
}
