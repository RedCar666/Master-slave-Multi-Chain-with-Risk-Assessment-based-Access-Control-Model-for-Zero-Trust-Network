package org.com.fisco.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
