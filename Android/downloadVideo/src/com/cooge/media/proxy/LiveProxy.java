package com.cooge.media.proxy;

import java.io.IOException;
import java.net.ServerSocket;

public class LiveProxy {

	public static int port = 16532;

	public static boolean isRun = false;

	public static void startService() {

		new Thread() {
			@Override
			public void run() {

				System.out.println("����cos����-----------");
				ServerSocket sock = null;
				try {
					sock = new ServerSocket(port);
					isRun = true;
				} catch (IOException e1) {
					e1.printStackTrace();
					isRun = false;
				}
				while (true) {
					try {
						new Thread(new ProxyRubble(sock.accept())).start();
					} catch (Exception e) {
						System.out.println("ֹͣcos����-----------");
						isRun = false;
						break;
					}
				}
				isRun = false;
				System.out.println("ֹͣcos����-----------");
			}
		}.start();
	}

}
