package com.mete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class MailTemplate {

    @Value("${template.registration_succesfull}")
    private String registrationTemplate;

    @Value("${template.cargo_delivered}")
    private String cargo_deliveredTemplate;

    @Value("${template.cargo_saved}")
    private String cargo_savedTemplate;

    @Value("${template.cargo_onDelivery}")
    private String cargo_onDeliveryTemplate;

    @Value("${template.cargo_atBranch}")
    private String cargo_atBranchTemplate;

    @Value("${template.cargo_return_sender}")
    private String cargo_returnTemplateSender;

    @Value("${template.cargo_return_reciever}")
    private String cargo_returnTemplateReciever;

    Map<Integer, String> getTemplates(){
        return Map.of(
                1,registrationTemplate,
                2,cargo_deliveredTemplate,
                3,cargo_savedTemplate,
                4,cargo_onDeliveryTemplate,
                5,cargo_atBranchTemplate,
                6,cargo_returnTemplateSender,
                7,cargo_returnTemplateReciever
        );
    }

}
