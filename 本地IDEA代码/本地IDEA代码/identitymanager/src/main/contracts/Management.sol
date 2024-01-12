pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract Management {
    event insert_user(int256 count);
    event select_user(string result);
    event update_user(int256 count);
    event identify_user(string result);
    KVTableFactory tableFactory;
    string constant TABLE_NAME = "t_Management";

    constructor() public {
        //The fixed address is 0x1010 for KVTableFactory
        tableFactory = KVTableFactory(0x1010);
        tableFactory.createTable(TABLE_NAME, "uid", "name,account,pkg,group,domain,domainPkg");
    }

     //身份注册
    function insertUser(string uid, string name,string account,string pkg,string group,string domain,string domainPkg)
        returns (int256)
    {
        KVTable table = tableFactory.openTable(TABLE_NAME);
        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("name", name);
        entry.set("account", account);
        entry.set("pkg", pkg);
        entry.set("group", group);
        entry.set("domain", domain);
        entry.set("domainPkg", domainPkg);
        int256 count = table.set(uid, entry);
        emit insert_user(count);
        return count;
    }
    
    //身份查询
    function selectUser(string uid)  returns (string[]) {
        KVTable table = tableFactory.openTable(TABLE_NAME);
        bool flag = false;
        Entry entry;
        (flag, entry) = table.get(uid);
        string[] memory user_list=new string[](6);
        if (flag) {       
           user_list[0] = entry.getString("name");
           user_list[1] = entry.getString("account");
           user_list[2] = entry.getString("pkg");
           user_list[3] = entry.getString("group");
           user_list[4] = entry.getString("domain");
           user_list[5] = entry.getString("domainPkg");       
        }
        emit select_user(uid);  
        return user_list;
    }

    //身份认证
    function identify(string uid)  returns (bool) {
        KVTable table = tableFactory.openTable(TABLE_NAME);
        bool flag = false;
        Entry entry;
        (flag, entry) = table.get(uid);
        if (flag) {       
              emit identify_user("identify success!!!"); 
              return flag;
        }
        emit identify_user("identify false!!!");  
        return flag;
    }

    //身份关联
    function updateUser(string memory uid, string domain, string memory domainPkg)
    public
    returns (int256)
    {
        KVTable table = tableFactory.openTable(TABLE_NAME);

        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("domain", domain);
        entry.set("domainPkg", domainPkg);
        int256 count = table.set(uid, entry);
        emit update_user(count);
        return count;
    }
}