package hc_dev.popup.actions;

import hc_dev.popup.actions.db.Column;
import hc_dev.popup.actions.db.DBUtil;
import hc_dev.popup.actions.db.SourceHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class JavaGeneratorShell extends Shell {
	String filePath = null;
	private Text packText;
	private Table table = null;
	private IJavaProject project = null;

	@Override
	protected void checkSubclass() {
	}

	/**
	 * 定义一个窗体
	 * 
	 * @param parent
	 *            shell对象
	 * @param location
	 *            文件存放位置
	 */
	public JavaGeneratorShell(Shell parent, final String jdbcDriver, final String jdbcUrl, final String jdbcUsername, final String jdbcPassword,
			final String location, final String src, final String pkg, final IJavaProject project) {
		super(parent, SWT.APPLICATION_MODAL | SWT.CLOSE);
		this.setText("生成Java代码");
		this.setBounds(300, 100, 560, 580);
		this.project = project;
		try {
			Label packLabel = new Label(this, SWT.LEFT | SWT.BOTTOM | SWT.None);
			packLabel.setText("请选择组件目录:");
			packLabel.setBounds(10, 8, 100, 25);
			packText = new Text(this, SWT.MULTI | SWT.LEFT | SWT.WRAP | SWT.BORDER);
			packText.setBounds(110, 5, 230, 25);
			final Button packButtonSure = new Button(this, SWT.PUSH);
			packButtonSure.setText("选择");
			packButtonSure.setBounds(345, 5, 80, 25);

			// 选择包按钮事件
			packButtonSure.addMouseListener(new MouseListener() {
				public void mouseUp(MouseEvent arg0) {
				}

				public void mouseDown(MouseEvent arg0) {
					try {
						IJavaProject projectA = JavaGeneratorShell.this.project;
						// projectA.getProject().getWorkspace().getRoot().getProject()
						ContainerSelectionDialog dialog = new ContainerSelectionDialog(JavaGeneratorShell.this.getShell(), projectA.getProject().getFolder(
								"src/com/"), false, "请选择组件目录!");
						if (dialog.open() == ContainerSelectionDialog.OK) {
							Object[] result = dialog.getResult();
							if (result.length == 1) {
								String path = ((Path) result[0]).toString();
								JavaGeneratorShell.this.packText.setText(path);
							}
						}
					} catch (Exception e) {
						MessageDialog.openError(null, "错误", e.getMessage());
					}
				}

				public void mouseDoubleClick(MouseEvent arg0) {
				}
			});

			Label dbTableLabel = new Label(this, SWT.LEFT | SWT.BOTTOM | SWT.None);
			dbTableLabel.setText("要过滤的表前缀:");
			dbTableLabel.setBounds(10, 38, 100, 25);
			final Text dbTableText = new Text(this, SWT.MULTI | SWT.LEFT | SWT.WRAP | SWT.BORDER);
			dbTableText.setBounds(110, 35, 230, 25);
			final Button searchButtonSure = new Button(this, SWT.PUSH);
			searchButtonSure.setText("过滤");
			searchButtonSure.setBounds(345, 35, 80, 25);
			// 数据表表格
			table = new Table(this, SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
			table.setHeaderVisible(true);// 设置显示表头
			table.setLinesVisible(true);// 设置显示表格线

			TableColumn tableColumn1 = new TableColumn(table, SWT.LEFT);
			tableColumn1.setWidth(260);
			tableColumn1.setText("数据库表");
			tableColumn1.setMoveable(false);
			TableColumn tableColumn2 = new TableColumn(table, SWT.LEFT);
			tableColumn2.setWidth(280);
			tableColumn2.setText("数据库表描述");
			tableColumn2.setMoveable(false);
			table.setBounds(10, 70, 540, 430);
			JavaGeneratorShell.this.refreshTableData(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, "");
			// 过滤按钮事件
			searchButtonSure.addMouseListener(new MouseListener() {
				public void mouseUp(MouseEvent arg0) {
				}

				public void mouseDown(MouseEvent arg0) {
					try {
						String tableV = dbTableText.getText().trim().toUpperCase().trim();
						JavaGeneratorShell.this.refreshTableData(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, tableV);
					} catch (Exception e) {
						MessageDialog.openError(null, "错误", e.getMessage());
					}
				}

				public void mouseDoubleClick(MouseEvent arg0) {
				}
			});

			Label file = new Label(this, SWT.LEFT | SWT.BOTTOM | SWT.None);
			file.setBounds(10, 515, 70, 30);
			file.setText("生成文件:");
			final Button btnMODEL = new Button(this, SWT.CHECK);
			btnMODEL.setText("model");
			btnMODEL.setSelection(true);
			btnMODEL.setBounds(80, 510, 70, 30);
			final Button btnACTION = new Button(this, SWT.CHECK);
			btnACTION.setText("action");
			btnACTION.setSelection(true);
			btnACTION.setBounds(150, 510, 70, 30);
			final Button btnSERVICE = new Button(this, SWT.CHECK);
			btnSERVICE.setText("service");
			btnSERVICE.setSelection(true);
			btnSERVICE.setBounds(220, 510, 70, 30);
			final Button btnDao = new Button(this, SWT.CHECK);
			btnDao.setText("dao");
			btnDao.setSelection(true);
			btnDao.setBounds(290, 510, 70, 30);

			final Button buttonSure = new Button(this, SWT.PUSH);
			buttonSure.setBounds(410, 510, 60, 30);
			buttonSure.setText("确定");

			// 确定按钮事件
			buttonSure.addMouseListener(new MouseListener() {
				public void mouseUp(MouseEvent arg0) {
				}

				public void mouseDown(MouseEvent arg0) {
					List<String> dbTableList = new ArrayList<String>();
					for (TableItem allItem : table.getItems()) {
						if (allItem.getChecked()) {
							dbTableList.add(allItem.getText(0));
						}
					}
					if (dbTableList.size() <= 0) {
						MessageDialog.openError(null, "错误", "请选择数据库表");
						return;
					}

					boolean makeDAO = btnDao.getSelection();
					boolean makeMODEL = btnMODEL.getSelection();
					boolean makeSERVICE = btnSERVICE.getSelection();
					boolean makeACTION = btnACTION.getSelection();

					if (!makeDAO && !makeMODEL && !makeSERVICE && !makeACTION) {
						MessageDialog.openError(null, "错误", "不生成文件你想干嘛。。。");
						return;
					}

					SourceHelper sh = new SourceHelper();
					sh.setMakeMODEL(makeMODEL);
					sh.setMakeACTION(makeACTION);
					sh.setMakeSERVICE(makeSERVICE);
					sh.setMakeDAO(makeDAO);
					// 代码路径
					sh.setLocationPath(location);
					String srcPath = JavaGeneratorShell.this.packText.getText();
					if (srcPath.indexOf("/src/") <= -1) {
						MessageDialog.openError(null, "错误", "组件目录需要包含/src/下");
					}
					sh.setSrcPath(JavaGeneratorShell.this.packText.getText());
					try {
						DBUtil.init(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
						for (String dbTable : dbTableList) {
							// 读取表结构
							String sql = "";
							if (jdbcUrl.contains("oracle")) {
								sql += " SELECT A.COLUMN_NAME AS COL_NAME, ";
								sql += "        A.DATA_TYPE AS COL_TYPE, ";
								sql += "        NVL(B.COMMENTS, A.COLUMN_NAME) AS COL_COMMENT, ";
								sql += "        A.NULLABLE AS COL_ISNULL, ";
								sql += "        A.DATA_PRECISION AS COL_PRECISION, ";
								sql += "        A.DATA_SCALE AS COL_SCALE ";
								sql += "   FROM USER_TAB_COLUMNS A, USER_COL_COMMENTS B ";
								sql += "  WHERE A.TABLE_NAME = '" + dbTable + "' ";
								sql += "    AND A.TABLE_NAME = B.TABLE_NAME ";
								sql += "    AND A.COLUMN_NAME = B.COLUMN_NAME ";
								sql += "  ORDER BY A.COLUMN_ID ASC ";
							} else if (jdbcUrl.contains("mysql")) {
								String[] jdbcUrlArr = jdbcUrl.split("/");
								String dataBaseName = jdbcUrlArr[jdbcUrlArr.length - 1];
								sql += " SELECT A.COLUMN_NAME AS COL_NAME ";
								sql += "        ,A.DATA_TYPE AS COL_TYPE ";
								sql += "        ,CASE WHEN LENGTH(A.COLUMN_COMMENT)=0 THEN A.COLUMN_NAME ELSE A.COLUMN_COMMENT END AS COL_COMMENT ";
								sql += "        ,A.IS_NULLABLE AS COL_ISNULL ";
								sql += "        ,A.NUMERIC_PRECISION AS COL_PRECISION ";
								sql += "        ,A.NUMERIC_SCALE AS COL_SCALE ";
								sql += "   FROM information_schema.COLUMNS A ";
								sql += "  WHERE A.TABLE_NAME = '" + dbTable + "' ";
								sql += "    AND A.TABLE_SCHEMA='" + dataBaseName + "' ";
								sql += "  ORDER BY A.ORDINAL_POSITION ASC ";
							}
							ResultSet rs = DBUtil.executeQuery(sql);
							List<Column> colList = new ArrayList<Column>();
							while (rs.next()) {
								colList.add(new Column(rs.getString("COL_NAME"), rs.getString("COL_TYPE"), rs.getString("COL_COMMENT"), rs
										.getString("COL_ISNULL"), rs.getInt("COL_PRECISION"), rs.getInt("COL_SCALE")));
							}
							sh.setMainTable(dbTable);
							sh.setDbTableColList(colList);
							sh.make();
						}

						buttonSure.getShell().close();
						// 刷新代码包
						IPackageFragmentRoot[] pkgs = project.getPackageFragmentRoots();
						for (IPackageFragmentRoot t : pkgs) {
							if (!t.isArchive() && t.getElementName().equals(src)) {
								t.getResource().refreshLocal(IResource.DEPTH_INFINITE, null);
							}
						}
					} catch (SQLException e) {
						MessageDialog.openError(null, "错误", e.getMessage());
					} catch (Exception e) {
						MessageDialog.openError(null, "错误", e.getMessage());
					} finally {
						DBUtil.close();
					}
				}

				public void mouseDoubleClick(MouseEvent arg0) {

				}
			});
			this.open();
		} catch (Exception e) {
			MessageDialog.openError(null, "错误", e.getMessage());
		}
	}

	private void refreshTableData(final String jdbcDriver, final String jdbcUrl, final String jdbcUsername, final String jdbcPassword, String tableV) {
		try {
			table.removeAll();
			DBUtil.init(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
			String tableSql = "";
			if (jdbcUrl.contains("oracle")) {
				tableSql += " SELECT A.TABLE_NAME,B.COMMENTS  ";
				tableSql += " FROM USER_TABLES A,USER_TAB_COMMENTS B  ";
				tableSql += " WHERE A.TABLE_NAME=B.TABLE_NAME  ";
				if (tableV.length() > 0) {
					tableSql += " AND A.TABLE_NAME LIKE '" + tableV + "%'";
				}
			} else if (jdbcUrl.contains("mysql")) {
				String[] jdbcUrlArr = jdbcUrl.split("/");
				String dataBaseName = jdbcUrlArr[jdbcUrlArr.length - 1];
				tableSql += " SELECT table_name AS TABLE_NAME,table_comment AS COMMENTS  ";
				tableSql += " FROM information_schema.TABLES  ";
				tableSql += " WHERE table_schema = '" + dataBaseName + "'  ";
				if (tableV.length() > 0) {
					tableSql += " AND table_name LIKE '" + tableV + "%'";
				}
			} else if (jdbcUrl.contains("db2")) {
				// 暂不支持
			} else if (jdbcUrl.contains("sqlserver")) {
				// 暂不支持
			}
			tableSql += " ORDER BY TABLE_NAME ASC ";
			ResultSet rs = DBUtil.executeQuery(tableSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				TableItem item = new TableItem(table, SWT.LEFT);
				String[] tableArr = new String[2];
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					tableArr[i - 1] = rs.getString(i);
				}
				item.setText(tableArr);
			}
		} catch (SQLException e) {
			MessageDialog.openError(null, "错误", e.getMessage());
		} catch (Exception e) {
			MessageDialog.openError(null, "错误", e.getMessage());
		} finally {
			DBUtil.close();
		}
	}
}
