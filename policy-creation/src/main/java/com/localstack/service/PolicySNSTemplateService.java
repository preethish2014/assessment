package com.localstack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.localstack.exception.SNSException;
import com.localstack.model.Policy;

public interface PolicySNSTemplateService {

    public String sendNotification(Policy policy) throws SNSException, JsonProcessingException;
}
