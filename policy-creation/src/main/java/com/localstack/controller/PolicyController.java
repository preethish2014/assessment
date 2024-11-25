package com.localstack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.localstack.exception.SNSException;
import com.localstack.exception.SQSException;
import com.localstack.model.Policy;
import com.localstack.service.PolicySNSTemplateService;
import com.localstack.service.PolicySQSTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/policy")
public class PolicyController {


    @Autowired
    private PolicySQSTemplateService sqsService;

    @Autowired
    private PolicySNSTemplateService snsService;


    @PostMapping("/creation")
    public ResponseEntity<String> createPolicy(@RequestHeader HttpHeaders httpHeaders, @RequestBody @Validated Policy policy) throws SQSException {
        log.info("inside create policy");
        String status = sqsService.createPolicy(policy);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/notification")
    public ResponseEntity<String> publishNotification(@RequestHeader HttpHeaders httpHeaders, @RequestBody Policy policy) throws SNSException, JsonProcessingException {
        log.info("inside publishNotification");
        String status = snsService.sendNotification(policy);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
