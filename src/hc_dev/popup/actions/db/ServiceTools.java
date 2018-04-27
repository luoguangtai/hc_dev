package hc_dev.popup.actions.db;

import java.io.File;

public class ServiceTools {
	// 接口
	public static void createServiceIface(String pkgPath, String root_pkg, String tableName, String pkName) {
		String className = Util.tableNameToClassName(tableName);
		pkName = pkName.toLowerCase();
		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".service;\n");
		sb.append("\n");

		// import
		sb.append("import ").append("com.hc.service.IBaseService;").append("\n");
		sb.append("import ").append(root_pkg).append(".model.").append(className).append(";\n");
		// class
		sb.append("\n");
		sb.append("public interface ").append("I").append(className).append("Service extends IBaseService {\n");
		sb.append("\n");
		sb.append("}");

		Util.toFile(pkgPath + File.separator + "service", "I" + className + "Service.java", sb.toString());
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
	public static void createServiceImpl(String pkgPath, String root_pkg, String tableName, String pkName) {
		String className = Util.tableNameToClassName(tableName);
		String p = className.substring(0, 1).toLowerCase() + className.substring(1);
		pkName = pkName.toLowerCase();
		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".service.impl;\n");
		sb.append("\n");

		// import
		sb.append("import org.springframework.stereotype.Service;\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		sb.append("\n");
		sb.append("import com.hc.sys.SpringUtil;\n");
		sb.append("import com.hc.service.BaseService;\n");
		sb.append("\n");
		sb.append("import ").append(root_pkg).append(".model.").append(className).append(";\n");
		sb.append("import ").append(root_pkg).append(".dao.").append("I").append(className).append("Dao;\n");
		sb.append("import ").append(root_pkg).append(".service.").append("I").append(className).append("Service;\n");
		sb.append("\n");

		// className
		sb.append("@Service(\"").append(root_pkg).append(".service.impl.").append(className).append("Service\")\n");
		sb.append("public class ").append(className).append("Service extends BaseService implements ").append("I").append(className).append("Service {\n");

		// classBody
		// sb.append(Util.TAB).append("@Autowired\n");
		// sb.append(Util.TAB).append("private ").append("I").append(className).append("Dao ").append(p).append("Dao;\n");
		// sb.append("\n");

		// classBody
		sb.append(Util.TAB).append("private ").append("I").append(className).append("Dao ").append(p).append("Dao = (").append("I").append(className)
				.append("Dao) SpringUtil.getBean(\"").append(root_pkg).append(".dao.impl.").append(className).append("Dao\");\n");
		sb.append("\n");
		sb.append(Util.TAB).append("public ").append(className).append("Service() {\n");
		sb.append(Util.TAB).append(Util.TAB).append("baseDao = ").append(p).append("Dao;\n");
		sb.append(Util.TAB).append("}\n");
		sb.append("}");

		Util.toFile(pkgPath + File.separator + "service" + File.separator + "impl", className + "Service.java", sb.toString());
	}
}
