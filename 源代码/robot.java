package chess;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.*;

public class robot extends Panel implements ActionListener{
	char whoTurn = 'O';//���������O���ˡ���X
	Button b[] = new Button[9];//9����ť
	StringBuffer robot = new StringBuffer("KKKKKKKKK");
	//��3��3��������һά��������ʾ����K�ַ���ʾ��λ��
	
	public robot() {
		setLayout(new GridLayout(3,3,5,5));
		for(int i=0;i<9;i++) {
			b[i] = new Button("");
			add(b[i]);
			b[i].setActionCommand(String.valueOf(i));
			b[i].addActionListener(this);
		}
		computerTake();//���ڼ��������
	}
		
	
	
	public void actionPerformed(ActionEvent e) {
		Button me = (Button)(e.getSource());
		if(!me.getLabel().equals(""))//����������������λ������
			return;
		me.setLabel("" + whoTurn);//�������
		int row = Integer.parseInt(me.getActionCommand());//��λ��
		robot.setCharAt(row,whoTurn);//��¼�û�������Ϣ
		if(gameOver())//�ж���Ϸ�Ƿ����
		{
			for(int i=0;i<9;i++) {
				b[i].setLabel(String.valueOf(""));
				robot.setCharAt(i,'K');
			}
			whoTurn = 'O';
			computerTake();//���ڼ��������
		}
		else
		{
			whoTurn = (whoTurn == 'O')?'X':'O';//�ֻ����
			computerTake();//���ڼ��������
		}
	}
		
	public int findplace() {//��������ܲ�������λ��
		for(int r=0;r<9;r++)
			if(robot.charAt(r) == 'K') {//�Ҹ���λ��
				robot.setCharAt(r,whoTurn);//����������
				if(isWin(whoTurn)) {//���Լ��´�λ���Ƿ���Ӯ
					robot.setCharAt(r,'K');//�ָ�ԭ״
					return r;
				}
				else
					robot.setCharAt(r,'K');//�ָ�ԭ״
			}
		//û�Լ���ֱ��Ӯ��λ���ٿ��Է���Ӯ�ĵط�
		char whoTurn2 = (whoTurn == 'O')?'X':'O';//���ɶԷ�˼��
		for(int r=0;r<9;r++)//�����ڿհ״����϶Է�������
			if(robot.charAt(r)=='K') {
				robot.setCharAt(r,whoTurn2);
				if(isWin(whoTurn2)) {//���������Ӻ��Ƿ���Ӯ
					robot.setCharAt(r,'K');//�ָ�ԭ״
					return r;
				}
				else
					robot.setCharAt(r,'K');//�ָ�ԭ״
			}
		//���ÿ�����ѧ֪ʶ��������ۺ��������ü���С�������������Ž�
		//���ۺ���e = e1 - e2
		//e1��ʾ������п���ʹX������һ�ߵ���Ŀ
		//e2��ʾ������п���ʹO������һ�ߵ���Ŀ
		//max��ʾ�ϴ�ĵ���ֵ��R��ʾ���Ž��λ��
			int e,e1 = 0,e2 = 0,min,max = 0,R=0,r1=0;
			ArrayList list = new ArrayList();//���ڴ��3��3�����̵�һά����
			ArrayList list2 = new ArrayList();//���ڼ���e1
			ArrayList list3 = new ArrayList();//���ڼ���e2
			ArrayList<Integer> list4 = new ArrayList();//���ڴ��min��һά���飬��������max
			for(int r2=0;r2<9;r2++)
			{
				list.add(robot.charAt(r2));
			}
			for(int r3=0;r3<9;r3++)
				if(list.get(r3).equals('K'))
				{
					list.set(r3, 'X');
					for(int a=0;a<9;a++)
						if(list.get(a).equals('K'))
						{
							list.set(a, 'O');
							for(int b=0;b<9;b++)
								if(!list.get(b).equals('O'))
									list2.add(b);
							e1 = getf(list2);//�ú���getf()����e1
							list2.clear();//�������
							for(int c=0;c<9;c++)
								if(!list.get(c).equals('X'))
									list3.add(c);
							e2 = getf(list3);//�ú���getf()����e2
							list3.clear();//�������
							list.set(a, 'K');
							e = e1 - e2;
							list4.add(e);
						}
					min = Collections.min(list4);//��Collections���������list4�е���Сֵ
					list4.clear();//�������
					max = min;//��max��¼��һ�εõ�����Сֵ
					R = r3;//��R��¼��һ�εõ������Ž��λ��
					r1 = r3+1;
					list.set(r3, 'K');
					break;
				}
			while(r1<9)
			{
				if(list.get(r1).equals('K'))
				{
					list.set(r1, 'X');
					for(int c=0;c<9;c++)
						if(list.get(c).equals('K'))
						{
							list.set(c, 'O');
							for(int a=0;a<9;a++)
								if(!list.get(a).equals('O'))
									list2.add(a);
							e1 = getf(list2);
							list2.clear();
							for(int b=0;b<9;b++)
								if(!list.get(b).equals('X'))
									list3.add(b);
							e2 = getf(list3);
							list3.clear();
							list.set(c, 'K');
							e = e1 - e2;
							list4.add(e);
						}
					min = Collections.min(list4);
					list4.clear();
					if(max < min)//��ȡmaxֵ�Լ����Ž�λ��
					{
						max = min;
						R = r1;
					}
					list.set(r1, 'K');
				}
				r1++;
			}
			return R;
	}
	
