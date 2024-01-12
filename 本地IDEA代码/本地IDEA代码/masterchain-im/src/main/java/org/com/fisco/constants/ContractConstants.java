package org.com.fisco.constants;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
public class ContractConstants {
  public static String ResponseAbi;

  public static String ResponseBinary;

  public static String ResponseGmBinary;

  public static String TableAbi;

  public static String TableBinary;

  public static String TableGmBinary;

  public static String ManagementAbi;

  public static String ManagementBinary;

  public static String ManagementGmBinary;

  static {
    try {
      ResponseAbi = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("abi/Response.abi"));
      ResponseBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/Response.bin"));
      ResponseGmBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/Response.bin"));
      TableAbi = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("abi/Table.abi"));
      TableBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/Table.bin"));
      TableGmBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/Table.bin"));
      ManagementAbi = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("abi/Management.abi"));
      ManagementBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/Management.bin"));
      ManagementGmBinary = org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/Management.bin"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
