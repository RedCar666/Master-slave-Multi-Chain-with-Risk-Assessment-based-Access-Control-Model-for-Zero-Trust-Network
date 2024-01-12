package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther ChenFei
 * @Date Create On 2022/5/24 9:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcInputBo {


    private String pkg;

    private String signInfo;

    private String encInfo;

    public List<Object> toArgs() {
        List args = new ArrayList();
        args.add(pkg);
        args.add(signInfo);
        args.add(encInfo);
        return args;
    }
}

