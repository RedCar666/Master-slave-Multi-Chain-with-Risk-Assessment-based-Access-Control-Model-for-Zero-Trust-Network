package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/24 9:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcAccessOutBo {

    private double risk;

    private String resultInfo;

    private String transactionHash;

    public List<Object> toArgs() {
        List args = new ArrayList();
        args.add(risk);
        args.add(resultInfo);
        args.add(transactionHash);
        return args;
    }
}

