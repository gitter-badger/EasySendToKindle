package kaaass.es2k.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import kaaass.es2k.crashreport.ErrorUtil;

public class FileUtil {
	public static String[] mErrorCode = new String[41];
	public static String[] mErrorInfo = new String[41];

	private File file;

	public FileUtil(File file) {
		this.file = file;
	}

	public Object[] readLine() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GBK"); // UTF-8
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		Object[] tem = null;
		String[] a = new String[FileUtil.mErrorCode.length];
		String[] b = new String[FileUtil.mErrorInfo.length];
		for (int i = 0; i < FileUtil.mErrorCode.length; i++) {
			if ((line = br.readLine()) != null) {
				tem = line.split("#");
				a[i] = (String) tem[0];
				b[i] = (String) tem[1];
			}
		}
		br.close();
		isr.close();
		fis.close();
		tem = new Object[2];
		tem[0] = a;
		tem[1] = b;
		return tem;
	}

	public static void loadFile() {
		File properties = new File("mail.properties");
		FileWriter fileWriter = null;
		File dic = new File("ErrorDictionary.dic");
		if (!properties.exists()) {
			try {
				properties.createNewFile();
				fileWriter = new FileWriter("mail.properties");
				String str;
				if ((str = JOptionPane.showInputDialog("����������Kindle��������:"
						+ "\nKindle����������������ġ�Kindle->����->�豸ѡ��->���Ի�����Kindle�����ҵ�")).equals("")) {
					str = "example@kindle.cn";
				}
				JOptionPane.showMessageDialog(null, "��������������䣺"
						+ "\n1.������ѷ����"
						+ "\n2.���ҵ��˻�->�����ҵ����ݺ��豸->����"
						+ "\n3.�ڡ����Ͽɵķ����˵��������б����µ���������Ͽɵĵ������䡱"
						+ "\n4.������������(Ĭ��Ϊ:es2kindle@163.com)"
						+ "\n5.(����Ϊ:es2kindle@sina.com)");
				fileWriter.write("#�����Ƿ�����������ã�Ĭ��Ϊ�����˺š�\n\n" + "#smtp������\n"
						+ "mail.smtp.host=smtp.163.com\n" + "#�Ƿ���Ҫ������֤����Ҫ�Ķ�\n"
						+ "mail.smtp.auth=true\n" + "#�����û���\n"
						+ "mail.sender.username=es2kindle@163.com\n"
						+ "#��������(��һ���ǵ�¼���� )\n"
						+ "mail.sender.password=vruzzyqprrwjyieh\n\n"
						+ "#Kindle��������\n"
						+ "es2k.mail.username=" + str);
			} catch (IOException e) {
				e.printStackTrace();
				(new ErrorUtil(e)).dealWithException();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
						(new ErrorUtil(e)).dealWithException();
					}
				}
			}
		}
		if (!dic.exists()) {
			JOptionPane.showMessageDialog(null,
					"�����ֵ�δ�ҵ�������Ŀ¼���Ƿ���ErrorDictionary.dic�ļ���", "����",
					JOptionPane.WARNING_MESSAGE);
		} else {
			FileUtil fu = new FileUtil(dic);
			try {
				Object[] tem = null;
				tem = fu.readLine();
				mErrorCode = (String[]) tem[0];
				mErrorInfo = (String[]) tem[1];
			} catch (IOException e) {
				e.printStackTrace();
				(new ErrorUtil(e)).dealWithException();
			}
		}
	}

	public static void pdf2txt(String file) throws Exception {
		boolean sort = false;
		String pdfFile = file;
		String textFile = null;
		String encoding = "UTF-8";
		int startPage = 1;
		int endPage = Integer.MAX_VALUE;
		Writer output = null;
		PDDocument document = null;
		try {
			try {
				URL url = new URL(pdfFile);
				document = PDDocument.load(pdfFile);
				String fileName = url.getFile();
				if (fileName.length() > 4) {
					File outputFile = new File(fileName.substring(0,
							fileName.length() - 4) + ".txt");
					textFile = outputFile.getName();
				}
			} catch (MalformedURLException e) {
				document = PDDocument.load(pdfFile);
				if (pdfFile.length() > 4) {
					textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
				}
			}
			output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
			PDFTextStripper stripper = null;
			stripper = new PDFTextStripper();
			stripper.setSortByPosition(sort);
			stripper.setStartPage(startPage);
			stripper.setEndPage(endPage);
			stripper.writeText(document, output);
		} finally {
			if (output != null) {
				output.close();
			}
			if (document != null) {
				document.close();
			}
		}
	}
}