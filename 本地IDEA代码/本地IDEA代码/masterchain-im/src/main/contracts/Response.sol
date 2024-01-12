pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract Response {
    event set_trans(int256);
    event get_trans(string);
    KVTableFactory tableFactory;
    string constant TABLE_NAME = "t_Response";

    constructor() public {
        //The fixed address is 0x1010 for KVTableFactory
        tableFactory = KVTableFactory(0x1010);
        tableFactory.createTable(TABLE_NAME, "uid", "objectType,transactionHash");
    }

    function setTrans(string uid, string objectType,string transactionHash)
        returns (int256)
    {
        KVTable table = tableFactory.openTable(TABLE_NAME);
        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("objectType", objectType);
        entry.set("transactionHash", transactionHash);     
        int256 count = table.set(uid, entry);
        emit set_trans(count);
        return count;
    }

    
    function getTrans(string uid)  returns (string,string) {
        KVTable table = tableFactory.openTable(TABLE_NAME);
        bool flag = false;
        Entry entry;
        (flag, entry) = table.get(uid);      
        if (flag) {       
          string memory transactionHash=entry.getString("transactionHash");
          string memory objectType=entry.getString("objectType");
        }
        emit get_trans(uid);  
        return (objectType,transactionHash);
    }

}