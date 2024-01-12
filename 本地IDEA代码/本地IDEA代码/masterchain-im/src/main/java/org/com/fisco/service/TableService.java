package org.com.fisco.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.fisco.constants.ContractConstants;
import org.com.fisco.model.bo.TableInsertInputBO;
import org.com.fisco.model.bo.TableRemoveInputBO;
import org.com.fisco.model.bo.TableSelectInputBO;
import org.com.fisco.model.bo.TableUpdateInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
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
public class TableService {
  @Value("${contract.tableAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse select(TableSelectInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ContractConstants.TableAbi, "select", input.toArgs());
  }

  public CallResponse newCondition() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ContractConstants.TableAbi, "newCondition", Arrays.asList());
  }

  public TransactionResponse insert(TableInsertInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.TableAbi, "insert", input.toArgs());
  }

  public CallResponse newEntry() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ContractConstants.TableAbi, "newEntry", Arrays.asList());
  }

  public TransactionResponse remove(TableRemoveInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.TableAbi, "remove", input.toArgs());
  }

  public TransactionResponse update(TableUpdateInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.TableAbi, "update", input.toArgs());
  }
}
