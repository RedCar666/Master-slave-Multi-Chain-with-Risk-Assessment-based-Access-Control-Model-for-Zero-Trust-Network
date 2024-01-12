package org.com.fisco.controller;


import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.StrUtil;
import org.com.fisco.model.CommonResponse;
import org.com.fisco.model.bo.*;
import org.com.fisco.service.ResponseService;
import org.com.fisco.util.RSAUtils;
import org.com.fisco.util.Utils;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;


/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@RestController
@SuppressWarnings("All")
public class ResponseController {

    @Autowired
    private Environment env;

    private final static int F = 2;
    private final static String follow_chain = "http://localhost:";
    private final static Long T = 1000 * 60 * 5L;
    private int i;
    private int j = 0;

    @Autowired
    private ResponseService responseService;

    @Resource
    private RestTemplate restTemplate;

    //身份注册
    @PostMapping("/register")
    public CommonResponse register1(@RequestBody RegisterInputBo bo) throws Exception {



        //准备身份信息加密
        String str = Utils.convertToString(bo);
        //获取从链代理节点公钥
        String proxyNodePkg = env.getProperty("followchain_" + (j + 1));
        //加密
        String encInfo = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(proxyNodePkg));
        //将相应信息转发给对应从链
        RpcInputBo rpcInputBo = new RpcInputBo();
        rpcInputBo.setPkg(bo.getPkg());
        rpcInputBo.setSignInfo(bo.getSignInfo());
        rpcInputBo.setEncInfo(encInfo);
        CommonResponse commonResponse = restTemplate.postForObject(follow_chain + "808" + (j + 1) + "/register/", rpcInputBo, CommonResponse.class);
        // 从链注册成功
        if (commonResponse.getCode().equals("202")) {
            //计算下一次处理请求的从链ID
            i = j;
            j = (i + 1) % F;
            //将UID和从链交易哈希保存到主链
            Map<String, String> map = (Map) commonResponse.getData();
            ResponseSetTransInputBO responseSetTransInputBo = new ResponseSetTransInputBO();
            responseSetTransInputBo.setUid(map.get("uid"));
            responseSetTransInputBo.setTransactionHash(map.get("transactionHash"));
            responseSetTransInputBo.setObjectType("register");
            TransactionResponse transactionResponse = responseService.setTrans(responseSetTransInputBo);
            //将UID和主链交易哈希返回给用户
            String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
            RpcOutputBo result = new RpcOutputBo();
            result.setUid(map.get("uid"));
            result.setTransactionHash(transactionHash);
            return new CommonResponse("202", "register success!!!", result);
        }
        return new CommonResponse("404", "register false!!!", commonResponse.getMessage());
    }


    //身份认证
    @PostMapping("/identify")
    public CommonResponse identify(@RequestBody IdentifyInputBo bo) throws Exception {

        //获取从链对应从链ID
        String uid = bo.getUid();
        List<String> strs = StrSpliter.split(uid, ":", 0, true, true);
        String follow_chain_ID = strs.get(2);

        //转发到从链中
        CommonResponse commonResponse = restTemplate.postForObject(follow_chain + "808" + follow_chain_ID + "/identify/", bo, CommonResponse.class);

        //从链认证成功
        if (commonResponse.getCode().equals("202")) {
            Map<String, String> map = (Map) commonResponse.getData();
            ResponseSetTransInputBO responseSetTransInputBo = new ResponseSetTransInputBO();
            responseSetTransInputBo.setUid(map.get("resultInfo"));
            responseSetTransInputBo.setTransactionHash(map.get("transactionHash"));
            responseSetTransInputBo.setObjectType("identify");
            TransactionResponse transactionResponse = responseService.setTrans(responseSetTransInputBo);
            String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
            RpcOutBo result = new RpcOutBo();
            result.setResultInfo(map.get("resultInfo"));
            result.setTransactionHash(transactionHash);
            return new CommonResponse("202", "identify success!!!", result);
        }
        return new CommonResponse("404", "identify false!!!", commonResponse.getMessage());
    }

    //身份关联
    @PostMapping("/relate")
    public CommonResponse relate(@RequestBody RelateInputBo bo) throws Exception {

        //获取从链对应从链ID
        String uid = bo.getUid();
        List<String> strs = StrSpliter.split(uid, ":", 0, true, true);
        String follow_chain_ID = strs.get(2);

        //转发到从链中
        CommonResponse commonResponse = restTemplate.postForObject(follow_chain + "808" + follow_chain_ID + "/relate/", bo, CommonResponse.class);

        //从链关联成功
        if (commonResponse.getCode().equals("202")) {
            Map<String, String> map = (Map) commonResponse.getData();
            ResponseSetTransInputBO responseSetTransInputBo = new ResponseSetTransInputBO();
            responseSetTransInputBo.setUid(map.get("resultInfo"));
            responseSetTransInputBo.setTransactionHash(map.get("transactionHash"));
            responseSetTransInputBo.setObjectType("relate");
            TransactionResponse transactionResponse = responseService.setTrans(responseSetTransInputBo);
            String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
            RpcOutBo result = new RpcOutBo();
            result.setResultInfo(map.get("resultInfo"));
            result.setTransactionHash(transactionHash);
            return new CommonResponse("202", "relate success!!!", result);
        }
        return new CommonResponse("404", "relate false!!!", commonResponse.getMessage());
    }

    //跨域访问
    @PostMapping("/cross/access")
    public CommonResponse crossAccess(@RequestBody CrossAccessInputBo bo) throws Exception {
        //获取从链对应从链ID
        String uid = bo.getUid();
        List<String> strs = StrSpliter.split(uid, ":", 0, true, true);
        String follow_chain_ID = strs.get(2);

        //转发到从链中
        CommonResponse commonResponse = restTemplate.postForObject(follow_chain + "808" + follow_chain_ID + "/cross/access", bo, CommonResponse.class);

        //从链认证成功
        if (commonResponse.getCode().equals("202")) {
            //查询历史跨域访问记录
            ResponseGetTransInputBO inputBO = new ResponseGetTransInputBO();
            inputBO.setUid(uid);
            TransactionResponse trans = responseService.getTrans(inputBO);
            String values = trans.getValues();
            if (values.length() > 4) {
                //处理交易结果
                String subValues = StrUtil.sub(values, 1, -1);
                List<String> splitValues = StrSpliter.split(subValues, ",", 0, true, true);
                String objectType = StrUtil.sub(splitValues.get(0), 1, -1);
                String time = StrUtil.sub(splitValues.get(1), 1, -1);
                //判断是否是数字类型
                Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                boolean flag = pattern.matcher(time).matches();
                if (objectType.equals("cross access") && flag) {
                    Long T1 = Long.parseLong(time);
                    Long T2 = System.currentTimeMillis();
                    if (T2 - T1 <= T) {
                        Map<String, String> map = (Map) commonResponse.getData();
                        ResponseSetTransInputBO responseSetTransInputBo = new ResponseSetTransInputBO();
                        responseSetTransInputBo.setUid(bo.getUid());
                        responseSetTransInputBo.setTransactionHash(map.get("transactionHash"));
                        responseSetTransInputBo.setObjectType("cross access");
                        TransactionResponse transactionResponse = responseService.setTrans(responseSetTransInputBo);
                        String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
                        RpcOutBo result = new RpcOutBo();
                        result.setResultInfo(map.get("resultInfo"));
                        result.setTransactionHash(transactionHash);
                        return new CommonResponse("202", "cross access success!!!", result);
                    }
                }
                Map<String, String> map = (Map) commonResponse.getData();
                ResponseSetTransInputBO responseSetTransInputBo = new ResponseSetTransInputBO();
                responseSetTransInputBo.setUid(bo.getUid());
                responseSetTransInputBo.setTransactionHash(String.valueOf(map.get("time")));
                responseSetTransInputBo.setObjectType("cross access");
                TransactionResponse transactionResponse = responseService.setTrans(responseSetTransInputBo);
                String transactionHash = transactionResponse.getTransactionReceipt().getTransactionHash();
                RpcOutBo result = new RpcOutBo();
                result.setResultInfo(map.get("resultInfo"));
                result.setTransactionHash(transactionHash);
                return new CommonResponse("202", "cross access success!!!", result);
            }
        }
        return new CommonResponse("404", "cross access false!!!", commonResponse.getMessage());
    }
}
