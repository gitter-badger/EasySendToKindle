package kaaass.es2k.mission;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kaaass.es2k.Main;
import kaaass.es2k.crashreport.ErrorUtil;

public class MissionManager {
	public List<IMission> mList = new ArrayList<IMission>();
	public List<IMission> todoList = new ArrayList<IMission>();
	public int id = 0;
	int[] sendCounter = new int[2];
	public boolean running = false;
	
	public int add (IMission m) {
		m.id = id;
		if (mList.size() - 1 < id) {
			mList.add(id, m);
		} else {
			mList.set(id, m);
		}
		return id;
	}
	
	public void todo (int id) {
		todoList.add(mList.get(id));
		sendCounter[0]++;
	}
	
	public void runMission () {
		todoList.get(0).start();
		running = true;
		Main.des4.setText("ִ���У������" + (sendCounter[0] - 1) + "����ʣ��" 
				+ todoList.size() + "��");
	}
	
	public void endMission (int id) {
		todoList.remove(mList.get(id));
		if (mList.get(id) instanceof MailMission) {
			if (!((MailMission) mList.get(id)).getResult().isSuccess()) {
				sendCounter[1]++;
			}
		}
		if (todoList.isEmpty()) {
			JOptionPane.showMessageDialog(null, String.format("�������!���� %d �Σ�ʧ�� %d �Ρ�", sendCounter[0], sendCounter[1]));
			running = false;
			Main.des4.setText("��ϡ������" + sendCounter[0] + "����ʧ��" 
					+ sendCounter[1] + "��");
			sendCounter = new int[2];
		} else {
			this.runMission();
		}
	}
	
	public void pause () {
		try {
			todoList.get(0).wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
	}
	
	public void resume () {
		todoList.get(0).notify();
	}
}
