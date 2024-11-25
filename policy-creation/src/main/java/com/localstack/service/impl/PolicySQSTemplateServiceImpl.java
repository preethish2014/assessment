package com.localstack.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.exception.SQSException;
import com.localstack.model.Policy;
import com.localstack.service.PolicySQSTemplateService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.localstack.constants.Constants.MY_QUEUE;

@Slf4j
@Service
public class PolicySQSTemplateServiceImpl implements PolicySQSTemplateService {


    @Autowired
    private SqsTemplate sqsTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String createPolicy(Policy policy) throws SQSException {
        log.info("Inside create Policy");
        try {
            String policyStr = objectMapper.writeValueAsString(policy);
            sqsTemplate.send(to -> to.queue(MY_QUEUE).payload(policyStr));
        } catch (Exception ex) {
            log.error("exception occurred {}",  ex.getMessage());
            throw new SQSException(ex.getMessage());
        }
        return "SUCCESSFULLY CREATED POLICY "+policy.getName();
    }
}
