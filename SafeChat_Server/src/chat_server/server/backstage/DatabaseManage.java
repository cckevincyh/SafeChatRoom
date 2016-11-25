package chat_server.server.backstage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.common.User;
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖保佑             永无BUG 
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？ 
/**
 * 服务器后台的数据库处理类
 * @author Administrator
 *
 */
public class DatabaseManage {
	//定义连接数据库所需要的对象
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Connection ct = null;
	
	public void init(){
		//加载驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//得到连接
			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/ChatRoomDao","root","123");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DatabaseManage(){
		this.init();
	}
	
	
	//取得用户信息
	public ResultSet GetUser(String Name){
		try {
			ps = ct.prepareStatement("select * from Users where Name=?");
			ps.setString(1, Name);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	
	//注册用户信息
	public boolean Register(User user){
		boolean b = true;
		try {
			ps = ct.prepareStatement("insert into Users(Name,PassWord) values(?,?)");
			ps.setString(1, user.getName());
			ps.setString(2, user.getPassWords());
			if(ps.executeUpdate()!=1)  // 执行sql语句
			{
				b=false;
			}
		} catch (SQLException e) {
			b = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	//对是否登陆进行修改
	public boolean Update_IsLogin(User user,int isLogin){
		boolean b = true;
		try {
			ps = ct.prepareStatement("update Users set IsLogin=? where Name=?");
			ps.setInt(1, isLogin);
			ps.setString(2, user.getName());
			if(ps.executeUpdate()!=1)  // 执行sql语句
			{
				b=false;
			}
		} catch (SQLException e) {
			b = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	
	
	
	//关闭数据库资源
	public void close()
	{
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(ct!=null) ct.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
