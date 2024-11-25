package com.localstack.dao;

import com.localstack.exception.PolicyException;
import com.localstack.model.Policy;

public interface SavePolicy {

    public void savePolicy(Policy policy) throws PolicyException;

}
