package cn.mao.Sensorandrfid;

import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import cn.mao.Sensorandrfid.Rxtx_sensor;;

public class SocketHandler extends IoHandlerAdapter {

	private String smart = "";
	private int water;
	private int wendu;
	private int o2;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("exceptionCaught: " + cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("recieve : " + (String) message);
		// session.write("hello I am server");
		if (message.toString().equals("111")) {
			System.out.println("收到111------------------------------");
			SendThread map1 = new SendThread(session);
			map1.start();
		} else if (message.toString().trim().length() == 2) {
			System.out.println("收到控制------------------------------");
			Rxtx_sensor.sendMsg((String) message);

		} else {
			System.out.println("收到json------------------------------");

			// System.out.println(message.toString());

			JSONObject json = new JSONObject(message.toString());

			smart = json.getString("smart");
			wendu = json.getInt("watertemp");
			water = json.getInt("watertime");
			o2 = json.getInt("o2");

			System.out.println(smart + wendu + water + o2);
		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpen");

		System.out.println("尝试连接！手机");
		new Rxtx_sensor().init();

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	class SendThread extends Thread {

		public SendThread(IoSession session) {
			super();
			this.session = session;
		}

		IoSession session;

		@Override
		public void run() {

			// 2.从容器中获取mapper
			while (true) {
				try {
					sleep(500);// 100ms

					Set<String> keySet = Rxtx_sensor.dataAll.keySet();
					Iterator<String> it1 = keySet.iterator();
					while (it1.hasNext()) {
						String ID = it1.next();
						if (ID.equals("EE 61 01")) {
							session.write("02 07 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
							// System.out.println("输出："+"02 07 18 00 F1 "+ID + "
							// " +
							// testRxtx.dataAll.get(ID)+" 11");

						} else if (ID.equals("D7 15 01")) {
							session.write("02 08 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
							// System.out.println("输出："+"02 08 18 00 F1 "+ID + "
							// " +
							// testRxtx.dataAll.get(ID)+" 11");

						} else if (ID.equals("00 A0 01")) {
							session.write("02 07 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
							// System.out.println("输出："+"02 07 18 00 F1 "+ID + "
							// " +
							// testRxtx.dataAll.get(ID)+" 11");

						} else if (ID.equals("47 8C 01")) {
							session.write("02 07 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
							// System.out.println("输出："+"02 07 18 00 F1 "+ID + "
							// " +
							// testRxtx.dataAll.get(ID)+" 11");

						} else if (ID.equals("47 8C 02")) {
							session.write("02 07 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
							// System.out.println("输出："+"02 07 18 00 F1 "+ID + "
							// " +
							// testRxtx.dataAll.get(ID)+" 11");

						}
					}

					// session.write(testRxtx.xxx);
					// System.out.println("输出："+testRxtx.xxx);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
