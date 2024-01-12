package org.com.fisco.controller;


import cn.hutool.core.text.StrSpliter;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.WeIdServiceImpl;
import org.com.fisco.model.CommonResponse;
import org.com.fisco.model.bo.*;
import org.com.fisco.service.ManagementService;
import org.com.fisco.service.ResponseService;
import org.com.fisco.util.RSAUtils;
import org.com.fisco.util.SignUtils;
import org.com.fisco.util.Utils;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@RestController
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    private final static String uid_prefix = "did:uid:0:";

    private WeIdService weIdService = new WeIdServiceImpl();

    @Autowired
    private Environment env;

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private ResponseService responseService;

    //身份注册
    @PostMapping("/master/register")
    public CommonResponse register(@RequestBody RegisterInputBo bo) throws Exception {

        String message = Utils.convertToString(bo);
        //验证签名
        boolean flag = SignUtils.verifySignature(bo.getSignInfo(), bo.getPkg(), message);

        //签名验证成功
        if (flag) {
            //创建UID
            ResponseData<CreateWeIdDataResult> response = weIdService.createWeId();
            String weId = response.getResult().getWeId();
            List<String> weIds = StrSpliter.split(weId, ":", 0, true, true);
            String uid_suffix = weIds.get(3);
            String uid = uid_prefix + uid_suffix;
            //保存身份凭证
            ManagementInsertUserInputBO insertUserInputBo = new ManagementInsertUserInputBO();
            insertUserInputBo.setName(bo.getName());
            insertUserInputBo.setAccount(bo.getAccount());
            insertUserInputBo.setPkg(bo.getPkg());
            insertUserInputBo.setGroup(bo.getGroup());
            insertUserInputBo.setDomain(bo.getDomain());
            insertUserInputBo.setDomainPkg(bo.getDomainPkg());
            insertUserInputBo.setUid(uid);
            TransactionResponse transactionResponse = managementService.insertUser(insertUserInputBo);
            String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
            RpcOutputBo result = new RpcOutputBo();
            result.setUid(uid);
            result.setTransactionHash(transactionHash);
            return new CommonResponse("202", "masterchain register success!!!", result);
        }
        return new CommonResponse("404", "masterchain register false!!!", "verify false!!!");
    }

}
