package org.com.fisco.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.fisco.constants.ContractConstants;
import org.com.fisco.model.bo.ManagementIdentifyInputBO;
import org.com.fisco.model.bo.ManagementInsertUserInputBO;
import org.com.fisco.model.bo.ManagementSelectUserInputBO;
import org.com.fisco.model.bo.ManagementUpdateUserInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class ManagementService {
  @Value("${contract.managementAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse selectUser(ManagementSelectUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ManagementAbi, "selectUser", input.toArgs());
  }

  public TransactionResponse updateUser(ManagementUpdateUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ManagementAbi, "updateUser", input.toArgs());
  }

  public TransactionResponse identify(ManagementIdentifyInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ManagementAbi, "identify", input.toArgs());
  }

  public TransactionResponse insertUser(ManagementInsertUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.ManagementAbi, "insertUser", input.toArgs());
  }
}
