package hc_dev.popup.actions.db;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 创建java文件
 */
public class DomainTools {
	public static void createDomain(String pkgPath, String root_pkg, String tableName, List<Column> colList) {
		String className = Util.tableNameToClassName(tableName);
		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package ").append(root_pkg).append(".model;\n");
		sb.append("import javax.persistence.Column;\n");
		sb.append("import javax.persistence.Entity;\n");
		sb.append("import javax.persistence.GeneratedValue;\n");
		sb.append("import javax.persistence.Id;\n");
		sb.append("import javax.persistence.Table;\n");
		sb.append("import javax.persistence.Transient;\n");
		sb.append("import org.hibernate.annotations.GenericGenerator;\n");
		sb.append("import com.hc.model.BaseModel;\n");
		sb.append("\n");
		sb.append(addImport(colList));
		sb.append("\n");

		// class
		sb.append("@Entity\n");
		sb.append("@Table(name = \"" + tableName.toUpperCase() + "\")\n");
		sb.append("public class ").append(className).append(" extends BaseModel implements Serializable{\n");
		sb.append(Util.TAB + "private static final long serialVersionUID = 1L;\n\n");
		// 变量
		for (int i = 0; i < colList.size(); i++) {
			String dataColName = colList.get(i).getColName().toUpperCase();
			String javaColName = colList.get(i).getJavaColName();
			String javaColType = colList.get(i).getJavaType().getSimpleName();
			String colNameFirstUpper = javaColName.substring(0, 1).toLowerCase() + javaColName.substring(1);
			String colComment = colList.get(i).getColComment();
			sb.append("\n");
			if (i == 0) {
				sb.append(Util.TAB + "@Id\n");
				sb.append(Util.TAB + "@Column(name = \"" + dataColName + "\")\n");
				sb.append(Util.TAB + "@GeneratedValue(generator = \"" + colNameFirstUpper + "\")\n");
				sb.append(Util.TAB + "@GenericGenerator(name = \"" + colNameFirstUpper + "\", strategy = \"uuid\")\n");
				sb.append(Util.TAB + "private ").append(javaColType).append(" ").append(javaColName).append(";\n");
			} else {
				sb.append(Util.TAB + "/**\n");
				sb.append(Util.TAB + " * " + colComment + "\n");
				sb.append(Util.TAB + " */\n");
				sb.append(Util.TAB + "@Column(name = \"" + dataColName + "\")\n");
				sb.append(Util.TAB + "private ").append(javaColType).append(" ").append(javaColName).append(";\n");
			}
		}

		// set & get
		for (int i = 0; i < colList.size(); i++) {
			sb.append("\n");
			sb.append(addSetMethod(colList.get(i)));
			sb.append(addGetMethod(colList.get(i)));
		}

		// 结束
		sb.append("}");

		Util.toFile(pkgPath + File.separator + "model", className + ".java", sb.toString());
	}

	private static String addImport(List<Column> colList) {
		String t = "import java.io.Serializable;\n";
		for (int i = 0; i < colList.size(); i++) {
			if (colList.get(i).getJavaType().equals(Date.class) && t.indexOf(Date.class.getName()) < 0) {
				t += "import " + Date.class.getName() + ";\n";
			} else if (colList.get(i).getJavaType().equals(Timestamp.class) && t.indexOf(Timestamp.class.getName()) < 0) {
				t += "import " + Timestamp.class.getName() + ";\n";
			} else if (colList.get(i).getJavaType().equals(BigDecimal.class) && t.indexOf(BigDecimal.class.getName()) < 0) {
				t += "import " + BigDecimal.class.getName() + ";\n";
			} else if (colList.get(i).getJavaType().equals(Time.class) && t.indexOf(Time.class.getName()) < 0) {
				t += "import " + Time.class.getName() + ";\n";
			} else if (colList.get(i).getJavaType().equals(InputStream.class) && t.indexOf(InputStream.class.getName()) < 0) {
				t += "import " + InputStream.class.getName() + ";\n";
			}
		}
		return t;
	}

	private static String addSetMethod(Column col) {
		String fieldName = col.getJavaColName();
		// 如果第二个字母是大写，则get set方法上第一个字母小写
		String setMethod = "set";
		setMethod += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		StringBuilder sb = new StringBuilder();
		sb.append(Util.TAB + "public void ").append(setMethod).append("(").append(col.getJavaType().getSimpleName()).append(" ").append(col.getJavaColName())
				.append("){\n");
		sb.append(Util.TAB + Util.TAB + "this.").append(col.getJavaColName()).append(" = ").append(col.getJavaColName()).append(";\n");
		sb.append(Util.TAB + "}\n");
		return sb.toString();
	}

	private static String addGetMethod(Column col) {
		String fieldName = col.getJavaColName();
		// 如果第二个字母是大写，则get set方法上第一个字母小写
		String getMethod = "get";
		getMethod += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		StringBuilder sb = new StringBuilder();
		sb.append(Util.TAB + "public ").append(col.getJavaType().getSimpleName()).append(" ").append(getMethod).append("(){\n");
		sb.append(Util.TAB + Util.TAB + "return this.").append(col.getJavaColName()).append(";\n");
		sb.append(Util.TAB + "}\n");
		return sb.toString();
	}

}
