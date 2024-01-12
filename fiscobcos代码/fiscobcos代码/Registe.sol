pragma solidity>=0.4.24 <0.6.11;
import "./LibString.sol";

contract Registe {

uint final_block_id=1;
uint final_valid_time=24*60*60*90;

struct User{
	string userName;
	string iCard;
	string tel;
	string wid;
	string domain;
	string pkg;
	bool isUsed;
	uint validTime;
}
	event Register(string register_info);
	event Related(string related_info);
	event Identification(string identification_info);
 	mapping(string => bool) 	 iCard_to_register;
    	mapping(string => User)   wid_to_User;
    	mapping(string => string) domain_to_pkg;

    //查询该用户是否已经注册
    function getResult(string iCard) public view returns (bool) {
        return iCard_to_register[iCard];
    }


    //设置该用户已经注册过
    function setResult(string iCard) public {
             iCard_to_register[iCard]=true;
    }

	//获取用户公钥
    function get_pkg(string wid) returns(string){
    		User memory user=wid_to_User[wid];
    		return user.pkg;
    }

    //判断该用户凭证是否过期
    function is_expire(string wid) returns(bool){
    		User memory user=wid_to_User[wid];
    		if(now<user.validTime){
			//未过期
    		   return false;
    		}else{
    		//已经过期
    		return true;
    		}
    }

    //获取用户所属域
    function getDomain(string wid) returns(string){
    		User memory user=wid_to_User[wid];
    		return user.domain;
    }

	//根据域获取公钥
    function getPkg(string domain) returns(string){
    		return domain_to_pkg[domain];
    }

    //到期自动撤销身份凭证
    function passive_revoke(string wid) {
    		//到期自动撤销
    		bool flag=is_expire(wid);
    		User memory user=wid_to_User[wid];
    		if(flag){
    			user.isUsed=false;
    		}
    }

	//手动撤销身份凭证
    function initiative_revoke(string wid) {
    		//主动撤销
    		User memory user=wid_to_User[wid];
    		user.isUsed=false;
    		
    }

    //过期后激活身份凭证
        function active(string wid){
    		User memory user=wid_to_User[wid];
    		user.validTime=now+final_valid_time;
    		user.isUsed=true;
    }


    //判断用户所属域是否相同
	function is_domain_same(string domains,string domain) returns(bool){
		string[] memory split_domains= LibString.split(domains,",");
		uint length=split_domains.length;
		for(uint i=0;i<length;i++){
			bool flag=LibString.equalNocase(split_domains[i],domain);
			if(flag){
			//有相同域 不用重复注册
			return false;
			}
		}
		//进行身份关联
		return true;
	
	}

	//字符串拼接
	function string_concat(string str1,string str2) returns(string){
		string memory str3=LibString.concat(str1,",");
		return LibString.concat(str3,str2);
	
	
	}

	//用户注册
	function register(string userName,string iCard,string tel,string wid,string domain,string pkg) returns(string,uint) {
			bool flag=getResult(iCard);
			if(flag){
			emit Register("Registe false!This user is already registe!!!Please try identity related!");
			return (wid,final_block_id);
			}else{
			User memory user=User(userName,iCard,tel,wid,domain,pkg,true,now+final_valid_time);
			domain_to_pkg[domain]=pkg;
			wid_to_User[wid]=user;
			setResult(iCard);			
			emit Register("Registe success!");
			return (wid,final_block_id);
			}
}

	//身份认证
	function identification(string wid,uint block_id) returns(bool){
		if(final_block_id==block_id){
	 		if(wid_to_User[wid].isUsed){
	 		emit Register("Identify success!");
		 		return true;
		 }
		 	else{
		 	emit Register("Identify false!This user may not register or identity is expired!");
		 		return false;
		 }
	}
		else{
			emit Register("Identify false!This chain search this user false!");
			    return false;
		}
	}

	//身份关联
	function related(string domain,string wid,string pkg){
		User memory user=wid_to_User[wid];
		if(user.isUsed){
		//如果已注册用户所属域和现注册用户所属域相同 表明重复注册
		if(is_domain_same(user.domain,domain)==false){
			emit Related("Please don't repeat registe!!!");
		}
		//如果已注册用户所属域和现注册用户所属域不同，则进行身份关联
		else{
		user.domain=string_concat(user.domain,domain);
		user.pkg=string_concat(user.pkg,pkg);
		wid_to_User[wid]=user;	
		domain_to_pkg[domain]=pkg;
		emit Related("Identity relate success!!!");
		}
	}

}

}