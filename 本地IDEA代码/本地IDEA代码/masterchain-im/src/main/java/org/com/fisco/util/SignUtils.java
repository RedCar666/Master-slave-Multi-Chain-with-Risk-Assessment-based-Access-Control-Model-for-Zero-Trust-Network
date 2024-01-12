package org.com.fisco.util;

import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.hash.Keccak256;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignature;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 15:46
 */
public class SignUtils {


    public static ECDSASignatureResult generateSignatureWithSecp256k1(CryptoKeyPair ecdsaKeyPair, String data) {
        // 生成secp256k1签名对象
        ECDSASignature signer = new ECDSASignature();
        // 计算data的哈希(keccak256)
        Keccak256 hasher = new Keccak256();
        String hashData = hasher.hash(data);
        return (ECDSASignatureResult) signer.sign(hashData, ecdsaKeyPair);
    }

    public static boolean verifySignature(String signInfo, String pkg, String data) {
        // 生成secp256k1验签对象
        ECDSASignature verifier = new ECDSASignature();
        // 计算data的哈希(keccak256)
        Keccak256 hasher = new Keccak256();
        String hashData = hasher.hash(data);
        // 验证签名
        return verifier.verify(pkg, hashData, signInfo);
    }

    public static Map<String, String> userAccountRegister() {
        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        Map<String, String> map = new HashMap<>();
        // 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        String hexPublicKey = cryptoKeyPair.getHexPublicKey();
        String hexPrivateKey = cryptoKeyPair.getHexPrivateKey();
        // 获取账户地址
        String accountAddress = cryptoKeyPair.getAddress();
        map.put("hexPublicKey", hexPublicKey);
        map.put("hexPrivateKey", hexPrivateKey);
        map.put("accountAddress", accountAddress);
        return map;
    }
}
