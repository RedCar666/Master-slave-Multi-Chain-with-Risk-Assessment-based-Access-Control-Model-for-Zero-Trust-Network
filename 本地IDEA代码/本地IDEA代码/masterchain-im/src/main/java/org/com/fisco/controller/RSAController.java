package org.com.fisco.controller;

import org.com.fisco.model.CommonResponse;
import org.com.fisco.util.RSAUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:24
 */
@RestController
public class RSAController {


    @GetMapping("/generate")
    public CommonResponse generate() throws Exception {
        Map<String, String> keys = RSAUtils.createKeys(512);
        String publicKey = keys.get("publicKey");
        String privateKey = keys.get("privateKey");
        return new CommonResponse("202", "generate success!!!", "publicKey：" + publicKey + "," + "privateKey：" + privateKey);
    }

    @GetMapping("/encrypt")
    public CommonResponse encrypt(String pkg, String message) throws Exception {
        String encInfo = RSAUtils.publicEncrypt(message, RSAUtils.getPublicKey(pkg));
        return new CommonResponse("202", "encrypt success!!!", "encInfo：" + encInfo);
    }

    @GetMapping("/decrypt")
    public CommonResponse decrypt(String skg, String encInfo) throws Exception {
        String message = RSAUtils.privateDecrypt(encInfo, RSAUtils.getPrivateKey(skg));
        return new CommonResponse("202", "decrypt success!!!", "message：" + message);
    }
}
