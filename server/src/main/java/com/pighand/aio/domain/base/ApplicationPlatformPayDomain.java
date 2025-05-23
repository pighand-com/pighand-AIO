package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 应用 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Table("base_application_platform_pay")
@Data
public class ApplicationPlatformPayDomain extends BaseDomainRecord<ApplicationPlatformPayDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("projectPlatformPayCreate")
    @RequestFieldException("projectPlatformPayUpdate")
    @Schema(description = "项目id")
    private Long id;

    @Length(max = 10)
    @Schema(description = "微信支付 - 商户id")
    private String wechatMerchantId;

    @Length(max = 16)
    @Schema(description = "微信支付 - 私钥文件名(apiclient_key.pem)，默认商户id_key.pem")
    private String wechatMerchantPrivateKey;

    @Length(max = 16)
    @Schema(description = "微信支付 - 证书名(apiclient_cert.pem)，默认商户id_cert.pem")
    private String wechatMerchantCertificate;

    @Length(max = 41)
    @Schema(description = "微信支付 - 证书(apiclient_cert.pem)序列号。查看方式：openssl x509 -in apiclient_cert.pem -noout -serial")
    private String wechatMerchantCertificateSerial;

    @Length(max = 40)
    @Schema(description = "微信支付 - APIV3密钥")
    private String wechatMerchantV3;
}
