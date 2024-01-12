package org.com.fisco.util;

import cn.hutool.core.util.StrUtil;
import org.com.fisco.model.bo.RegisterInputBo;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/24 9:16
 */
public class Utils {

    public static String convertToString(RegisterInputBo bo) {
        String conjunction = ",";
        String result = StrUtil.join(conjunction, bo.getName(), bo.getAccount(),
                bo.getGroup(), bo.getDomain(), bo.getDomainPkg());
        return result;
    }

}
