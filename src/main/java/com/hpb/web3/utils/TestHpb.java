package com.hpb.web3.utils;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.Admin;
import com.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.web3.protocol.core.methods.response.HpbBlock;
import com.hpb.web3.protocol.http.HttpService;
import com.hpb.web3.protocol.ipc.UnixIpcService;
import com.hpb.web3.protocol.ipc.WindowsIpcService;

import rx.Observable;

public class TestHpb {

	public static void main(String[] args) {
		try {
			Web3Service web3Service=buildService("http://120.79.224.14:24100/");
			Admin admin = Admin.build(web3Service);
			
			Observable<HpbBlock> blockObservable = admin.catchUpToLatestBlockObservable(
					DefaultBlockParameter.valueOf(BigInteger.valueOf(0)), true);
			blockObservable.subscribe(hpbBlock ->{
				System.out.println(hpbBlock.getBlock());
			},Throwable::printStackTrace);
			/*PersonalListAccounts accounts = admin.personalListAccounts().sendAsync().get(15, TimeUnit.MINUTES);
			List<String> list = accounts.getAccountIds();*/
			//System.out.println(Arrays.toString(list.toArray()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static Web3Service buildService(String clientAddress) {
		Web3Service web3Service;
		if (clientAddress==null) {
			web3Service = new HttpService();
		} else if (clientAddress.startsWith("http")) {
			web3Service = new HttpService(clientAddress);
		} else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			web3Service = new WindowsIpcService(clientAddress);
		} else {
			web3Service = new UnixIpcService(clientAddress);
		}
		return web3Service;
	}
}
