package org.com.fisco.controller;

import org.com.fisco.model.CommonResponse;
import org.com.fisco.util.SignUtils;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther ChenFei
 * @Date Create On 2022/5/23 23:20
 */
@RestController
public class SignController {
    @GetMapping("/userAccountRegister")
    public CommonResponse userAccountRegister() throws Exception {
        Map<String, String> stringStringMap = SignUtils.userAccountRegister();
        String publicKey = stringStringMap.get("hexPublicKey");
        String privateKey = stringStringMap.get("hexPrivateKey");
        String accountAddress = stringStringMap.get("accountAddress");
        return new CommonResponse("202", "userAccountRegister success!!!", "publicKey：" + publicKey + "," + "privateKey：" + privateKey + "," + "accountAddress：" + accountAddress);
    }

    @GetMapping("/sign")
    public CommonResponse sign(String hexPrivateKey, String data) throws Exception {
        ECDSAKeyPair keyFacotry = new ECDSAKeyPair();
        // 从十六进制字符串加载hexPrivateKey
        CryptoKeyPair keyPair = keyFacotry.createKeyPair(hexPrivateKey);
        ECDSASignatureResult ecdsaSignatureResult = SignUtils.generateSignatureWithSecp256k1(keyPair, data);
        String result = ecdsaSignatureResult.convertToString();
        return new CommonResponse("202", "sign success!!!", result);
    }


    @GetMapping("/verify")
    public CommonResponse verify(String pkg, String signInfo, String data) throws Exception {
        boolean result = SignUtils.verifySignature(signInfo, pkg, data);
        return new CommonResponse("202", "verify success!!!", result);
    }
}
