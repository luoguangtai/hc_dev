package hc_dev.popup.actions.db;

import java.io.File;

public class DaoTools {
	// 接口
	public static void createDaoIface(String pkgPath, String root_pkg, String tableName, String pkName) {
		String className = Util.tableNameToClassName(tableName);
		pkName = pkName.toLowerCase();
		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".dao;\n");
		sb.append("\n");

		// import
		sb.append("import ").append("com.hc.dao.IBaseDao;\n");
		sb.append("\n");
		sb.append("import ").append(root_pkg).append(".model.").append(className).append(";\n");
		// class
		sb.append("\n");
		sb.append("public interface ").append("I").append(className).append("Dao extends IBaseDao {\n");
		sb.append("\n");
		sb.append("}");

		Util.toFile(pkgPath + File.separator + "dao", "I" + className + "Dao.java", sb.toString());
	}

	/**
	 * 生成实现类
	 * 
	 * @param pkgPath
	 * @param root_pkg
	 * @param tableName
	 * @param pkName
	 * @param detailTable
	 */
	public static void createDaoImpl(String pkgPath, String root_pkg, String tableName, String pkName) {
		String className = Util.tableNameToClassName(tableName);
		String p = className.substring(0, 1).toLowerCase() + className.substring(1);
		pkName = pkName.toLowerCase();
		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".dao.impl;\n");
		sb.append("\n");

		// import
		sb.append("import java.util.List;\n");
		sb.append("\n");
		sb.append("import org.springframework.stereotype.Repository;\n");
		sb.append("import com.hc.exception.AppException;\n");
		sb.append("import com.hc.dao.BaseDao;\n");
		sb.append("\n");
		sb.append("import ").append(root_pkg).append(".dao.").append("I").append(className).append("Dao;\n");
		sb.append("import ").append(root_pkg).append(".model.").append(className).append(";\n");
		sb.append("\n");

		// className
		sb.append("@Repository(\"").append(root_pkg).append(".dao.impl.").append(className).append("Dao\")\n");
		sb.append("public class ").append(className).append("Dao extends BaseDao implements ").append("I").append(className).append("Dao {\n");
		// classBody
		// 方法1
		sb.append("\n");
		sb.append(Util.TAB).append("public ").append(className).append("Dao() {\n");
		sb.append(Util.TAB).append(Util.TAB).append("modelClass = ").append(className).append(".class;\n");
		sb.append(Util.TAB).append("}\n");
		// 方法2
		sb.append("\n");
		sb.append(Util.TAB).append("public List<").append(className).append("> get").append(className).append("List(").append(className)
				.append(" paramBase){\n");
		sb.append(Util.TAB).append(Util.TAB).append("try{\n");
		sb.append(Util.TAB).append(Util.TAB).append(Util.TAB).append("String hql = \"\";\n");
		sb.append(Util.TAB).append(Util.TAB).append(Util.TAB).append("hql += \"from ").append(className).append(" A  where A.dataState = '1' \";\n");
		sb.append(Util.TAB).append(Util.TAB).append(Util.TAB).append("List<").append(className).append("> list = this.findListByHql(hql);\n");
		sb.append(Util.TAB).append(Util.TAB).append(Util.TAB).append("return list;\n");
		sb.append(Util.TAB).append(Util.TAB).append("}catch(Exception ex){\n");
		sb.append(Util.TAB).append(Util.TAB).append(Util.TAB).append("throw new AppException(ex);\n");
		sb.append(Util.TAB).append(Util.TAB).append("}\n");
		sb.append(Util.TAB).append("}\n");
		sb.append("}");

		Util.toFile(pkgPath + File.separator + "dao" + File.separator + "impl", className + "Dao.java", sb.toString());
	}

}
