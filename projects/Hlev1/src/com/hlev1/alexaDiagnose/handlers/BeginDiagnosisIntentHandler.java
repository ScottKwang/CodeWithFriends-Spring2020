package com.hlev1.alexaDiagnose.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.Intent;
import com.hlev1.alexaDiagnose.utils.SkillUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.util.*;
import static java.util.Map.entry;

import static com.hlev1.alexaDiagnose.utils.SessionStorage.*;

public class BeginDiagnosisIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
        return intentRequest.getIntent().getName().equals("BeginDiagnosisIntent");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Map session = handlerInput.getAttributesManager().getSessionAttributes();
        String age = "";
        String gender = "";
        String questionType = "";
        JSONObject questionObj = null;
        JSONArray listOfQuestions = null;
        String evidence = "[]";


        // Ask another question
        Object obj = session.getOrDefault(CONTINUOUS_QUESTION, "");
        if (obj.getClass() == String.class) {
            if (obj.equals(QUESTION_ANSWERED)) {
                ArrayList ev = (ArrayList) session.getOrDefault(EVIDENCE, new ArrayList<>()); // Collect the evidence from a previous question
                evidence = formatEvidence(ev);
                age = (String) session.getOrDefault(AGE, "");
                gender = (String) session.getOrDefault(GENDER, "");

            } else {
                // Initial dialog run through
                DialogState dialog = intentRequest.getDialogState();

                if (!dialog.getValue().toString().equals("COMPLETED")) {

                    Intent thisIntent = intentRequest.getIntent();
                    return handlerInput.getResponseBuilder()
                            .addDelegateDirective(thisIntent)
                            .build();
                } else {
                    Map slots = intentRequest.getIntent().getSlots();
                    age = ((Slot) slots.get("age")).getValue();
                    gender = ((Slot) slots.get("gender")).getValue();

                    session.put(AGE, age);
                    session.put(GENDER, gender);
                }
            }
        }


        try {
            JSONObject apiResponse = makePOST("https://api.infermedica.com/covid19/diagnosis",
                                                Integer.parseInt(age), gender, evidence);

            if ((Boolean) apiResponse.get("should_stop")) {
                JSONObject triageResponse = makePOST("https://api.infermedica.com/covid19/triage",
                                                Integer.parseInt(age), gender, evidence);

                String advice = (String) triageResponse.get("description");

                return handlerInput.getResponseBuilder()
                        .withSpeech(advice)
                        .build();
            }

            questionObj = (JSONObject) apiResponse.get("question");
            listOfQuestions = (JSONArray) questionObj.get("items");
            questionType = (String) questionObj.get("type");

        } catch (Exception e) {
            // ERROR HANDLING
            return handlerInput.getResponseBuilder()
                    .withSpeech(e.getMessage())
                    .withReprompt("I've heard enough")
                    .build();
        }

        JSONObject nextQuestion;
        String nextQuestionText;
        String nextQuestionId;
        String questionOverview;

        switch (questionType) {
            case "single":
                nextQuestion = (JSONObject) listOfQuestions.remove(0);
                nextQuestionText = (String) nextQuestion.get("name");
                nextQuestionId = (String) nextQuestion.get("id");

                session.put(CONTINUOUS_QUESTION, QUESTION_ANSWERED);
                session.put(JUST_ASKED_ID, nextQuestionId);
                session.put(JUST_ASKED_Q, nextQuestionText);

                return handlerInput.getResponseBuilder()
                        .withSpeech(nextQuestionText)
                        .withReprompt("well? yes or no")
                        .build();

            case "group_single":

                nextQuestionText = (String) questionObj.get("text");

                ArrayList askedOptions = new ArrayList();
                final String[] numNames = {"zero","one","two","three","four","five","six","seven","eight","nine","ten"};

                for (int i = 0; i < listOfQuestions.size(); i++) {
                    JSONObject nextOption = (JSONObject) listOfQuestions.get(i);
                    nextOption.remove("choices");
                    askedOptions.add(nextOption);

                    String nextOptionText = ((String) nextOption.get("name")).replaceAll("\\(.*?\\)","");
                    String nextOptionLabel = numNames[i+1];

                    String and = (i == listOfQuestions.size() - 1) ? "And " : "";

                    nextQuestionText += "\n";
                    nextQuestionText += and + nextOptionLabel + ", ";
                    nextQuestionText += nextOptionText + ".";
                }

                session.put(CONTINUOUS_QUESTION, askedOptions);
                session.put(JUST_ASKED_Q, nextQuestionText);

                return handlerInput.getResponseBuilder()
                        .withSpeech(nextQuestionText)
                        .withReprompt("Please choose one.")
                        .build();

            case "group_multiple":
                questionOverview = "Please reply with yes or no to all of the following " +
                        "statements that apply to you. ";

                nextQuestion = (JSONObject) listOfQuestions.remove(0);
                nextQuestionText = (String) nextQuestion.get("name");
                nextQuestionId = (String) nextQuestion.get("id");

                session.put(CONTINUOUS_QUESTION, listOfQuestions);
                session.put(JUST_ASKED_ID, nextQuestionId);
                session.put(JUST_ASKED_Q, nextQuestionText);

                return handlerInput.getResponseBuilder()
                        .withSpeech(questionOverview + nextQuestionText)
                        .withReprompt("well? yes or no")
                        .build();

            default:
                break;
        }

        Intent thisIntent = intentRequest.getIntent();
        return handlerInput.getResponseBuilder()
                .addDelegateDirective(thisIntent)
                .build();
    }

    private JSONObject makePOST(String url, int age, String gender, String evidence) throws UnirestException, ParseException {
        String json = String.format("{\n    \"sex\": \"%s\",\n    \"age\": %d,\n    \"evidence\": %s\n}", gender, age, evidence);

        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .header("App-Id", "bd3cbf8c")
                .header("App-Key", "48a6449979b3156bda8756b746860788")
                .header("User-Agent", "PostmanRuntime/7.19.0")
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Postman-Token", "c8ad7e10-ccf0-4bfc-86ab-cf6c15db2bef,67e475e7-0092-481a-8915-95a40dfe03b2")
                .header("Host", "api.infermedica.com")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .header("cache-control", "no-cache")
                .body(json)
                .asString();

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(response.getBody());

        return obj;
    }

    private String formatEvidence(ArrayList<LinkedHashMap> evidence) {
        String out = "[";
        for (int i = 0; i < evidence.size(); i++) {
            LinkedHashMap h = evidence.get(i);
            String s = String.format("{\"id\": \"%s\", \"choice_id\": \"%s\"},", h.get("id"), h.get("choice_id"));

            if (i == evidence.size() - 1) {
                s = s.substring(0, s.length() - 1);
            }
            out += s;
        }

        return out + "]";
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? (String.valueOf((char)(i + 64))).toUpperCase() : null;
    }

}
