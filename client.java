package com.node.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Client extends Thread{

	private static final String HOST = "localhost";
	private static final int PORT = 9009;
	private SocketChannel socket = null;
	private Selector selector = null;
	private static final Charset charset = Charset.forName("utf-8");
	private final List pool = new LinkedList();
	/**
	 * * */
	public Client() throws IOException{
		selector = Selector.open();
		socket = SocketChannel.open();
		socket.configureBlocking(false);
		socket.connect(new InetSocketAddress(HOST, PORT));
		socket.register(selector, SelectionKey.OP_READ);
		socket.finishConnect();
		new ChatTerminal(this).start();
	}
	
	@Override
	public void run() {
		while(true){
			try {
				System.out.println("trying to select....");
				int num = selector.select();
				if(num > 0){
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while(keys.hasNext()){
						SelectionKey key = keys.next();
						keys.remove();
						if(key.isReadable()){
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							socket.read(buffer);
							buffer.flip();
							String msg = charset.decode(buffer).toString();
							System.err.println("<< " + msg);
						}
						if(key.isWritable()){
							StringBuilder sb = new StringBuilder();
							synchronized(pool){
								while(pool.size()>0){
									sb.append(pool.remove(0)).append("\r\n");
								}
							}
							CharBuffer b = CharBuffer.wrap(sb.toString());
							socket.write(charset.encode(b));
							key.cancel();
						}
					}
				}else{
					//注册写通道
					socket.register(selector, SelectionKey.OP_WRITE);
				}
			} catch (IOException e) {
				e.printStackTrace();
				try{selector.close();socket.close();}catch(IOException ex){ex.printStackTrace();}
				break;
			}
		}
	}
	
	public void addMessageToPool(String msg){
		synchronized(pool){
			pool.add(pool.size(),msg);
			pool.notifyAll();
			selector.wakeup();
		}
	}

}

