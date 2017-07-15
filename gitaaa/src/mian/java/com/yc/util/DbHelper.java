package com.yc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbHelper<T> {
//	static String driverName = "oracle.jdbc.driver.OracleDriver";
//	static String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
//	static String username = "scott";
//	static String userpwd = "a";
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	private ResultSet rs = null;
	
	//加载驱动
	static{
		try{
			Class.forName(MyProperties.getInstance().getProperty("driverName"));
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取数据库连接对象
	 * @return
	 */
	public Connection getConn(){
		//获取对象
		try {
			//getConnection(url,Properties);
			conn = DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),MyProperties.getInstance());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//从容器tomcat中取出名为jdbc/bbs2db的连接池中的联结
		
//		try {
//			Context initCtx = new InitialContext();  //创建一个容器
//			Context envCtx = (Context) initCtx.lookup("java:comp/env");  //查找资源
//			DataSource ds = (DataSource)envCtx.lookup("jdbc/product");	//提供要查找的资源名
//			conn = ds.getConnection();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		return conn;
		
		
	}
	/**
	 * 关闭对象
	 * @param conn
	 * @param pstmt
	 * @param rs
	 */
	public void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
		if(null!=rs){//关闭结果集
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//关闭语句对象
		if(null!=pstmt){
			try{
				pstmt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//关闭连接对象
		if(null!=conn){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public List<T> findObject(String sql,List<Object> params,Class<T> c) throws Exception{
		List<Map<String, Object>> list = findMultiObject(sql, params);
		
		List<T> listT = new ArrayList<T>();
		
		if(list!=null){
			for(Map<String, Object> map:list){
				T t = RequestUtil.getParemeter(map,c);
				listT.add(t);
			}
		}
		return listT;
	}
	
	
	
	/**
	 * 查看操作：sql语句可以查询出多条记录
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public List<Map<String, Object>> findMultiObject(String sql,List<Object> params) throws SQLException, FileNotFoundException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		try{
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			this.setparams(pstmt, params);
			rs = pstmt.executeQuery();
			//获取结果集中所有的列名
			List<String> columnNames = getAllColumnName(rs);
			while(rs.next()){
				map = new HashMap<String,Object>();
				for(String name:columnNames){
					map.put(name, rs.getObject(name));
				}
				list.add(map);
			}
		}finally{
			this.closeAll(conn, pstmt, rs);
		}
		return list;
	}
	/**
	 * 查询操作，select * from emp where id = ?  只有一条结果
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public Map<String, Object> findSingleObject(String sql,List<Object> params) throws SQLException, FileNotFoundException{
		Map<String, Object> map = null;
		try{
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			this.setparams(pstmt, params);
			rs = pstmt.executeQuery();
			//获取结果集中所有的列名
			List<String> columnNames = getAllColumnName(rs);
			if(rs.next()){
				map = new HashMap<String,Object>();
				for(String name:columnNames){
					map.put(name, rs.getObject(name));
				}
			}
		}finally{
			this.closeAll(conn, pstmt, rs);
		}
		return map;
	}
	
	
	
	
	/**
	 * 获取结果集中的所有列表
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<String> getAllColumnName(ResultSet rs) throws SQLException{
		List<String> columnNames = new ArrayList<String>();
		ResultSetMetaData dd = rs.getMetaData();
		for(int i=1;i<=dd.getColumnCount();i++){
			columnNames.add(dd.getColumnName(i));
		}
		return columnNames;
	}
	
	
	/**
	 * 单条sql语句  更新操作：增删改
	 * @param sql  语句
	 * @param params  传入的参数
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public int doUpdate(String sql,List<Object> params) throws SQLException, FileNotFoundException{
		int result = 0;
		try{
			conn = this.getConn();//获取连接对象
			pstmt = conn.prepareStatement(sql);
			//设置参数
			this.setparams(pstmt,params);
			result = pstmt.executeUpdate();
		}finally{
			//关闭对象
			this.closeAll(conn, pstmt, null);
		}
		return result;
	}
	/**
	 * 多条sql语句的更新操作  批处理  注意：这些sql语句执行的结果要么一起成功，要么一起失败
	 * @param sqls
	 * @param params    对应每一条sql语句所需要的参数集合
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public int doUpdate(List<String> sqls,List<List<Object>> params) throws SQLException, FileNotFoundException{
		int result = 0;
		try{
			conn = this.getConn();
			//设置事务提交方式为手动提交
			conn.setAutoCommit(false);
			if(null != sqls&& sqls.size()>0){
				//对sql语句进行循环
				for(int i=0;i<sqls.size();i++){
					String sql = sqls.get(i);
					pstmt = conn.prepareStatement(sql);
					this.setparams(pstmt, params.get(i));//第几条sql语句对应list集合中的第一个小list
					result = pstmt.executeUpdate();
				}
			}
			conn.commit();//手动提交事务
		}catch(SQLException e){
			conn.rollback();//事务回滚
		}finally {
			conn.setAutoCommit(true);//恢复事务
			this.closeAll(conn, pstmt, rs);
		}
		return result;
	}
	
	
	
	/**
	 * 设置参数
	 * @param pstmt  预编译对象
	 * @param params  外部传入的参数值  添加值时顺序要和？对应值得顺序一致
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public void setparams(PreparedStatement pstmt,List<Object> params) throws SQLException, FileNotFoundException{
		if(null!=params&&params.size()>0){
			//图片必须单独处理，传入的数据流，所以从界面上传入的文件对象
			for(int i=0;i<params.size();i++){
				File f = new File("");
				if(params.get(i) instanceof File){//判断参数是否为文件对象
				//insert into student values(seq_stu_id.nextval,1,?,default,?,?,?,sysdate)
					File file = (File)params.get(i);//强转为文件对象
					InputStream in = new FileInputStream(file);//转为输入流对象
					pstmt.setBinaryStream(i+1, in,(int)file.length());
				}else{
					pstmt.setObject(i+1, params.get(i));//设置？值
				}
			}
		}
	}
	/**
	 * 聚合函数查询    select count(*) from emp;
	 * @param sql
	 * @param parama
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException 
	 */
	public double getCount(String sql,List<Object> parama) throws SQLException, FileNotFoundException{
		double result = 0;
		try {
			conn = this.getConn();
			pstmt = conn.prepareStatement(sql);
			setparams(pstmt, parama);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getDouble(1);//获取第一列
			}
		} finally {
			this.closeAll(conn, pstmt, rs);
			
		}
		return result;
	}
	
	
	
	
	
}
