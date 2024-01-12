package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInputBo {

    private String name;

    private String account;

    private String pkg;

    private String group;

    private String domain;

    private String domainPkg;

    private String signInfo;

    public List<Object> toArgs() {
        List args = new ArrayList();
        args.add(name);
        args.add(account);
        args.add(pkg);
        args.add(group);
        args.add(domain);
        args.add(domainPkg);
        args.add(signInfo);
        return args;
    }
}
