package org.com.fisco.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.StrUtil;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.WeIdServiceImpl;
import org.com.fisco.model.CommonResponse;
import org.com.fisco.model.bo.*;
import org.com.fisco.service.ManagementService;
import org.com.fisco.util.RSAUtils;
import org.com.fisco.util.SignUtils;
import org.com.fisco.util.Utils;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@RestController
@SuppressWarnings("ALL")
public class ManagementController {

    @Value("${skg}")
    private String skg;

    @Autowired
    private ManagementService managementService;
    private final static String uid_prefix = "did:uid:1:";
    private WeIdService weIdService = new WeIdServiceImpl();


    @PostMapping("/register")
    public CommonResponse register(@RequestBody RpcInputBo bo) throws Exception {
        //解密获取身份信息原文
        String encInfo = bo.getEncInfo();
        String str = RSAUtils.privateDecrypt(encInfo, RSAUtils.getPrivateKey(skg));
        //验证签名
        boolean flag = SignUtils.verifySignature(bo.getSignInfo(), bo.getPkg(), str);
        //签名验证成功
        if (flag) {
            //创建UID
            ResponseData<CreateWeIdDataResult> response = weIdService.createWeId();
            String weId = response.getResult().getWeId();
            List<String> weIds = StrSpliter.split(weId, ":", 0, true, true);
            String uid_suffix = weIds.get(3);
            String uid = uid_prefix + uid_suffix;
            List<String> strs = StrSpliter.split(str, ",", 0, true, true);
            //保存身份凭证
            ManagementInsertUserInputBO insertUserInputBo = new ManagementInsertUserInputBO();
            insertUserInputBo.setName(strs.get(0));
            insertUserInputBo.setAccount(strs.get(1));
            insertUserInputBo.setPkg(bo.getPkg());
            insertUserInputBo.setGroup(strs.get(2));
            insertUserInputBo.setDomain(strs.get(3));
            insertUserInputBo.setDomainPkg(strs.get(4));
            insertUserInputBo.setUid(uid);
            TransactionResponse transactionResponse = managementService.insertUser(insertUserInputBo);
            String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
            RpcOutputBo result = new RpcOutputBo();
            result.setUid(uid);
            result.setTransactionHash(transactionHash);
            //返回UID和交易哈希
            return new CommonResponse("202", "followchain1 register success!!!", result);
        }
        //将响应信息转发给对应从链
        return new CommonResponse("404", "followchain1 register false!!!", "verify false!!!");
    }

    @PostMapping("/identify")
    public CommonResponse identify(@RequestBody IdentifyInputBo bo) throws Exception {

        ManagementSelectUserInputBO managementSelectUserInputBO = new ManagementSelectUserInputBO();
        managementSelectUserInputBO.setUid(bo.getUid());
        ManagementIdentifyInputBO managementIdentifyInputBO = new ManagementIdentifyInputBO();
        managementIdentifyInputBO.setUid(bo.getUid());

        //根据UID查询身份凭证信息
        TransactionResponse transactionResponse = managementService.selectUser(managementSelectUserInputBO);
        String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
        String values = transactionResponse.getValues();
        //这里如果结果为null的话会返回[[]]
        if (values.length() > 4) {
            //处理交易结果
            String subValues = StrUtil.sub(values, 2, -2);
            List<String> splitValues = StrSpliter.split(subValues, ",", 0, true, true);
            String name = StrUtil.sub(splitValues.get(0), 1, -1);
            String account = StrUtil.sub(splitValues.get(1), 1, -1);
            String pkg = StrUtil.sub(splitValues.get(2), 1, -1);
            String group = StrUtil.sub(splitValues.get(3), 1, -1);
            String domain = StrUtil.sub(splitValues.get(4), 1, -1);
            String domainPkg = StrUtil.sub(splitValues.get(5), 1, -1);
            String data = StrUtil.join(",", name, account,
                    group, domain, domainPkg);
            //验证签名
            boolean flag = SignUtils.verifySignature(bo.getSignInfo(), pkg, data);
            if (flag) {
                RpcOutBo result = new RpcOutBo();
                result.setResultInfo("身份认证成功");
                result.setTransactionHash(transactionHash);
                return new CommonResponse("202", "followchain1 identify success!!!", result);
            } else {
                return new CommonResponse("404", "followchain1 identify false!!!", "verify false!!!");
            }
        }
        return new CommonResponse("404", "followchain1 identify false!!!", "transactionResponse is null!!!");
    }


