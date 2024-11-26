package com.localstack.dao.impl;

import com.localstack.dao.SavePolicy;
import com.localstack.exception.PolicyException;
import com.localstack.model.Policy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class SavePolicyImpl implements SavePolicy {

    private final DynamoDbTable<Policy> policyTable;


    @Autowired
    public SavePolicyImpl(DynamoDbTable<Policy> policyTable) {
        this.policyTable = policyTable;
    }




    @Override
    public void savePolicy(Policy policy) throws PolicyException {
        log.info("inside save policy");
        try {
            upsert(policy);
        } catch (Exception ex) {
            throw new PolicyException("save policy failed");
        }
    }


    public Policy upsert(Policy policyTbl) {
        policyTable.putItem(policyTbl);
        return policyTbl;
    }




    public Optional<Policy> getPolicy(String code) {
        return Optional.ofNullable(
                policyTable.getItem(Key.builder().partitionValue(code).build()));
    }




    public String delete(String code) {
        policyTable.deleteItem(Key.builder().partitionValue(code).build());

        return "Policy has been deleted";
    }




    public List<Policy> getAllPolicies() {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().build();
        SdkIterable<Policy> policies = policyTable.scan(request).items();
        return policies.stream().toList();
    }

}
