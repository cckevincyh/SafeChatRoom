package chat_server.server.backstage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import chat.common.Message;
import chat.common.MessageType;
import chat.common.User;
import chat.utils.IOUtils;
import chat_server.server.tools.ServerThreadCollection;
import chat_server.server.view.Server_Frame;



/**
                       ::
                      :;J7, :,                        ::;7:
                      ,ivYi, ,                       ;LLLFS:
                      :iv7Yi                       :7ri;j5PL
                     ,:ivYLvr                    ,ivrrirrY2X,
                     :;r@Wwz.7r:                :ivu@kexianli.
                    :iL7::,:::iiirii:ii;::::,,irvF7rvvLujL7ur
                   ri::,:,::i:iiiiiii:i:irrv177JX7rYXqZEkvv17
                ;i:, , ::::iirrririi:i:::iiir2XXvii;L8OGJr71i
              :,, ,,:   ,::ir@mingyi.irii:i:::j1jri7ZBOS7ivv,
                 ,::,    ::rv77iiiriii:iii:i::,rvLq@huhao.Li
             ,,      ,, ,:ir7ir::,:::i;ir:::i:i::rSGGYri712:
           :::  ,v7r:: ::rrv77:, ,, ,:i7rrii:::::, ir7ri7Lri
          ,     2OBBOi,iiir;r::        ,irriiii::,, ,iv7Luur:
        ,,     i78MBBi,:,:::,:,  :7FSL: ,iriii:::i::,,:rLqXv::
        :      iuMMP: :,:::,:ii;2GY7OBB0viiii:i:iii:i:::iJqL;::
       ,     ::::i   ,,,,, ::LuBBu BBBBBErii:i:i:i:i:i:i:r77ii
      ,       :       , ,,:::rruBZ1MBBqi, :,,,:::,::::::iiriri:
     ,               ,,,,::::i:  @arqiao.       ,:,, ,:::ii;i7:
    :,       rjujLYLi   ,,:::::,:::::::::,,   ,:i,:,,,,,::i:iii
    ::      BBBBBBBBB0,    ,,::: , ,:::::: ,      ,,,, ,,:::::::
    i,  ,  ,8BMMBBBBBBi     ,,:,,     ,,, , ,   , , , :,::ii::i::
    :      iZMOMOMBBM2::::::::::,,,,     ,,,,,,:,,,::::i:irr:i:::,
    i   ,,:;u0MBMOG1L:::i::::::  ,,,::,   ,,, ::::::i:i:iirii:i:i:
    :    ,iuUuuXUkFu7i:iii:i:::, :,:,: ::::::::i:i:::::iirr7iiri::
    :     :rk@Yizero.i:::::, ,:ii:::::::i:::::i::,::::iirrriiiri::,
     :      5BMBBBBBBSr:,::rv2kuii:::iii::,:i:,, , ,,:,:i@petermu.,
          , :r50EZ8MBBBBGOBBBZP7::::i::,:::::,: :,:,::i;rrririiii::
              :jujYY7LS0ujJL7r::,::i::,::::::::::::::iirirrrrrrr:ii:
           ,:  :@kevensun.:,:,,,::::i:i:::::,,::::::iir;ii;7v77;ii;i,
           ,,,     ,,:,::::::i:iiiii:i::::,, ::::iiiir@xingjief.r;7:i,
        , , ,,,:,,::::::::iiiiiiiiii:,:,:::::::::iiir;ri7vL77rrirri::
         :,, , ::::::::i:::i:::i:i::,,,,,:,::i:i:::iir;@Secbone.ii:::
		 
**/


/**
 * 服务器后台的处理类
 * @author Administrator
 *
 */
