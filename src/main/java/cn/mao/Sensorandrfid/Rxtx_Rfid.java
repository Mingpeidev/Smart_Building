package cn.mao.Sensorandrfid;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import cn.mao.util.CharFormatUtil;

@SuppressWarnings("restriction")
public class Rxtx_Rfid implements SerialPortEventListener {

	private static final String DEMONAME = "RFID串口";

	private CommPortIdentifier portId = null;

	// 输入输出流
	private static InputStream inputStream;
	private static OutputStream outputStream;

	// RS-232的串行口
	private static SerialPort serialPort;
	
	private static String ID="";

	public static void main(String[] args) {

		Rxtx_Rfid test1 = new Rxtx_Rfid();
		test1.init();
		System.out.println("主函数");

		Rxtx_Rfid.sendMsg("0200000446529C03");// 寻卡
		System.out.println("寻卡");

		// test1.closeSerialPort();
	}
	
	public static String getID(){
		return ID;
	}

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
			portId = CommPortIdentifier.getPortIdentifier("COM5");
			System.out.println("打开端口：" + portId.getName());
			serialPort = (SerialPort) portId.open(DEMONAME, 2000);
			// 设置串口监听
			serialPort.addEventListener(this);
			// 设置开启监听
			serialPort.notifyOnDataAvailable(true);
			// 设置波特率、数据位、停止位、检验位
			serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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

				if (CharFormatUtil.byte2HexStr(readBuffer, len).equals("02 00 00 05 46 00 04 00 4F 03")) {
					System.out.println("寻卡成功");
					Rxtx_Rfid.sendMsg("0200000447044F03");
					System.out.println("防冲突");
				}

				data2 = CharFormatUtil.byte2HexStr(readBuffer, len);
				int x = data2.split(" ").length;
				String[] handler = data2.split(" ");
				if (x == 12) {
					ID = handler[6] + handler[7] + handler[8] + handler[9];
					System.out.println("防冲突成功。卡号：" + ID);
				} else {
					ID="";
					System.out.println("fail");
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("RFID通信中断！");
			closeSerialPort();
		}

	}

	/**
	 * 向串口发送信息
	 * 
	 * @param info
	 */
	public static void sendMsg(String info) {

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
