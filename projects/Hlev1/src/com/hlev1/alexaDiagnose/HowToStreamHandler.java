package com.hlev1.alexaDiagnose;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.hlev1.alexaDiagnose.handlers.*;

public class HowToStreamHandler extends SkillStreamHandler {


    private static Skill getSkill() {

        return Skills.standard()
                .addRequestHandlers(
                        new LaunchHandler(),
                        new FallbackIntentHandler(),
                        new BeginDiagnosisIntentHandler(),
                        new YesNoIntentHandler(),
                        new MultipleChoiceIntentHandler(),
                        new HelpIntentHandler(),
                        new RepeatIntentHandler(),
                        new ExitIntentHandler(),
                        new ErrorHandler(),
                        new SessionEndedHandler()
                )
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public HowToStreamHandler() {
        super(getSkill());
    }
}
