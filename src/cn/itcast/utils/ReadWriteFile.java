package cn.itcast.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ReadWriteFile {
	public static BufferedReader bufread;
	// 指定文件路径和名称

	private static String readStr = "";

	/**
	 * 创建文本文件.
	 * 
	 * @throws IOException
	 * 
	 */
	public static void creatTxtFile(File filename) throws IOException {
		if (!filename.exists()) {
			filename.createNewFile();
			System.err.println(filename + "已创建！");
		}
		clearTxtFile(filename);
	}

	public static void clearTxtFile(File filename) {
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件.
	 * 
	 */
	public static String readTxtFile(File filename) {
		String read;
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					readStr = readStr + read + "\r\n";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("文件内容是:" + "\r\n" + readStr);
		return readStr;
	}

	/**
	 * 写文件.
	 * 
	 */
	public static void writeTxtFile(String newStr,File filename) throws IOException {
		// 先读取原有文件内容，然后进行写入操作
//		String filein = newStr + "\r\n" + readStr + "\r\n";
//		RandomAccessFile mm = null;
//		try {
//			mm = new RandomAccessFile(filename, "rw");
//			mm.writeBytes(newStr);
//		} catch (IOException e1) {
//			// TODO 自动生成 catch 块
//			e1.printStackTrace();
//		} finally {
//			if (mm != null) {
//				try {
//					mm.close();
//				} catch (IOException e2) {
//					// TODO 自动生成 catch 块
//					e2.printStackTrace();
//				}
//			}
//		}
		try   
	    {      
//	        File f = new File(filename);      
	        if (!filename.exists())   
	        {       
	        	filename.createNewFile();      
	        }      
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filename),"gbk");      
	        BufferedWriter writer=new BufferedWriter(write);          
	        writer.write(newStr);      
	        writer.close();     
	    } catch (Exception e)   
	    {      
	        e.printStackTrace();     
	    }  
	}

	/**
	 * 将文件中指定内容的第一行替换为其它内容.
	 * 
	 * @param oldStr
	 *            查找内容
	 * @param replaceStr
	 *            替换内容
	 */
	public static void replaceTxtByStr(String oldStr, String replaceStr,File filename) {
		String temp = "";
		try {
			File file = filename;
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			// 保存该行前面的内容
			for (int j = 1; (temp = br.readLine()) != null
					&& !temp.equals(oldStr); j++) {
				buf = buf.append(temp);
				buf = buf.append(System.getProperty("line.separator"));
			}

			// 将内容插入
			buf = buf.append(replaceStr);

			// 保存该行后面的内容
			while ((temp = br.readLine()) != null) {
				buf = buf.append(System.getProperty("line.separator"));
				buf = buf.append(temp);
			}

			br.close();
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
