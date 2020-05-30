package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

public class MultipleChoiceIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("MultipleChoiceIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map session = handlerInput.getAttributesManager().getSessionAttributes();
        ArrayList evidence = (ArrayList) session.getOrDefault(EVIDENCE, new ArrayList());
        Slot slot = intentRequest.getIntent().getSlots().get("answer");
        ArrayList question = (ArrayList) session.get(CONTINUOUS_QUESTION);
        LinkedHashMap newEvidence = null;
        try {
            int answer = Integer.parseInt(slot.getValue()) - 1;
            newEvidence = (LinkedHashMap) question.get(answer);
            newEvidence.remove("name");
            newEvidence.put("choice_id", "present");
            if (newEvidence.containsKey("explanation")) { newEvidence.remove("name"); }

        } catch (Exception e) {
            String justAsked = (String) session.get(JUST_ASKED_Q);
            return handlerInput.getResponseBuilder()
                    .withSpeech("That didn't work, could you try again please.")
                    .withReprompt(justAsked)
                    .build();
        }


        evidence.add(newEvidence);
        // if the evidence.size() is greater than 1, that means that there was previous evidence saved.
        // And so we do not need to put it back into the session because it has already been updated.
        if (evidence.size() <= 1) {
            session.put(EVIDENCE, evidence);
        }

        session.put(CONTINUOUS_QUESTION, QUESTION_ANSWERED);

        BeginDiagnosisIntentHandler switchIntent = new BeginDiagnosisIntentHandler();
        return switchIntent.handle(handlerInput, intentRequest);
    }
}
