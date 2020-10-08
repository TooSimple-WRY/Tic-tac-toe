package chess;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.*;

public class robot extends Panel implements ActionListener{
	char whoTurn = 'O';//计算机——O，人——X
	Button b[] = new Button[9];//9个按钮
	StringBuffer robot = new StringBuffer("KKKKKKKKK");
	//将3行3列棋盘用一维数组来表示，用K字符表示空位置
	
	public robot() {
		setLayout(new GridLayout(3,3,5,5));
		for(int i=0;i<9;i++) {
			b[i] = new Button("");
			add(b[i]);
			b[i].setActionCommand(String.valueOf(i));
			b[i].addActionListener(this);
		}
		computerTake();//现在计算机下棋
	}
		
	
	
	public void actionPerformed(ActionEvent e) {
		Button me = (Button)(e.getSource());
		if(!me.getLabel().equals(""))//不允许在已有棋子位置下棋
			return;
		me.setLabel("" + whoTurn);//标记下棋
		int row = Integer.parseInt(me.getActionCommand());//求位置
		robot.setCharAt(row,whoTurn);//记录用户下棋信息
		if(gameOver())//判断游戏是否结束
		{
			for(int i=0;i<9;i++) {
				b[i].setLabel(String.valueOf(""));
				robot.setCharAt(i,'K');
			}
			whoTurn = 'O';
			computerTake();//现在计算机下棋
		}
		else
		{
			whoTurn = (whoTurn == 'O')?'X':'O';//轮换玩家
			computerTake();//现在计算机下棋
		}
	}
		
	public int findplace() {//计算机智能查找下棋位置
		for(int r=0;r<9;r++)
			if(robot.charAt(r) == 'K') {//找个空位置
				robot.setCharAt(r,whoTurn);//先填上棋子
				if(isWin(whoTurn)) {//看自己下此位置是否能赢
					robot.setCharAt(r,'K');//恢复原状
					return r;
				}
				else
					robot.setCharAt(r,'K');//恢复原状
			}
		//没自己能直接赢的位置再看对方能赢的地方
		char whoTurn2 = (whoTurn == 'O')?'X':'O';//换成对方思考
		for(int r=0;r<9;r++)//依次在空白处填上对方的棋子
			if(robot.charAt(r)=='K') {
				robot.setCharAt(r,whoTurn2);
				if(isWin(whoTurn2)) {//看填上棋子后是否能赢
					robot.setCharAt(r,'K');//恢复原状
					return r;
				}
				else
					robot.setCharAt(r,'K');//恢复原状
			}
		//运用课上所学知识，计算估价函数，运用极大极小搜索方法求最优解
		//估价函数e = e1 - e2
		//e1表示棋局上有可能使X成三子一线的数目
		//e2表示棋局上有可能使O成三子一线的数目
		//max表示较大的倒退值，R表示最优解的位置
			int e,e1 = 0,e2 = 0,min,max = 0,R=0,r1=0;
			ArrayList list = new ArrayList();//用于存放3行3列棋盘的一维数组
			ArrayList list2 = new ArrayList();//用于计算e1
			ArrayList list3 = new ArrayList();//用于计算e2
			ArrayList<Integer> list4 = new ArrayList();//用于存放min的一维数组，用来计算max
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
							e1 = getf(list2);//用函数getf()计算e1
							list2.clear();//内容清空
							for(int c=0;c<9;c++)
								if(!list.get(c).equals('X'))
									list3.add(c);
							e2 = getf(list3);//用函数getf()计算e2
							list3.clear();//内容清空
							list.set(a, 'K');
							e = e1 - e2;
							list4.add(e);
						}
					min = Collections.min(list4);//用Collections类计算数组list4中的最小值
					list4.clear();//内容清空
					max = min;//用max记录第一次得到的最小值
					R = r3;//用R记录第一次得到的最优解的位置
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
					if(max < min)//获取max值以及最优解位置
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
	
	private int getf(ArrayList list2) {//计算e1,e2
		int i = 0;//i用来记录三子一线的数量
		//用Collection接口保存三子一线的集合
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
		int x = findplace();//根据策略找位置
		robot.setCharAt(x, whoTurn);
		b[x].setLabel(String.valueOf(whoTurn));
		if(gameOver())//判断游戏是否结束
		{
			for(int i=0;i<9;i++) {
				b[i].setLabel(String.valueOf(""));
				robot.setCharAt(i,'K');
			}
			whoTurn = 'O';
			computerTake();//现在计算机下棋
		}
		else
			whoTurn = (whoTurn == 'O')?'X':'O';//轮换玩家
	}
	
	public boolean gameOver() {
		if(isWin(whoTurn)) {//判断是否取胜
			JOptionPane.showMessageDialog(null, whoTurn+" win!");
//			System.exit(0);
			return true;
		}
		else if(isFull()) {//判断是否下满棋盘
			JOptionPane.showMessageDialog(null, "game is over!");
//			System.exit(0);
			return true;
		}
		return false;
	}
	
	public boolean isWin(char who) {
		String s3 = "" +who + who + who;
		String sum;//用来拼接一个方向的棋子标识
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
	
	public boolean isFull() {//判断是否下满
		return robot.toString().indexOf("K") == -1;
	}
	
	public static void main(String args[]) {
		Point p = new Point(900,200);
		JFrame f = new JFrame("一字棋—电脑先下");
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