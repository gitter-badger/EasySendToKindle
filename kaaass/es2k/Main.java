package kaaass.es2k;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumnModel;

import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.mail.MailUtil;
import kaaass.es2k.mail.MailUtil.Result;
import kaaass.es2k.mission.MailMission;
import kaaass.es2k.mission.MissionManager;

public class Main extends JFrame {
	private static final long serialVersionUID = 5727395420329762298L;
	
	public static MissionManager missionManager = new MissionManager();
	
	public static boolean otherM = false;
	public static Vector<String> cN = new Vector<String>();
	public static Vector<Vector<String>> data = new Vector<Vector<String>>();
	public static String lastDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
	
	public static JLabel des0;
	public static JScrollPane sp;
	public static JTable table;
	public static TableColumnModel tableCM;
	public static JButton cBtn;
	public static JButton fBtn;
	public static JCheckBox des1;
	public static JCheckBox des2;
	public static JComboBox<?> comboF;
	public static JLabel des3;
	public static JLabel des4;
	public static JButton mBtn;
	public static JButton sBtn;

	public JMenuBar mb;
	public JPopupMenu popup;
	public JMenu fileMenu;
	public JMenu helpMenu;
	public JMenu aboutMenu;
	public JMenuItem fmSend;
	public JMenuItem fmOption;
	public JMenuItem fmClose;
	public JMenuItem fmDebug;
	public JMenuItem fmHelp;
	public JMenuItem fmAbout;
	public JMenuItem fmSend2;
	public JMenuItem fmDel;

