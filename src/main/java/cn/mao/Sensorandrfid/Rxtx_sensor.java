package cn.mao.Sensorandrfid;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import cn.mao.pojo.Sensor;
import cn.mao.service.SensorService;
import cn.mao.util.ApplicationContextHelper;
import cn.mao.util.CharFormatUtil;
import cn.mao.util.ScheduleUtil;

@SuppressWarnings("restriction")
public class Rxtx_sensor implements SerialPortEventListener {

	private SensorService sensorService;

	private String temp = "";
	private String humi = "";
	private String light = "";
	private String human = "";
	private String smoke = "";
	private String control = "";

	String lamp_control = "";
	String air_control = "";
	String alarm_control = "";
	String door_control = "";

	private int Swendu = 26;
	private int Smart = 0;

	private static final String DEMONAME = "串口测试";

	private CommPortIdentifier portId = null;

	// 输入输出流
	private static InputStream inputStream;
	private static OutputStream outputStream;

	// RS-232的串行口
	private static SerialPort serialPort;

	// 地址
	public static Map<String, String> dataAll = new HashMap<String, String>();

	public static void main(String[] args) {
		Rxtx_sensor test1 = new Rxtx_sensor();
		test1.init();
		System.out.println("主函数");

		MaplayoutThread map1 = test1.new MaplayoutThread();
		map1.start();

		// test1.closeSerialPort();
	}

	// 温湿度、光照写入进程
	ScheduleUtil.SRunnable insertsensorRunnable = new ScheduleUtil.SRunnable() {
		@Override
		public void run() {

			// 读取dataAll
			Set<String> keySet = dataAll.keySet();
			Iterator<String> it1 = keySet.iterator();
			while (it1.hasNext()) {
				String ID = it1.next();
				if (ID.equals("8B 55 01")) {
					temp = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				} else if (ID.equals("8B 55 02")) {
					humi = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				} else if (ID.equals("E9 4E 01")) {
					light = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				}
			}

			sensorService = ApplicationContextHelper.getBean(SensorService.class);
			Sensor sensor = new Sensor();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			sensor.setTemp(temp);
			sensor.setHumi(humi);
			sensor.setLight(light);
			sensor.setHuman("0");
			sensor.setSmoke("0");
			sensor.setTime(timestamp);

			sensorService.insertSensor(sensor);
			System.out.println("写入数据库" + timestamp);

		}

		@Override
		public String getName() {
			return "insertsensor";
		}
	};

	/**
	 * 串口监听函数
	 */
	public void serialEvent(SerialPortEvent serialPortEvent) {
		switch (serialPortEvent.getEventType()) {
		case SerialPortEvent.BI: // 通讯中断
			break;
		case SerialPortEvent.OE: // 溢位错误
		case SerialPortEvent.FE: // 帧错误
		case SerialPortEvent.PE: // 奇偶校验错误
		case SerialPortEvent.CD: // 载波检测
		case SerialPortEvent.CTS: // 清除发送
		case SerialPortEvent.DSR: // 数据设备准备好
		case SerialPortEvent.RI: // 响铃侦测
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:// 输出缓冲区已清空
			break;
		// 获取到有效信息
		case SerialPortEvent.DATA_AVAILABLE:

			readComm();

			if (!ScheduleUtil.isAlive(insertsensorRunnable) && serialPort != null) {
				ScheduleUtil.stard(insertsensorRunnable, 20, 20, TimeUnit.SECONDS);// 每10s写入一次传感器数据到数据库
				System.out.println("开启进程");
			}

			// ErrorControl();

			break;

		default:
			break;
		}
	}

	/**
	 * 初始化串口
	 * 
	 * @param baudRate
	 *            波特率
	 */

