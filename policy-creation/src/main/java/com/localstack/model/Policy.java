package com.localstack.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.lang.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Policy {

    @NotBlank
    String code;
    @NotBlank
    String name;
    @NotBlank
    String category;
    @NotBlank
    String premium;

    @DynamoDbPartitionKey
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @org.springframework.lang.NonNull
    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @org.springframework.lang.NonNull
    @DynamoDbAttribute("category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @NonNull
    @DynamoDbAttribute("premium")
    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }
}
