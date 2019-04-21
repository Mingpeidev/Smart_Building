package cn.mao.Sensorandrfid;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import cn.mao.pojo.Sensor;
import cn.mao.pojo.Setting;
import cn.mao.service.SensorService;
import cn.mao.service.SettingService;
import cn.mao.util.ApplicationContextHelper;
import cn.mao.util.CharFormatUtil;
import cn.mao.util.ScheduleUtil;

@SuppressWarnings("restriction")
public class Rxtx_sensor implements SerialPortEventListener {

	private SensorService sensorService;
	private SettingService settingService;

	private String temp = "";// 传感器数据十六进制
	private String humi = "";
	private String light = "";
	// private String human = "";
	// private String smoke = "";
	// private String control = "";

	String lamp_control = "";// 控制二进制字节
	String air_control = "";
	String alarm_control = "";
	String door_control = "";

	private int Stemp = 26;// 设置的阈值
	private int Shumi = 50;
	private Integer Slight = null;
	private String Stimeon;
	private String Stimeoff;
	private String Smart = "";

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

		// test1.closeSerialPort();
	}

	// 温湿度、光照等传感器数据写入线程
	ScheduleUtil.SRunnable insertsensorRunnable = new ScheduleUtil.SRunnable() {
		@Override
		public void run() {

			String temp1 = "";
			String humi1 = "";
			String light1 = "";

			// 读取dataAll传感器数据
			Set<String> keySet = dataAll.keySet();
			Iterator<String> it1 = keySet.iterator();
			while (it1.hasNext()) {
				String ID = it1.next();
				if (ID.equals("8B 55 01")) {
					temp1 = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				} else if (ID.equals("8B 55 02")) {
					humi1 = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				} else if (ID.equals("E9 4E 01")) {
					light1 = String.valueOf(CharFormatUtil.exchange(dataAll.get(ID)));
				}
			}

			sensorService = ApplicationContextHelper.getBean(SensorService.class);// 写入传感器数据到数据库
			Sensor sensor = new Sensor();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			sensor.setTemp(temp1);
			sensor.setHumi(humi1);
			sensor.setLight(light1);
			sensor.setHuman("0");
			sensor.setSmoke("0");
			sensor.setTime(timestamp);

			sensorService.insertSensor(sensor);
			System.out.println("写入数据库" + timestamp);

			settingService = ApplicationContextHelper.getBean(SettingService.class);// 获取数据库设置信息
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");// 指定类型

			Setting setting = settingService.selectSetting(1);

			Stemp = setting.getTemp();// 初始化阈值
			Shumi = setting.getHumi();
			Slight = setting.getLight();
			Stimeon = sdfTime.format(setting.getTimeon());// sqltime转为date指定类型
			Stimeoff = sdfTime.format(setting.getTimeoff());// sqltime转为date指定类型
			Smart = setting.getSmart();

		}

		@Override
		public String getName() {
			return "insertsensor";
		}
	};

	// 智能阈值控制线程
	ScheduleUtil.SRunnable smartRunnable = new ScheduleUtil.SRunnable() {
		@Override
		public void run() {

			// 读取dataAll传感器数据
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

			settingService = ApplicationContextHelper.getBean(SettingService.class);// 获取数据库设置信息
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");// 指定类型

			Setting setting = settingService.selectSetting(1);

			Stemp = setting.getTemp();// 初始化阈值
			Shumi = setting.getHumi();
			Slight = setting.getLight();
			Stimeon = sdfTime.format(setting.getTimeon());// sqltime转为date指定类型
			Stimeoff = sdfTime.format(setting.getTimeoff());// sqltime转为date指定类型
			Smart = setting.getSmart();

			SmartControl();

		}

		@Override
		public String getName() {
			return "smartRunnable";
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
				ScheduleUtil.stard(insertsensorRunnable, 20, 20, TimeUnit.SECONDS);// 每20s写入一次传感器数据到数据库
				System.out.println("开启传感器写入进程");
			}

			if (!ScheduleUtil.isAlive(smartRunnable) && serialPort != null) {
				ScheduleUtil.stard(smartRunnable, 10, 10, TimeUnit.SECONDS);// 每10s读取数据库与传感器信息并按设定的阈值运行
				System.out.println("开启智能控制进程");
			}

			break;

		default:
			break;
		}
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

	/**
	 * 阈值智能控制
	 */
	public void SmartControl() {

		Calendar Scalendaron = Calendar.getInstance();// 设置数据库timeon阈值时间
		Scalendaron.setTimeInMillis(System.currentTimeMillis());

		Scalendaron.set(Calendar.HOUR_OF_DAY, Integer.valueOf(Stimeon.substring(0, 2)));
		Scalendaron.set(Calendar.MINUTE, Integer.valueOf(Stimeon.substring(3, 5)));
		Scalendaron.set(Calendar.SECOND, 0);
		Scalendaron.set(Calendar.MILLISECOND, 0);

		Calendar Scalendaroff = Calendar.getInstance();// 设置数据库timeoff阈值时间
		Scalendaroff.setTimeInMillis(System.currentTimeMillis());

		Scalendaroff.set(Calendar.HOUR_OF_DAY, Integer.valueOf(Stimeoff.substring(0, 2)));
		Scalendaroff.set(Calendar.MINUTE, Integer.valueOf(Stimeoff.substring(3, 5)));
		Scalendaroff.set(Calendar.SECOND, 0);
		Scalendaroff.set(Calendar.MILLISECOND, 0);

		Calendar calendar = Calendar.getInstance();// 现在系统时间
		calendar.setTimeInMillis(System.currentTimeMillis());

		String control = "";
		control = dataAll.get("14 24 01");

		String x = CharFormatUtil.hexString2binaryString(control);

		lamp_control = x.substring(4, 5);// 初始化控制信息
		air_control = x.substring(5, 6);
		alarm_control = x.substring(6, 7);
		door_control = x.substring(7, 8);

		System.out.println(Double.valueOf(temp) + "@" + Double.valueOf(humi) + "@" + Double.valueOf(light) + "@" + Stemp
				+ "@" + Shumi + "@" + Slight + "@" + Stimeon + "@" + Stimeoff + "@" + Smart);

		if (Smart.equals("on")) {
			System.out.println("智能控制方法");

			// 温度自控
			if (Double.valueOf(temp) > Stemp && air_control.equals("1")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + "0" + alarm_control + door_control));
				air_control = "0";
				System.out.println("airoff");
			} else if (Double.valueOf(temp) < Stemp && air_control.equals("0")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + "1" + alarm_control + door_control));
				air_control = "1";
				System.out.println("airon");
			}

			// 湿度自控
			if (Double.valueOf(humi) > Shumi && alarm_control.equals("1")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + "0" + door_control));
				alarm_control = "0";
				System.out.println("alarmoff");
			} else if (Double.valueOf(humi) < Shumi && alarm_control.equals("0")) {
				sendMsg("0" + CharFormatUtil.binaryString2hexString(lamp_control + air_control + "1" + door_control));
				alarm_control = "1";
				System.out.println("alarmon");
			}

			// 灯智能控制
			if (Slight == null) {

				// 时间控制
				if (calendar.getTimeInMillis() < Scalendaron.getTimeInMillis()
						&& calendar.getTimeInMillis() > Scalendaroff.getTimeInMillis() && lamp_control.equals("1")) {
					sendMsg("0"
							+ CharFormatUtil.binaryString2hexString("0" + air_control + alarm_control + door_control));
					lamp_control = "0";
					System.out.println("lampofft");
				} else if (calendar.getTimeInMillis() > Scalendaron.getTimeInMillis()
						&& calendar.getTimeInMillis() < Scalendaroff.getTimeInMillis() && lamp_control.equals("0")) {
					sendMsg("0"
							+ CharFormatUtil.binaryString2hexString("1" + air_control + alarm_control + door_control));
					lamp_control = "1";
					System.out.println("lampont");
				}

			} else {// 光照阈值控制
				if (Double.valueOf(light) > Slight && lamp_control.equals("1")) {
					sendMsg("0"
							+ CharFormatUtil.binaryString2hexString("0" + air_control + alarm_control + door_control));
					lamp_control = "0";
					System.out.println("lampoff");
				} else if (Double.valueOf(light) < Slight && lamp_control.equals("0")) {
					sendMsg("0"
							+ CharFormatUtil.binaryString2hexString("1" + air_control + alarm_control + door_control));
					lamp_control = "1";
					System.out.println("lampon");
				}
			}

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
}
