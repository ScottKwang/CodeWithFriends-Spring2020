/*
     Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler;
import com.amazon.ask.model.Intent;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import org.json.simple.JSONArray;

import java.util.Optional;
import java.util.ResourceBundle;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

public class LaunchHandler implements LaunchRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, LaunchRequest launchRequest) {
        final ResourceBundle messages = SkillUtils.getResourceBundle(handlerInput, "Messages");

        String speechText = String.format(messages.getString("WELCOME_MESSAGE"), messages.getString("SKILL_NAME"));
        //String repromptText = messages.getString("FIND_PROFILE");
        String repromptText = "To begin your diagnosis, please say begin diagnosis";
        speechText += " " + "To begin your diagnosis, please say begin diagnosis";

        //Intent chainedIntent = Intent.builder().withName("BeginDiagnosisIntent").build();
        return handlerInput.getResponseBuilder()
                //.addDelegateDirective(chainedIntent)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}
