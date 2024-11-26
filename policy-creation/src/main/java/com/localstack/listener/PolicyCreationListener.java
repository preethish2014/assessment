package com.localstack.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.dao.SavePolicy;
import com.localstack.exception.ListenerException;
import com.localstack.model.Policy;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PolicyCreationListener {

    @Autowired
    SavePolicy savePolicy;

    @Autowired
    ObjectMapper objectMapper;

    @SqsListener("my-queue")
    public void listen(String policyStr) throws JsonProcessingException, ListenerException {
        if (StringUtils.isNoneEmpty(policyStr)) {
            try {
                Policy policy = objectMapper.readValue(policyStr, Policy.class);
                log.info("Message received - {}", policy);
                savePolicy.savePolicy(policy);
            } catch (Exception ex) {
                throw new ListenerException("Listener failed");
            }
            log.info("SUCCESSFULLY SAVED POLICY");
        }
    }

}
