package cn.itcast.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;

public class FtpUtils {
	/**
	 * 通过ftp上传文件
	 * 
	 * @param url
	 *            ftp服务器地址 如： ftp://218.93.118.78
	 * @param port
	 *            端口如 ： 21
	 * @param username
	 *            登录名 lw
	 * @param password
	 *            密码 lw
	 * @param remotePath
	 *            上到ftp服务器的磁盘路径 E:\HB-ServU\lwfile
	 * @param fileNamePath
	 *            要上传的文件路径
	 * @param fileName
	 *            要上传的文件名
	 * @return
	 */

	public static String ftpUpload(String url, String port, String username,
			String password, String remotePath, String fileNamePath,
			String fileName) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
				ftpClient.makeDirectory(remotePath);
				// 设置上传目录
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileNamePath + fileName);
//				fis = context.openFileInput(fileNamePath + fileName);
				System.out.println(fileNamePath + fileName);
				System.out.println(fileName);
				ftpClient.storeFile(fileName, fis);
				returnMessage = "1"; // 上传成功
			} else {// 如果登录失败
				returnMessage = "0";
			}
			System.out.println("returnMessage--------------"+returnMessage);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
		return returnMessage;
	}
}
