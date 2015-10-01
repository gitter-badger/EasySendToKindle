package kaaass.es2k.mission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kaaass.es2k.Main;
import kaaass.es2k.Main.SendType;
import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.file.ZipCompressor;
import kaaass.es2k.mail.MailUtil;
import kaaass.es2k.mail.MailUtil.Result;

public class MailMission extends IMission {
	String[] file = new String[0];
	List<File> fileT = new ArrayList<File>();
	MailUtil mail = null;
	Result result = null;
	boolean hasMobi = false;

	MailMission() {

	}

	public MailMission(String par) {
		super();
		String[] s = new String[1];
		s[0] = par;
		this.file = s;
		Main.missionManager.todo(this.id);
	}

	public MailMission(String[] par) {
		super();
		this.file = par;
		Main.missionManager.todo(this.id);
	}

	@Override
	public SendType getInfo() {
		if (this.isAlive()) {
			if (result.isSuccess()) {
				return SendType.OK;
			} else {
				return SendType.ERROR;
			}
		}
		return SendType.SENDING;
	}

	@Override
	public void onStart() {
		// String tempPath = System.getProperty("java.io.tmpdir");
		for (int ii = 0; ii < file.length; ii++) {
			if (file[ii].toLowerCase().endsWith(".pdf")
					&& Main.des1.isSelected()) {
				switch (Main.comboF.getSelectedIndex()) {
				case 0:
					try {
						FileUtil.pdf2txt(file[ii]);
					} catch (Exception e) {
						e.printStackTrace();
						(new ErrorUtil(e)).dealWithException();
					}
					file[ii] = file[ii].substring(0, file[ii].length() - 4)
							+ ".txt";
					fileT.add(new File(file[ii]));
					break;
				}
			}
			if (file[ii].toLowerCase().endsWith(".mobi")) {
				FileUtil.makeDirs("mobi/");
				File a = new File(file[ii]);
				FileUtil.copyFileTo(a, new File("mobi/" + a.getName()));
				hasMobi = true;
			}
		}
		if (hasMobi) {
			try {
				(new ZipCompressor("mobi.zip")).compress("mobi/");
			} catch (RuntimeException e) {
				e.printStackTrace();
				(new ErrorUtil(e)).dealWithException();
			}
			fileT.add(new File("mobi.zip"));
			List<String> a = new ArrayList<String>();
			for (String s : file) {
				if (!s.toLowerCase().endsWith(".mobi")) {
					a.add(s);
				}
			}
			a.add((new File("mobi.zip")).getAbsolutePath());
			String[] b = new String[a.size()];
			for (int i = 0; i < a.size(); i++) {
				b[i] = a.get(i);
			}
			file = b;
		}
	}

	@Override
	public void onRun() {
		this.mail = new MailUtil(Main.isDebug);
		this.result = mail.send("Kindle�����ʼ�", "<p>���ʼ��ǳ����Զ����͵��ʼ�������ظ���лл��</p>",
				file);
		if (!result.isSuccess()) {
			boolean origin = Main.otherM;
			Main.missionFrame.redraw();
			for (int i = 0; i < 3; i++) {
				if (i == 2) {
					Main.otherM = true;
				}
				result = mail.send("Kindle�����ʼ�",
						"<p>���ʼ��ǳ����Զ����͵��ʼ�������ظ���лл��</p>", file);
				Main.missionFrame.redraw();
				if (result.isSuccess()) {
					break;
				}
			}
			Main.otherM = origin;
			(new ErrorUtil(result)).dealWithResult();
		}
	}

	@Override
	public void onEnd() {
		for (File f : fileT) {
			f.delete();
		}
		if (hasMobi) {
			FileUtil.deleteDirectory("mobi/");
		}
		if (!result.isSuccess()) {
			System.out.println("Send error!");
		} else {
			System.out.println("Send ok!");
		}
	}

	@Override
	public void onStop() {
	}

	@Override
	public String getDesc() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("\n�������ͣ���������");
		stringbuilder.append("\n�����ļ���");
		for (String s : file) {
			File f = new File(s);
			stringbuilder.append("\n  " + f.getName());
		}
		stringbuilder.append("\n�ļ�λ�ã�");
		stringbuilder.append("\n  " + file[0] + " ��");
		stringbuilder.append("\n����״̬��");
		stringbuilder.append("\n  " + getStates());
		return stringbuilder.toString();
	}

	@Override
	public Object getType() {
		return new MailMission();
	}

	public Result getResult() {
		return result;
	}

	@Override
	public String getTitle() {
		File f = new File(file[0]);
		return f.getName() + " ���ļ�";
	}

	@Override
	public String getStates() {
		if (this.result != null) {
			if (this.result.isSuccess()) {
				return "�����";
			} else {
				if (Main.missionManager.todoList.isEmpty()) {
					return "���ʹ���";
				} else {
					if (Main.missionManager.todoList.get(0) == this.id) {
						return "���ʹ���,��������";
					} else {
						return "���ʹ���";
					}
				}
			}
		} else if (Main.missionManager.todoList.get(0) == this.id) {
			return "������";
		} else {
			return "�Ŷ���";
		}
	}

	@Override
	public String getTypeName() {
		return "��������";
	}

	@Override
	public IMission restart() {
		return new MailMission(this.file);
	}
}
