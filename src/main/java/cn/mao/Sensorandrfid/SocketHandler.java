package cn.mao.Sensorandrfid;

import java.util.Iterator;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import cn.mao.Sensorandrfid.Rxtx_sensor;;

public class SocketHandler extends IoHandlerAdapter {
	
	SendThread sendThread;

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
			sendThread = new SendThread(session);
			sendThread.start();
		} else if (message.toString().trim().length() == 2) {
			System.out.println("收到控制------------------------------");
			Rxtx_sensor.sendMsg((String) message);

		} else {
			System.out.println("收到json------------------------------");

			// System.out.println(message.toString());

			// JSONObject json = new JSONObject(message.toString());

		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed");
		sendThread.stop();
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

	public class SendThread extends Thread {
		
		IoSession session;

		public SendThread(IoSession session) {
			this.session = session;
		}

		@Override
		public void run() {

			// 2.从容器中获取mapper
			while (true) {
				try {
					sleep(500);// 500ms
					
					System.out.println("啊哈哈");

					Set<String> keySet = Rxtx_sensor.dataAll.keySet();
					Iterator<String> it1 = keySet.iterator();
					while (it1.hasNext()) {
						String ID = it1.next();

						if (Rxtx_sensor.dataAll.get(ID).length() == 2) {
							session.write("02 07 18 00 F1 " + ID + " " + Rxtx_sensor.dataAll.get(ID) + " 11");
						} 
						if (Rxtx_sensor.dataAll.get(ID).length() == 4) {
							String x = Rxtx_sensor.dataAll.get(ID).substring(0, 2);
							String y = Rxtx_sensor.dataAll.get(ID).substring(2, 4);

							session.write("02 07 18 00 F1 " + ID + " " + x + " " + y + " 11");
						}

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
