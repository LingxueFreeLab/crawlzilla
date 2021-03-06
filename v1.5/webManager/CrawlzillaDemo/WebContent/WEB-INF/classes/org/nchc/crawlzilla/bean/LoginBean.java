/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @guide
 * javabean for setting or getting password
 * @web
 * http://code.google.com/p/crawlzilla 
 * @author Waue, Shunfa, Rock {waue, shunfa, rock}@nchc.org.tw
 */
package org.nchc.crawlzilla.bean;/**

 * 
 * 
 * @web
 * <a href="http://code.google.com/p/crawlzilla/">http://code.google.com/p/crawlzilla </a>
 * 
 * @author Waue, Shunfa, Rock {waue, shunfa, rock}@nchc.org.tw
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginBean {
	public boolean getConfirm(String user, String passwd) throws IOException, NoSuchAlgorithmException {
		File UserExists = new File("/home/crawler/crawlzilla/user/" + user + "/meta/passwd");
		if (UserExists.exists()){
			System.out.println("user exists!");

			FileReader NP = new FileReader("/home/crawler/crawlzilla/user/" + user + "/meta/passwd");
			BufferedReader stdin = new BufferedReader(NP);
			String crawlerPasswd = new String(stdin.readLine());

			String md5PWStr = MD5(passwd);
			if (crawlerPasswd.equals(md5PWStr.toString()))
			{	
				NP.close();
				return true;
			}					
		} else	return false;
		
		return false;
	}
	
	public boolean checkFristLogin() throws IOException{
		File checkExists = new File("/home/crawler/crawlzilla/user/admin/meta/passwd");
		if (checkExists.exists()){
			FileReader NP = new FileReader("/home/crawler/crawlzilla/user/admin/meta/passwd");
			BufferedReader stdin = new BufferedReader(NP);
			String crawlerPasswd = new String(stdin.readLine());
			String md5PWStr = MD5("crawler");
			if (crawlerPasswd.equals(md5PWStr.toString()))
			{
				NP.close();
				return true;
			}		
		}
		return false;
	}
	
	public void changePW(String userName, String newPW) throws IOException, InterruptedException{
		String userPath = "/home/crawler/crawlzilla/user/" + userName + "/meta";		
		File passwdFile = new File(userPath + "/passwd");
		Thread.sleep(10);
		FileWriter fout = new FileWriter(passwdFile);
		BufferedWriter foutWriter = new BufferedWriter(fout);
		foutWriter.write(MD5(newPW));
		foutWriter.newLine();
		foutWriter.close();
	}
	
	public final static String MD5(String s) {
	    try {
	     byte[] btInput = s.getBytes();
	     MessageDigest mdInst = MessageDigest.getInstance("MD5");
	     mdInst.update(btInput);
	     byte[] md = mdInst.digest();
	     StringBuffer sb = new StringBuffer();
	     for (int i = 0; i < md.length; i++) {
	      int val = ((int) md[i]) & 0xff;
	      if (val < 16)
	       sb.append("0");
	      sb.append(Integer.toHexString(val));
	    
	     }
	     return sb.toString();
	    } catch (Exception e) {
	     return null;
	    }
	}
	
	public String getPortNO() throws IOException{
		String portNO = "";
		String cmd = "cat -n /opt/crawlzilla/tomcat/conf/server.xml | grep protocol=\\\"HTTP\\/1\\.1\\\" | awk 'BEGIN {FS=\"\\\"\"} {print $2}\'";
		String[] portCmd = { "/bin/bash", "-c" ,cmd};
		Process pl = Runtime.getRuntime().exec(portCmd);
		BufferedReader p_in = new BufferedReader(new InputStreamReader(pl.getInputStream()));
		portNO = p_in.readLine();
        p_in.close();
		System.out.println(cmd);
		return portNO;
	}
	
	public void editEmail(String userName, String newEmail) throws IOException{
		String filePath = "/home/crawler/crawlzilla/user/"+ userName +"/meta/email";
		File emailFile = new File(filePath);
		if(emailFile.exists()){
			FileWriter fout = new FileWriter(emailFile);
			BufferedWriter foutWriter = new BufferedWriter(fout);
			foutWriter.write(newEmail);
			foutWriter.newLine();
			foutWriter.close();
		}
	}
	
	public static void main(String args[]) throws NoSuchAlgorithmException, IOException{
		//LoginBean test = new LoginBean();
		//test.editEmail("shunfa", "shunfa@gmail.com");
	}
}