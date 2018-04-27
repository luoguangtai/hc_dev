package hc_dev.popup.actions.db;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

public class Column {
	private String colName;
	private String colType;
	private String colComment;
	private String colIsNull;
	private int colPrecision;
	private int colScale;

	public Column(String colName, String colType, String colComment, String isNullable, int colPrecision, int colScale) {
		this.colName = colName;
		this.colComment = colComment;
		this.colType = colType;
		this.colIsNull = colIsNull;
		this.colPrecision = colPrecision;
		this.colScale = colScale;
	}

	@SuppressWarnings("rawtypes")
	public Class getJavaType() {
		if (this.colType.equals("VARCHAR2") || this.colType.equals("CHAR") || this.colType.equals("CLOB") || this.colType.equals("char")
				|| this.colType.equals("varchar") || this.colType.equals("text")) {
			return String.class;
		} else if (this.colType.equals("int")) {
			return Integer.class;
		} else if (this.colType.equals("bigint")) {
			return Long.class;
		} else if (this.colType.equals("float")) {
			return Float.class;
		} else if (this.colType.equals("double")) {
			return Double.class;
		} else if (this.colType.equals("NUMBER")) {
			if (this.colScale == 0) {
				if (this.colPrecision > 9) {
					return Long.class;
				} else {
					return Integer.class;
				}
			} else {
				return Double.class;
			}
		} else if (this.colType.equals("DATE")) {
			return Date.class;
		} else if (this.colType.equals("TIMESTMP")) {
			return Timestamp.class;
		} else if (this.colType.equals("BLOB")) {
			return InputStream.class;
		} else {
			return String.class;
		}
	}

	public String getJavaColName() {
		return Util.filedNameToJavaName(colName);
	}

	public String getColName() {
		return colName;
	}

	public String getColComment() {
		return colComment;
	}

}