	public void init() {
		if (serialPort != null) {
			closeSerialPort();
			System.out.println("端口被占用，先关闭串口，再连接！");
		}
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM3");
			System.out.println("打开端口：" + portId.getName());
			serialPort = (SerialPort) portId.open(DEMONAME, 2000);
			// 设置串口监听
			serialPort.addEventListener(this);
			// 设置开启监听
			serialPort.notifyOnDataAvailable(true);
			// 设置波特率、数据位、停止位、检验位
			serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			// 获取输入流
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
		} catch (PortInUseException e) {
			System.out.println("端口被占用");
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			System.out.println("串口监听类数量过多！添加操作失败！");
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("输入出错");
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			System.out.println("没有该端口");
			e.printStackTrace();
		}
	}

	public int getSmart() {
		return Smart;
	}

	public void setSmart(int smart) {
		Smart = smart;
	}

	/**
	 * 读取串口返回信息，并定义其长度,主要用这个进行数据库和智能控制
	 */
	public void readComm() {
		byte[] readBuffer = new byte[1024];
		String data2 = null;
		try {
			// 从线路上读取数据流
			int len = 0;
			while ((len = inputStream.read(readBuffer)) != -1) {

				System.out.println("实时反馈：" + CharFormatUtil.byte2HexStr(readBuffer, len));

				data2 = CharFormatUtil.byte2HexStr(readBuffer, len);
				int x = data2.split(" ").length;
				String[] handler = data2.split(" ");
				if (x == 10) {
					// s传感器短地址，t传感器获取数据
					String s = handler[5] + " " + handler[6] + " " + handler[7];
					String t = handler[8];
					// System.out.println("网络地址：" + s + " 值：" + t);
					dataAll.put(s, t);
				} else if (x == 11) {
					String s = handler[5] + " " + handler[6] + " " + handler[7];
					String t = handler[8] + handler[9];
					// System.out.println("网络地址：" + s + " 值：" + t);
					dataAll.put(s, t);
				}

				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("通信中断！");
			ScheduleUtil.stop(insertsensorRunnable);
			System.out.println("关闭进程！");
			closeSerialPort();
		}

	}

	/**
	 * 向串口发送信息
	 * 
	 * @param com
	 */
	public static void sendMsg(String com) {

		String info = "";
		String msg = "071800F1142401" + com;// 要发送的命令
		info = "02" + msg + CharFormatUtil.checkcode(msg);
		System.out.println("info=" + info + "  字符串：" + CharFormatUtil.hexStr2Bytes(info));

		try {
			outputStream.write(CharFormatUtil.hexStr2Bytes(info));
			outputStream.flush();
			System.out.println("输出成功");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("输出失败");
		}
	}

	public void sendO2Msg(String o2) {
		System.out.println("o2:" + o2);
		System.out.println("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + o2 + door_control));
		sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + o2 + door_control));
		alarm_control = o2;
	}

	public void sendWaterMsg(String water) {
		System.out.println("water:" + water);
		System.out.println(
				"0" + CharFormatUtil.binaryString2hexString(lamp_control + water + alarm_control + door_control));
		sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + water + alarm_control + door_control));
		air_control = water;
	}

	class MaplayoutThread extends Thread {
		@Override
		public void run() {

			// 2.从容器中获取mapper

			while (true) {
				try {
					sleep(3000);
					Set<String> keySet = dataAll.keySet();
					Iterator<String> it1 = keySet.iterator();
					while (it1.hasNext()) {
						String ID = it1.next();
						System.out.println("哈哈哈：" + ID + " " + dataAll.get(ID));
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public void ErrorControl() {

		String control = "";
		control = dataAll.get("00 A0 01");

		String x = CharFormatUtil.hexString2binaryString(control);

		lamp_control = x.substring(4, 5);
		air_control = x.substring(5, 6);
		alarm_control = x.substring(6, 7);
		door_control = x.substring(7, 8);
		if (Smart == 1) {
			if (dataAll.get("EE 61 01").equals("01") && air_control.equals("1")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + "0" + alarm_control + door_control));
			}
			/*
			 * else if (dataAll.get("EE 61 01").equals("00") &&
			 * water_control.equals("0")) { sendMsg("0" +
			 * binaryString2hexString(light_control + "1" + addo2_control +
			 * heating_control)); }
			 */

			String[] handler = dataAll.get("47 8C 01").split(" ");
			String t = handler[1] + handler[0];
			float wendu = (float) (Integer.parseInt(t, 16) / 100.00);

			if (wendu > Swendu && door_control.equals("1")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + alarm_control + "0"));
			} else if (wendu < Swendu && door_control.equals("0")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + alarm_control + "1"));
			}
		}
	}

	public int getSwendu() {
		return Swendu;
	}

	public void setSwendu(int swendu) {
		Swendu = swendu;
	}

	/**
	 * 关闭串口
	 */
	public void closeSerialPort() {
		if (serialPort != null) {

			serialPort.notifyOnDataAvailable(false);
			serialPort.removeEventListener();

			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;

				} catch (IOException e) {
					System.out.println("hhh");
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
				}
			}
			serialPort.close();
			serialPort = null;
			System.out.println("已关闭端口！");
		}
	}

	/**
	 * 判断是否连接串口
	 * 
	 * @return
	 */
	public static String judgelink() {
		if (serialPort != null) {
			return "link";
		} else {
			return null;
		}
	}
}
