package com.pighand.aio.common.sdk.wechat;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

/**
 * 微信公众号
 *
 * @author wangshuli
 * @description <a
 * href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Overview.html">doc</a>
 */
@HttpExchange(value = "https://api.weixin.qq.com/", accept = "application/json", contentType = "application/json")
public interface MiniApplet {

    /**
     * 使用code获取access_token
     *
     * @param appid
     * @param secret
     * @param js_code
     * @return AccessToken
     * @see <a
     * href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">doc</a>
     */
    @GetExchange("sns/jscode2session")
    String code2Session(@RequestParam String appid, @RequestParam String secret, @RequestParam String js_code,
        @RequestParam(defaultValue = "authorization_code") String grant_type);

    @GetExchange("cgi-bin/token")
    String accessToken(@RequestParam String appid, @RequestParam String secret,
        @RequestParam(defaultValue = "client_credential") String grant_type);

    @PostExchange("wxa/business/getuserphonenumber")
    String getPhone(@RequestParam String access_token, @RequestBody Map<String, String> params,
        @RequestHeader Map<String, String> headers);

    /**
     * TODO： headers应该不用传，本地没问题，服务器报错，因为微信校验content-length。改用docker看是否取消此问题
     * 获取不限制的小程序码
     * 该接口用于获取小程序码，适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制。
     *
     * @param access_token 接口调用凭证
     * @param params       请求参数，包含以下字段：
     *                     - scene: 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     *                     - page: 页面路径，例如 pages/index/index，根路径前不要填加 /，不能携带参数
     *                     - check_path: 检查page是否存在，默认true
     *                     - env_version: 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"，默认正式版
     *                     - width: 二维码的宽度，单位px，最小280px，最大1280px，默认430
     *                     - auto_color: 自动配置线条颜色，默认false
     *                     - line_color: 线条颜色，格式为{"r":"xxx","g":"xxx","b":"xxx"}，auto_color为false时生效
     *                     - is_hyaline: 是否需要透明底色，默认false
     * @return 图片二进制内容
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html">获取不限制的小程序码</a>
     */
    @PostExchange("wxa/getwxacodeunlimit")
    byte[] getAppletQRCode(@RequestParam String access_token, @RequestBody Map<String, String> params,
        @RequestHeader Map<String, String> headers);

    /**
     * 发送模板消息
     * 本接口用于向用户发送模板消息
     *
     * @param access_token 接口调用凭证
     * @param params       请求参数，包含以下字段：
     *                     - touser: 接收者（用户）的 openid
     *                     - template_id: 所需下发的订阅模板id
     *                     - url: 模板跳转链接（可选）
     *                     - miniprogram: 跳转小程序时填写（可选）
     *                     - data: 模板内容，需根据模板给定的格式给出
     *                     - client_msg_id: 防重入id（可选）
     * @return 发送结果，包含msgid、errcode、errmsg
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/notify/notify/api_sendnewsubscribemsg.html">发送模板消息</a>
     */
    @PostExchange("cgi-bin/message/subscribe/send")
    String sendTemplateMessage(@RequestParam String access_token, @RequestBody Map<String, Object> params,
        @RequestHeader Map<String, String> headers);

}