	private int getf(ArrayList list2) {//����e1,e2
		int i = 0;//i������¼����һ�ߵ�����
		//��Collection�ӿڱ�������һ�ߵļ���
		Collection f1 = (Collection) Arrays.asList(0,1,2);
		Collection f2 = (Collection) Arrays.asList(3,4,5);
		Collection f3 = (Collection) Arrays.asList(6,7,8);
		Collection f4 = (Collection) Arrays.asList(0,3,6);
		Collection f5 = (Collection) Arrays.asList(1,4,7);
		Collection f6 = (Collection) Arrays.asList(2,5,8);
		Collection f7 = (Collection) Arrays.asList(0,4,8);
		Collection f8 = (Collection) Arrays.asList(2,4,6);
		if(list2.containsAll(f1))
			i++;
		if(list2.containsAll(f2))
			i++;
		if(list2.containsAll(f3))
			i++;
		if(list2.containsAll(f4))
			i++;
		if(list2.containsAll(f5))
			i++;
		if(list2.containsAll(f6))
			i++;
		if(list2.containsAll(f7))
			i++;
		if(list2.containsAll(f8))
			i++;
		return i;
	}

	public void computerTake() {
		int x = findplace();//���ݲ�����λ��
		robot.setCharAt(x, whoTurn);
		b[x].setLabel(String.valueOf(whoTurn));
		if(gameOver())//�ж���Ϸ�Ƿ����
		{
			for(int i=0;i<9;i++) {
				b[i].setLabel(String.valueOf(""));
				robot.setCharAt(i,'K');
			}
			whoTurn = 'O';
			computerTake();//���ڼ��������
		}
		else
			whoTurn = (whoTurn == 'O')?'X':'O';//�ֻ����
	}
	
	public boolean gameOver() {
		if(isWin(whoTurn)) {//�ж��Ƿ�ȡʤ
			JOptionPane.showMessageDialog(null, whoTurn+" win!");
//			System.exit(0);
			return true;
		}
		else if(isFull()) {//�ж��Ƿ���������
			JOptionPane.showMessageDialog(null, "game is over!");
//			System.exit(0);
			return true;
		}
		return false;
	}
	
	public boolean isWin(char who) {
		String s3 = "" +who + who + who;
		String sum;//����ƴ��һ����������ӱ�ʶ
		for(int k=0;k<3;k++) {
			sum = "" + robot.charAt(k*3+0) + robot.charAt(k*3+1)
						+ robot.charAt(k*3+2);
			if(sum.equals(s3)) return true;
		}
		sum = "" + robot.charAt(0) + robot.charAt(3) + robot.charAt(6);
		if(sum.equals(s3)) return true;
		sum = "" + robot.charAt(1) + robot.charAt(4) + robot.charAt(7);
		if(sum.equals(s3)) return true;
		sum = "" + robot.charAt(2) + robot.charAt(5) + robot.charAt(8);
		if(sum.equals(s3)) return true;
		sum = "" + robot.charAt(0) + robot.charAt(4) + robot.charAt(8);
		if(sum.equals(s3)) return true;
		sum = "" + robot.charAt(2) + robot.charAt(4) + robot.charAt(6);
		if(sum.equals(s3)) return true;
		return false;
	}
	
	public boolean isFull() {//�ж��Ƿ�����
		return robot.toString().indexOf("K") == -1;
	}
	
	public static void main(String args[]) {
		Point p = new Point(900,200);
		JFrame f = new JFrame("һ���塪��������");
		f.add(new robot());
		f.setSize(300,300);
		f.setLocation(p);
		f.setVisible(true);
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				f.dispose();
			}
		});
	}
}