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
public class ManagementIdentifyInputBO {
  private String uid;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(uid);
    return args;
  }
}
