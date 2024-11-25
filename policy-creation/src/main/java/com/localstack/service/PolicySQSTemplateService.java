package com.localstack.service;

import com.localstack.exception.SQSException;
import com.localstack.model.Policy;

public interface PolicySQSTemplateService {

    public String createPolicy(Policy policy) throws SQSException;
}
