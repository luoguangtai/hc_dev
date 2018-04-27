package hc_dev.popup.actions.db;

import java.io.File;

public class ActionTools {

	public static void createAction(String pkgPath, String root_pkg, String tableName, String pkName) {
		String className = Util.tableNameToClassName(tableName);
		String p = className.substring(0, 1).toLowerCase() + className.substring(1);

		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".action;\n");
		sb.append("\n");
		sb.append("import org.springframework.stereotype.Controller;\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		sb.append("\n");
		sb.append("import com.hc.sys.SpringUtil;\n");
		sb.append("import com.hc.action.BaseAction;\n");
		sb.append("import ").append(root_pkg).append(".service.").append("I").append(className).append("Service;\n");
		sb.append("\n");
		sb.append("@Controller\n");
		sb.append("public class ").append(className).append("Action extends BaseAction{\n");
		// service
		sb.append(Util.TAB).append("private ").append("I").append(className).append("Service ").append(p).append("Service = (").append("I").append(className)
				.append("Service) SpringUtil.getBean(\"").append(root_pkg).append(".service.impl.").append(className).append("Service\");\n");

		sb.append("}");

		Util.toFile(pkgPath + File.separator + "action", className + "Action.java", sb.toString());
	}

}
