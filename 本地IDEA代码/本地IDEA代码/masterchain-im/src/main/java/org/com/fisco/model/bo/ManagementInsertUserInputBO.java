package org.com.fisco.model.bo;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagementInsertUserInputBO {
  private String uid;

  private String name;

  private String account;

  private String pkg;

  private String group;

  private String domain;

  private String domainPkg;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(uid);
    args.add(name);
    args.add(account);
    args.add(pkg);
    args.add(group);
    args.add(domain);
    args.add(domainPkg);
    return args;
  }
}