public class ServerManage implements Runnable{
	private  ServerSocket ss ;
	private ObjectInputStream ois;
	private ObjectOutputStream os;
	private Server_Connect_Database server;
	public  ServerManage() {
		Message m = new Message();
		m.setContent("服务器在9999端口监听..\r\n");
		m.setMessageType(MessageType.CommonMessage);
		Server_Frame.showMessage(m);
		try {
			ss = new ServerSocket(9999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	




	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true){
				try {
					//接受客户端发送过来的信息
					Socket s = null;
					try{
						s = ss.accept();
					}catch(SocketException e){
						if(s!=null){
							s.close();
						}
						break;
					}
					ois = new ObjectInputStream(s.getInputStream());
					Object obj = ois.readObject();
					if(obj instanceof User){	
						User user = (User)obj;
						//如果读取到的是用户注册的信息
						if(user.getType().equals(MessageType.UserRegister)){
							server = new Server_Connect_Database();
							Message mess = new Message();
							//如果注册成功
							if(server.CheckRegister(user)){
								mess.setMessageType(MessageType.Register_Success);
								os = new ObjectOutputStream(s.getOutputStream());
								os.writeObject(mess);
								s.close();
							}else{
								mess.setMessageType(MessageType.Register_Fail);
								os = new ObjectOutputStream(s.getOutputStream());
								os.writeObject(mess);
								s.close();
							}
						}
						
						
						//如果读取到的是用户登陆的信息
						if(user.getType().equals(MessageType.UserLogin)){
							server = new Server_Connect_Database();
							Message mess = new Message();
							//判断是否重复登陆
							if(server.Check_IsLogin(user)){
								//登陆过了
								mess.setMessageType(MessageType.Login);
								os = new ObjectOutputStream(s.getOutputStream());
								os.writeObject(mess);
								s.close();
							}else{
								//没登陆过
								mess.setMessageType(MessageType.NoLogin);
								os = new ObjectOutputStream(s.getOutputStream());
								os.writeObject(mess);
								//如果登陆成功
								if(server.CheckLogin(user) && server.Update_IsLogin(user, 1)){//更改成登陆了
									mess.setMessageType(MessageType.Login_Success);
									os = new ObjectOutputStream(s.getOutputStream());
									os.writeObject(mess);		
									//登陆成功后，单独开一个线程为客户端服务，并将该线程放入集合,以便取出遍历
									ServerConClient scc = new ServerConClient(s,user.getName());
									//添加入集合
									ServerThreadCollection.addServerConnectClientThreadCollection(user.getName(), scc);
									//服务器更新在线用户列表
									scc.ServerUpdataOnline();
									//启动线程序
									Thread t = new Thread(scc);
									t.start();
									//提醒其他用户更新在线列表
									scc.UpdataOnline();
								}else{
									mess.setMessageType(MessageType.Login_Fail);
									os = new ObjectOutputStream(s.getOutputStream());
									os.writeObject(mess);
									s.close();
								}
							}
						}
					}else if(obj instanceof Message){
						//接受到的是消息，说明是客户端发送过来的秘钥
						Message mess = (Message) obj;
						//保存秘钥到文件中
						IOUtils.SaveKeyFile(mess.getSender()+ "_PublicKey.key", mess.getContent());
					}
					
				}catch(SocketException e){
					e.printStackTrace();
					break;
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();		
		}
	}
	
	
	
	
	/**
	 * 发送系统消息的方法
	 * @param message 系统消息
	 */
	public static void Send_SystemMessage(String message){
		
		Message mess = new Message();
		mess.setContent(message);
		mess.setMessageType(MessageType.System_Messages);
		//获得在线用户
		String string = ServerThreadCollection.GetOnline();
		String[] strings = string.split(" ");
		String Name = null;
		for(int i=0;i<strings.length;i++){
			Name = strings[i];
			//设置接收用户
			mess.setGetter(Name);
			//获得其他服务器端与客户端通信的线程
			ServerConClient sccc = ServerThreadCollection.getServerContinueConnetClient(Name);
			try {
				ObjectOutputStream os = new ObjectOutputStream(sccc.getS().getOutputStream());
				os.writeObject(mess);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 关闭服务器的方法
	 */
	public void CloseServer(){
		try {
			//获得在线用户
			String string = ServerThreadCollection.GetOnline();
			String[] strings = string.split(" ");
			for(int i=0;i<strings.length;i++){
				//获得其他服务器端与客户端通信的线程
				ServerConClient sccc = ServerThreadCollection.getServerContinueConnetClient(strings[i]);
				if(sccc!=null){
					sccc.CloseThread();
				}
			}
			this.ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
