package org.com.fisco.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.fisco.constants.ContractConstants;
import org.com.fisco.model.bo.ResponseGetTransInputBO;
import org.com.fisco.model.bo.ResponseSetTransInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Auther ChenFei
 * @Date Create On 2022/1/23 16:06
 */
@Service
@NoArgsConstructor
@Data
public class ResponseService {
  @Value("${contract.responseAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse setTrans(ResponseSetTransInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ResponseAbi, "setTrans", input.toArgs());
  }

  public TransactionResponse getTrans(ResponseGetTransInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ResponseAbi, "getTrans", input.toArgs());
  }
}
