package hc_dev.popup.actions.db;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class SourceHelper {
	String locationPath;
	String srcPath;
	//主表
	String dbTable;
	//表结构
	List<Column> dbTableColList;
	//生成bean文件
	boolean makeMODEL = false;
	//生成对应的action，控制调整
	boolean makeACTION = false;
	//生成dao
	boolean makeDAO = false;
	//生成业务处理层接口
	boolean makeSERVICE = false;
	
	
	/**
	 * 调用方法生成各个对应文件
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void make() throws ClassNotFoundException, SQLException{
		String filePathStart = locationPath.substring(0,locationPath.lastIndexOf("/"));
		String filePath = filePathStart+srcPath;
		String packPath = srcPath.substring(srcPath.indexOf("/src/")+5).replaceAll("/", ".");
		
		if(this.makeMODEL){
			DomainTools.createDomain(filePath, packPath, dbTable, dbTableColList);
		}
		if(this.makeACTION){
			ActionTools.createAction(filePath, packPath, dbTable, "");
		}
		if(this.makeDAO){
			DaoTools.createDaoIface(filePath, packPath, dbTable, "");
			DaoTools.createDaoImpl(filePath, packPath, dbTable, "");
		}
		if(this.makeSERVICE){
			ServiceTools.createServiceIface(filePath, packPath, dbTable, "");
			ServiceTools.createServiceImpl(filePath, packPath, dbTable, "");
		}
	}
	
	public void setLocationPath(String locationPath) {
		this.locationPath = locationPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public void setMainTable(String dbTable) {
		this.dbTable = dbTable;
	}
	public void setDbTableColList(List<Column> dbTableColList){
		this.dbTableColList = dbTableColList;
	}
	public void setMakeMODEL(boolean makeMODEL) {
		this.makeMODEL = makeMODEL;
	}
	public void setMakeACTION(boolean makeACTION) {
		this.makeACTION = makeACTION;
	}
	public void setMakeSERVICE(boolean makeSERVICE) {
		this.makeSERVICE = makeSERVICE;
	}
	public void setMakeDAO(boolean makeDAO) {
		this.makeDAO = makeDAO;
	}
}
