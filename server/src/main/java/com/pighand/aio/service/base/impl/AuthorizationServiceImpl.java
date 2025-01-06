package com.pighand.aio.service.base.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pighand.aio.common.enums.AuthorizationAlgEnum;
import com.pighand.aio.common.enums.AuthorizationTypeEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.ApplicationAuthorizationDomain;
import com.pighand.aio.service.base.ApplicationAuthorizationService;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.exception.ThrowPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 授权服务
 *
 * @author wangshuli
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    ObjectMapper om = new ObjectMapper();

    @Value("spring.profiles.active")
    private String env;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ApplicationAuthorizationService projectAuthorizationService;

    /**
     * redis key
     *
     * @param key userId 或 hash
     * @returns
     */
    private String getRedisKey(String key) {
        return "auth:" + key;
    }

    /**
     * 获取JWT签名算法
     *
     * @param alg
     * @param jwtSalt
     * @returns
     */
    private Algorithm getJWTSign(AuthorizationAlgEnum alg, String jwtSalt) {
        switch (alg) {
            case HMAC256:
                return Algorithm.HMAC256(jwtSalt);
            case HMAC384:
                return Algorithm.HMAC384(jwtSalt);
            case HMAC512:
                return Algorithm.HMAC512(jwtSalt);
            default:
                throw new ThrowException("不支持的签名算法");
        }
    }

    /**
     * 获取Hash签名算法
     *
     * @param alg
     * @returns
     */
    private String getHashSign(AuthorizationAlgEnum alg) {
        switch (alg) {
            case HMAC256:
                return "SHA-256";
            case HMAC384:
                return "SHA-348";
            case HMAC512:
                return "SHA-512";
            default:
                throw new ThrowException("不支持的签名算法");
        }
    }

    /**
     * 生成jwt token
     *
     * <p>如果支持退出登录，会将userId存入redis，根据redis是否存在判断token是否有效。同时将在token中标记是否校验redis
     * <p>如果非正式环境，会将环境变量存入redis或token中。用于校验不同环境
     *
     * @param projectAuthorization
     * @param userInfo
     * @returns jwt
     */
    private String generateJWTToken(ApplicationAuthorizationDomain projectAuthorization, UserVO userInfo) {
        if (userInfo == null) {
            throw new ThrowPrompt("用户不存在");
        }

        JWTCreator.Builder jwtBuilder = JWT.create();

        // 根据jwtBody中的key，从userInfo和extension中获取对应的值
        Map<String, Object> jwtBody = projectAuthorization.getJwtBody();
        if (jwtBody != null) {

            // 使用om，将userInfo和extension转换为map，方便获取值
            Map userMap = userInfo != null ? om.convertValue(userInfo, Map.class) : null;

            for (String key : jwtBody.keySet()) {
                String value = jwtBody.get(key).toString();
                String[] tableAndField = value.split("\\.");

                Object tableValue = "";
                if (tableAndField.length == 1) {
                    tableValue = userMap.get(tableAndField[0]);
                } else {
                    Map subMap = userMap.get(tableAndField[0]) != null ?
                        om.convertValue(userMap.get(tableAndField[0]), Map.class) : null;
                    tableValue = subMap.get(tableAndField[1]);
                }

                jwtBuilder.withClaim(key, tableValue.toString());
            }
        }

        // 如果需要登录校验，则将登录信息存入redis
        boolean isSetToRedis = false;
        if (projectAuthorization.getSupportLogout()) {
            jwtBuilder.withClaim("supportLogout", true);

            isSetToRedis = true;
        }

        if (isSetToRedis) {
            String redisKey = this.getRedisKey(userInfo.getId().toString());
            redisTemplate.opsForHash().put(redisKey, "env", env);
            redisTemplate.expire(redisKey, projectAuthorization.getExpiresIn(), TimeUnit.SECONDS);
        }

        // 已经存入redis，环境校验通过redis校验；或者生产环境，不存如token。节省jwt体积
        if (projectAuthorization.getMatchingEnv() && !isSetToRedis && !env.equals("prod")) {
            jwtBuilder.withClaim("env", env);
        }

        // _c：是否存入redis
        jwtBuilder.withClaim("id", userInfo.getId());
        if (isSetToRedis) {
            jwtBuilder.withClaim("_c", 1);
        }

        // 生成token
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.SECOND, projectAuthorization.getExpiresIn());

        String token = jwtBuilder.withExpiresAt(expires.getTime())
            .sign(this.getJWTSign(projectAuthorization.getAlg(), projectAuthorization.getJwtSalt()));

        return token;
    }

    /**
     * 生成hash token
     *
     * @param authorization
     * @param userId
     * @returns hashPrefix + hash(base64)
     */
    @Override
    public String generateHashToken(ApplicationAuthorizationDomain authorization, Long userId) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(this.getHashSign(authorization.getAlg()));
        } catch (NoSuchAlgorithmException e) {
            throw new ThrowException(e.getMessage());
        }
        byte[] byteOfTextToHash = userId.toString().getBytes(StandardCharsets.UTF_8);
        byte[] hashedByteArray = digest.digest(byteOfTextToHash);
        String encoded = Base64.getEncoder().encodeToString(hashedByteArray);

        String redisKey = this.getRedisKey(encoded);
        redisTemplate.opsForHash().putAll(redisKey, Map.of("env", env, "id", userId.toString()));
        redisTemplate.expire(redisKey, authorization.getExpiresIn(), TimeUnit.SECONDS);

        String prefix = Optional.ofNullable(authorization.getHashPrefix()).orElse("");

        return prefix + encoded;
    }

    /**
     * 生成授权
     *
     * @returns token
     */
    @Override
    public UserVO generateToken(UserVO userInfo) {
        Long applicationId = Context.applicationId();

        ApplicationAuthorizationDomain projectAuthorization = projectAuthorizationService.getById(applicationId);

        String token = "";
        if (AuthorizationTypeEnum.JWT.equals(projectAuthorization.getType())) {
            token = this.generateJWTToken(projectAuthorization, userInfo);
        } else {
            token = this.generateHashToken(projectAuthorization, userInfo.getId());
        }
        userInfo.setToken(token);

        userInfo.setPassword(null);

        return userInfo;
    }

    /**
     * 校验token
     *
     * <p>jwt根据_c判断是否校验redis，否则只校验jwt hash校验redis
     *
     * @param authorization
     * @returns userId
     */
    @Override
    public LoginUser checkToken(String authorization) {
        Long applicationId = Context.applicationId();
        ApplicationAuthorizationDomain projectAuthorization = projectAuthorizationService.getById(applicationId);

        String checkEnv = null;
        DecodedJWT decodedJWT = null;

        String redisKey = this.getRedisKey(authorization);
        Boolean checkRedis = true;

        // 解析jwt
        if (AuthorizationTypeEnum.JWT.equals(projectAuthorization.getType())) {
            try {
                JWTVerifier verifier =
                    JWT.require(this.getJWTSign(projectAuthorization.getAlg(), projectAuthorization.getJwtSalt()))
                        .build();
                decodedJWT = verifier.verify(authorization);

                redisKey = this.getRedisKey(decodedJWT.getClaim("id").asString());
                checkEnv = Optional.ofNullable(decodedJWT.getClaim("env")).map(Claim::asString).orElse(null);
                checkRedis = Optional.ofNullable(decodedJWT.getClaim("_c")).map(Claim::asBoolean).orElse(false);
            } catch (Exception e) {
                throw new ThrowException("授权错误", 401);
            }
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setId(Long.valueOf(decodedJWT.getClaim("id").toString()));

        // redis校验：登录状态、所属环境
        if (checkRedis) {
            Map redisInfo = redisTemplate.opsForHash().entries(redisKey);

            if (redisInfo == null) {
                throw new ThrowException("授权错误", 401);
            }

            checkEnv = StringUtils.hasText(checkEnv) ? checkEnv : redisInfo.get("env").toString();

            if (loginUser.getId() == null) {
                loginUser.setId(Long.valueOf(redisInfo.get("id").toString()));
            }
        }

        // 校验环境变量
        if (projectAuthorization.getMatchingEnv() && !env.equals("prod") && !env.equals(checkEnv)) {
            throw new ThrowException("授权错误", 401);
        }

        return loginUser;
    }

    /**
     * 登出
     *
     * <p>删除redis中的授权信息
     *
     * @param authorization
     */
    @Override
    public void logout(String authorization) {
        Long applicationId = Context.applicationId();
        ApplicationAuthorizationDomain projectAuthorization = projectAuthorizationService.getById(applicationId);

        String redisKey = this.getRedisKey(authorization);
        if (AuthorizationTypeEnum.JWT.equals(projectAuthorization.getType())) {
            try {
                JWTVerifier verifier =
                    JWT.require(this.getJWTSign(projectAuthorization.getAlg(), projectAuthorization.getJwtSalt()))
                        .build();

                DecodedJWT decodedJWT = verifier.verify(authorization);

                redisKey = this.getRedisKey(decodedJWT.getClaim("id").asString());
            } catch (Exception e) {
                throw new ThrowException("授权错误", 401);
            }
        }

        redisTemplate.delete(redisKey);
    }
}
