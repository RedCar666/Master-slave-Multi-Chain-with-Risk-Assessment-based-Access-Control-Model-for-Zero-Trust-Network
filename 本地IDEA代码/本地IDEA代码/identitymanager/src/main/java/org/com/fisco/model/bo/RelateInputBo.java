package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelateInputBo {

    private String uid;

    private String signInfo;

    private String domain;

    private String domainPkg;

    public List<Object> toArgs() {
        List args = new ArrayList();
        args.add(uid);
        args.add(signInfo);
        args.add(domain);
        args.add(domainPkg);
        return args;
    }
}