	public Main() {
		cN.add("�ļ���");
		cN.add("·��");
		cN.add("��С");
		cN.add("״̬");
		des0 = new JLabel("�����ļ��б�:");
		table = new JTable(data, cN){
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
		table.setRowSelectionAllowed(true);
		sp = new JScrollPane(table);
		cBtn = new JButton("ѡ���ļ�");
		fBtn = new JButton("ѡ���ļ���");
		des1 = new JCheckBox("��pdf�ļ�תΪ");
		des2 = new JCheckBox("ȫѡ");
		String[] format = { ".txt", ".mobi" };
		comboF = new JComboBox<Object>(format);
		comboF.enableInputMethods(false);
		des3 = new JLabel("���ͽ���:");
		des4 = new JLabel("���");
		mBtn = new JButton("�������");
		sBtn = new JButton("����ȫ��");
		menuInit();
		btnLis();
		this.setLayout(null);
		this.add(des0);
		this.add(sp);
		this.add(cBtn);
		this.add(fBtn);
		this.add(des1);
		this.add(des2);
		this.add(comboF);
		this.add(des3);
		this.add(des4);
		this.add(mBtn);
		this.add(sBtn);
	}
	
	private static void boundsInit() {
		des0.setBounds(2, 0, 90, 20);
		sp.setBounds(2, 20, 432, 273);
		cBtn.setBounds(228, 295, 102, 20);
		fBtn.setBounds(332, 295, 102, 20);
		des1.setBounds(53, 294, 108, 20);
		des2.setBounds(2, 294, 53, 20);
		comboF.setBounds(162, 296, 60, 18);
		des3.setBounds(2, 316, 60, 20);
		des4.setBounds(62, 316, 188, 20);
		mBtn.setBounds(252, 316, 90, 20);
		sBtn.setBounds(344, 316, 90, 20);
	}

	private void menuInit() {
		mb = new JMenuBar();
		fileMenu = new JMenu("�ļ�");
		helpMenu = new JMenu("����");
		aboutMenu = new JMenu("����");
		popup = new JPopupMenu();
		fmSend = new JMenuItem("����");
		fmOption = new JMenuItem("ѡ��");
		fmClose = new JMenuItem("�ر�");
		fmDebug = new JMenuItem("��������");
		fmHelp = new JMenuItem("����");
		fmAbout = new JMenuItem("����");
		fmSend2 = new JMenuItem("����");
		fmDel = new JMenuItem("ɾ���ļ�");
		menuListener();
		fileMenu.add(fmSend);
		fileMenu.add(fmOption);
		fileMenu.addSeparator();
		fileMenu.add(fmClose);
		helpMenu.add(fmDebug);
		helpMenu.addSeparator();
		helpMenu.add(fmHelp);
		aboutMenu.add(fmAbout);
		popup.add(fmSend2);
		popup.addSeparator();
		popup.add(fmDel);
		mb.add(fileMenu);
		mb.add(helpMenu);
		mb.add(aboutMenu);
		this.setJMenuBar(mb);
	}
	
	private void menuListener() {
		fmSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				int[] tem = new int[data.size()];
				for (int i = 0; i < tem.length; i++) {
					tem[i] = i;
				}
				if (tem.length <= 0) {
					JOptionPane.showMessageDialog(null, "�б�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				send(tem);
			}
		});
		fmOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				otherM = JOptionPane.showConfirmDialog(null,
						"�Ƿ�ʹ�ñ���Դ���ͣ�(��������ָ������Ƽ�ʹ�ã���������Դ)", "ѡ��",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}
		});
		fmClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		fmDebug.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				MailUtil mail = new MailUtil(false);
				if (!(new File("CrashReport/").isDirectory())) {
					JOptionPane.showMessageDialog(null, "δ��ѯ�������¼��", "����",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String[] a = new String[3];
				while (true) {
					a[0] = JOptionPane.showInputDialog("���������ĳƺ���");
					if (a[0] == null) {
						return;
					} else if (!a[0].equals("")) {
						break;
					}
				}
				while (true) {
					a[1] = JOptionPane.showInputDialog("���������������ַ��");
					if (a[1] == null) {
						return;
					} else if (!a[1].equals("")) {
						break;
					}
				}
				while (true) {
					a[2] = JOptionPane.showInputDialog("����������������");
					if (a[2] == null) {
						return;
					} else if (!a[2].equals("")) {
						break;
					}
				}
				if (JOptionPane.showConfirmDialog(null,
						"���ȷ��������������Ҫ�ȴ�һ��ʱ�䡣����رճ����ֹ����ʧ�ܡ�", "��ʾ",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					Result r = mail.sendDebugInfo(a[0], a[1], a[2]);
					if (!r.isSuccess()) {
						(new ErrorUtil(r)).dealWithResult();
					} else {
						JOptionPane.showMessageDialog(null, "�����ɹ��������߽��ᾡ�����ȡ����ϵ��");
					}
				}
			}
		});
		fmHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(null, "���ʹ�ã�"
						+ "\nע�����Ƚ�����������������Ϊ�Ͽ����䣡��������һ����"
						+ "\n1.���ѡ��ťѡ�����ĵ�����"
						+ "\n2.ѡ�����ȷ����Ŀ��ʾ���б���"
						+ "\n3.������Ͱ�ť���鼮���Զ����͵�����Kindle��");
				JOptionPane.showMessageDialog(null, "��������������䣺"
						+ "\n1.������ѷ����"
						+ "\n2.���ҵ��˻�->�����ҵ����ݺ��豸->����"
						+ "\n3.�ڡ����Ͽɵķ����˵��������б��µ��������Ͽɵĵ������䡱"
						+ "\n4.������������(Ĭ��Ϊ:es2kindle@163.com)"
						+ "\n5.(����Ϊ:es2kindle@sina.com)");
				JOptionPane.showMessageDialog(null, "��ν������"
						+ "\n1.���ı��ĵ���ͬĿ¼�µ�mail.properties�ļ�"
						+ "\n2.������ʾ�޸���Ŀ"
						+ "\n3.Kindle����������������ġ�Kindle->����->�豸ѡ��->���Ի�����Kindle�����ҵ�"
						+ "\n4.�������ܽ������ͨ���˵�->����->����������֪ͨ�������Խ����");
			}
		});
		fmAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(null, "����"
						+ "\n������:Easy Send To Kindle"
						+ "\n����:KAAAsS"
						+ "\n�汾:1.0.2.1509_Alpha"
						+ "\n������Java 1.8"
						+ "\n��л���ס���������ķ���֧�֡�", "����", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		fmSend2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				send(table.getSelectedRows());
			}
		});
		fmDel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				int[] tem = table.getSelectedRows();
				List<Vector<?>> remove = new ArrayList<Vector<?>>();
				if (tem.length > 0) {
					for (int i = 0; i < tem.length; i++) {
						remove.add(data.get(tem[i]));
					}
					for (Vector<?> v: remove) {
						data.remove(v);
					}
					table.clearSelection();
					table.updateUI();
				}
			}
		});
		MouseInputListener mil = new MouseInputListener(){
			@Override
			public void mouseReleased(MouseEvent e) {
				processEvent(e);
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 && !e.isControlDown() && !e.isShiftDown()) {
					popup.show(table, e.getX(), e.getY());
                } 
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//processEvent(e);
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 && !e.isControlDown() && !e.isShiftDown()) {
					int row = (int) Math.floor(e.getY() / table.getRowHeight());
					int[] tem = table.getSelectedRows();
					for (int i: tem) {
						if (i == row) {
							return;
						}
					}
					table.changeSelection(row, 0, false, false);
				} else if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
					des2.setSelected(false);
				}
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}
			
		};
		table.addMouseListener(mil);
		table.addMouseMotionListener(mil);
	}
	
	private void btnLis() {
		cBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JFileChooser c = new JFileChooser();
				FileFilter ff = new FileFilter(){
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						return f.getName().endsWith(".doc") ||
								f.getName().endsWith(".docx") ||
								f.getName().endsWith(".rtf") ||
								f.getName().endsWith(".txt") ||
								f.getName().endsWith(".mobi") ||
								f.getName().endsWith(".pdf");
					} 
					public String getDescription(){
						return "Kindle֧���ļ�";
					}
				};
				c.setFileFilter(ff);
				c.setMultiSelectionEnabled(false);
				c.setCurrentDirectory(new File(lastDir));
				c.setDialogTitle("��ѡ�������ļ�...");
				c.setApproveButtonText("�����б�");
				c.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File f = c.getSelectedFile();
					lastDir = f.getAbsolutePath().substring(0, f.getAbsolutePath()
							.length() - f.getName().length());
					if (ff.accept(f)) {
						if (f.length() > 31457280) {
							JOptionPane.showMessageDialog(null, "���ļ���С���ܴ���30MB��", "����",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						addListItem(f, SendType.READY);
					} else {
						JOptionPane.showMessageDialog(null, "��ѡ��֧�ֵ��ļ���(*.doc��"
								+ "*.docx��*.rtf��*.txt��*.mobi��*.pdf)", "����",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		fBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setMultiSelectionEnabled(false);
				c.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				c.setDialogTitle("��ѡ�������ļ����ڵ��ļ���...");
				c.setApproveButtonText("��������");
				c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					addDic(c.getSelectedFile());
				}
			}
		});
		sBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				int[] tem = new int[data.size()];
				for (int i = 0; i < tem.length; i++) {
					tem[i] = i;
				}
				if (tem.length <= 0) {
					JOptionPane.showMessageDialog(null, "�б�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				send(tem);
			}
		});
		comboF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (comboF.getSelectedIndex() == 1) {
					JOptionPane.showMessageDialog(null, "ת*.mobi��������ԭ�򲻶��⿪�š�", "��ʾ",
							JOptionPane.WARNING_MESSAGE);
					comboF.setSelectedIndex(0);
				}
			}
		});
		des2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (des2.isSelected()) {
					table.selectAll();
				} else {
					table.clearSelection();
				}
			}
		});
		new DropTarget (this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void drop (DropTargetDropEvent dtde) {
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						List<File> list = (List<File>)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
						for(File file: list) {
							if (file.getName().endsWith(".doc") ||
									file.getName().endsWith(".docx") ||
									file.getName().endsWith(".rtf") ||
									file.getName().endsWith(".txt") ||
									file.getName().endsWith(".mobi") ||
									file.getName().endsWith(".pdf")) {
								if (file.length() > 31457280) {
									continue;
								}
								addListItem(file, SendType.READY);
							} else if (file.isDirectory()) {
								addDic(file);
							}
						}
						dtde.dropComplete(true);
					} else {
                    dtde.rejectDrop();
					}
				} catch (Exception e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				}
			}
		});
	}
	
	public static void addDic (File f) {
		if (f.isDirectory()) {
			File[] tem = f.listFiles();
			if (tem == null) {
				return;
			}
			for (int i = 0; i < tem.length; i++) {
				if (tem[i].getName().endsWith(".doc") ||
						tem[i].getName().endsWith(".docx") ||
						tem[i].getName().endsWith(".rtf") ||
						tem[i].getName().endsWith(".txt") ||
						tem[i].getName().endsWith(".mobi") ||
						tem[i].getName().endsWith(".pdf")) {
					if (tem[i].length() > 31457280) {
						break;
					}
					addListItem(tem[i], SendType.READY);
				} else if (tem[i].isDirectory()) {
					addDic(tem[i]);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		boundsInit();
		m.setSize(442, 392);
		m.setLocationRelativeTo(null);
		m.setVisible(true);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.setTitle("Easy Send To Kindle");
		m.setResizable(false);
		FileUtil.loadFile();
	}
	
	public static void addListItem(File file, SendType type) {
		Vector<String> v = new Vector<String>();
		v.add(file.getName());
		v.add(file.getAbsolutePath());
		v.add(bytes2kb(file.length()));
		v.add(type.toString());
		if (data.indexOf(v) < 0) {
			data.add(v);
			table.updateUI();
		}
	}
	
	public void send (int[] item) {
		try {
			float total = 0F;
			int last = 0;
			List<Vector<?>> remove = new ArrayList<Vector<?>>();
			if (item.length <= 0) {
				JOptionPane.showMessageDialog(null, "��ѡ������һ�", "����",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			for (int i = 0; i < item.length; i++) {
				total += kb2mb(data.get(item[i]).get(2));
				if (total >= 30F) {
					total = 0F;
					String[] file = new String[i - last];
					remove.clear();
					for (int ii = 0; ii < file.length; ii++) {
						file[ii] = data.get(ii + last).get(1);
						remove.add(data.get(ii + last));
						table.updateUI();
					}
					last = i;
					for (Vector<?> v: remove) {
						data.remove(v);
					}
					table.updateUI();
					new MailMission(file);
				}
			}
			String[] file = new String[item.length - last];
			remove.clear();
			for (int ii = 0; ii < file.length; ii++) {
				file[ii] = data.get(ii + last).get(1);
				remove.add(data.get(ii + last));
				table.updateUI();
			}
			for (Vector<?> v: remove) {
				data.remove(v);
			}
			table.updateUI();
			new MailMission(file);
			missionManager.runMission();
		} catch (Exception e) {
			e.printStackTrace();
			(new ErrorUtil(e)).dealWithException();
		}
	}
	
	public void clear () {
		int i = 0;
		while(true){
			if (i > data.size() - 1) {
				break;
			}
			if (data.get(i).get(3).equals(SendType.OK.toString())) {
				data.remove(i);
				i = 0;
			}
			i++;
		}
		table.updateUI();
	}
	
	public static String bytes2kb(long bytes) {  
        BigDecimal filesize = new BigDecimal(bytes);  
        BigDecimal megabyte = new BigDecimal(1024 * 1024);  
        float returnValue = filesize.divide(megabyte, 4, BigDecimal.ROUND_UP)  
                .floatValue();  
        if (returnValue > 1)  
            return (returnValue + " MB");  
        BigDecimal kilobyte = new BigDecimal(1024);  
        returnValue = filesize.divide(kilobyte, 4, BigDecimal.ROUND_UP)  
                .floatValue();  
        return (returnValue + " KB");  
    }
	
	public static float kb2mb(String kb) {  
		if (kb.endsWith(" KB")) {
			float f = Float.valueOf(kb.substring(0, kb.length() - 3));
			BigDecimal filesize = new BigDecimal(f);  
			BigDecimal megabyte = new BigDecimal(1024);  
			float returnValue = filesize.divide(megabyte, 7, BigDecimal.ROUND_UP)  
					.floatValue();   
			return (returnValue); 
		}
		return Float.valueOf(kb.substring(0, kb.length() - 3));
    }
	
	public enum SendType {
		READY(0), SENDING(1), OK(2), ERROR(3);
		
		private int index;
		
		SendType (int index) {
			this.index = index;
		}
		
		public String toString () {
			switch(this.index){
			case 0:
				return "׼�����";
			case 1:
				return "������";
			case 2:
				return "���ͳɹ�";
			case 3:
				return "����ʧ��";
			}
			return "";
		}
		
		public static SendType toEnum(String str) {
			switch(str){
			case "׼�����":
				return SendType.READY;
			case "������":
				return SendType.SENDING;
			case "���ͳɹ�":
				return SendType.OK;
			case "����ʧ��":
				return SendType.ERROR;
			}
			return null;
		}
	}
}