    @PostMapping("/relate")
    public CommonResponse relate(@RequestBody RelateInputBo bo) throws Exception {

        ManagementSelectUserInputBO managementSelectUserInputBO = new ManagementSelectUserInputBO();
        managementSelectUserInputBO.setUid(bo.getUid());


        //根据UID查询身份凭证信息
        TransactionResponse transactionResponse = managementService.selectUser(managementSelectUserInputBO);
        String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
        String values = transactionResponse.getValues();
        //这里如果结果为null的话会返回[[]]
        if (values.length() > 4) {
            //处理交易结果
            String subValues = StrUtil.sub(values, 2, -2);
            List<String> splitValues = StrSpliter.split(subValues, ",", 0, true, true);
            String name = StrUtil.sub(splitValues.get(0), 1, -1);
            String account = StrUtil.sub(splitValues.get(1), 1, -1);
            String pkg = StrUtil.sub(splitValues.get(2), 1, -1);
            String group = StrUtil.sub(splitValues.get(3), 1, -1);
            String domain = StrUtil.sub(splitValues.get(4), 1, -1);
            String domainPkg = StrUtil.sub(splitValues.get(5), 1, -1);
            String data = StrUtil.join(",", name, account,
                    group, domain, domainPkg);
            //验证签名
            boolean flag = SignUtils.verifySignature(bo.getSignInfo(), pkg, data);
            if (flag) {
                ManagementUpdateUserInputBO managementUpdateUserInputBO = new ManagementUpdateUserInputBO();
                managementUpdateUserInputBO.setUid(bo.getUid());
                managementUpdateUserInputBO.setDomain(domain + "-" + bo.getDomain());
                managementUpdateUserInputBO.setDomainPkg(domainPkg + "-" + bo.getDomainPkg());
                //进行身份关联
                TransactionResponse transactionResponse1 = managementService.updateUser(managementUpdateUserInputBO);
                String transactionHash1 = transactionResponse1.getTransactionReceipt().getTransactionHash();
                RpcOutBo result = new RpcOutBo();
                result.setResultInfo("身份关联成功");
                result.setTransactionHash(transactionHash1);
                return new CommonResponse("202", "followchain1 relate success!!!", result);
            } else {
                return new CommonResponse("404", "followchain1 relate false!!!", "verify false!!!");
            }
        }
        return new CommonResponse("404", "followchain1 relate false!!!", "transactionResponse is null!!!");
    }

    @PostMapping("/cross/access")
    public CommonResponse crossAccess(@RequestBody CrossAccessInputBo bo) throws Exception {

        ManagementSelectUserInputBO managementSelectUserInputBO = new ManagementSelectUserInputBO();
        managementSelectUserInputBO.setUid(bo.getUid());
        //根据UID查询身份凭证信息
        TransactionResponse transactionResponse = managementService.selectUser(managementSelectUserInputBO);
        String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
        String values = transactionResponse.getValues();
        //这里如果结果为null的话会返回[[]]
        if (values.length() > 4) {
            //处理交易结果
            String subValues = StrUtil.sub(values, 2, -2);
            List<String> splitValues = StrSpliter.split(subValues, ",", 0, true, true);
            String name = StrUtil.sub(splitValues.get(0), 1, -1);
            String account = StrUtil.sub(splitValues.get(1), 1, -1);
            String pkg = StrUtil.sub(splitValues.get(2), 1, -1);
            String group = StrUtil.sub(splitValues.get(3), 1, -1);
            String domain = StrUtil.sub(splitValues.get(4), 1, -1);
            String domainPkg = StrUtil.sub(splitValues.get(5), 1, -1);
            String data = StrUtil.join(",", name, account,
                    group, domain, domainPkg);
            //验证签名
            boolean flag = SignUtils.verifySignature(bo.getSignInfo(), pkg, data);
            String return_pkg = null;
            if (flag) {
                List<String> domains = StrSpliter.split(domain, "-", 0, true, true);
                System.out.println("domains" + domains);
                List<String> domainPkgs = StrSpliter.split(domainPkg, "-", 0, true, true);
                System.out.println("domainPkgs" + domainPkgs);
                //大于1才说明已经关联过身份
                if (domains.size() > 1) {
                    int length = domains.size();
                    for (int i = 0; i < length; i++) {
                        //如果跨域身份存在
                        if (domains.get(i).equals(bo.getDomainName())) {
                            return_pkg = domainPkgs.get(i);
                        }
                    }

                }
                //进行身份关联
                RpcCrossAccessOutBo result = new RpcCrossAccessOutBo();
                result.setResultInfo(return_pkg);
                result.setTime(System.currentTimeMillis());
                result.setTransactionHash(transactionHash);
                return new CommonResponse("202", "followchain1 cross access success!!!", result);
            } else {
                return new CommonResponse("404", "followchain1 cross access false!!!", "cross access false!!!");
            }
        }
        return new CommonResponse("404", "followchain1 cross access false!!!", "transactionResponse is null!!!");
    }

}