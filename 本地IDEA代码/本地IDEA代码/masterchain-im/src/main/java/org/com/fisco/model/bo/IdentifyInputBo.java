package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifyInputBo {

    private String uid;

    private String signInfo;

    public List<Object> toArgs() {
        List args = new ArrayList();
        args.add(uid);
        args.add(signInfo);
        return args;
    }
}
