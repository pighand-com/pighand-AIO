package com.pighand.aio.service.ECommerce.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyVO {

    @JsonProperty("original_type")
    private String originalType;

    private String algorithm;

    private String ciphertext;

    private String nonce;

    @JsonProperty("associated_data")
    private String associatedData;
}
