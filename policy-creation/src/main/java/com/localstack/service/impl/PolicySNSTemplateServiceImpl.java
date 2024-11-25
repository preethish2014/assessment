package com.localstack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.exception.SNSException;
import com.localstack.model.Policy;
import com.localstack.service.PolicySNSTemplateService;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PolicySNSTemplateServiceImpl  implements PolicySNSTemplateService {

    @Autowired
    private SnsTemplate snsTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String sendNotification(Policy policy) throws SNSException, JsonProcessingException {
        log.debug("Sending notification for {}", policy);
        try {
            String policyStr = objectMapper.writeValueAsString(policy);
            snsTemplate.sendNotification("my-topic", SnsNotification.of(policyStr));
        } catch (Exception ex) {
            log.error("exception occurred {}",  ex.getMessage());
            throw new SNSException(ex.getMessage());
        }
        return "SUCCESSFULLY SEND NOTIFICATION for "+policy.getName();
    }

}
