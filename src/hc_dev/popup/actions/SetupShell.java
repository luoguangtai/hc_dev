package hc_dev.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SetupShell extends Shell {
	String filePath = null;

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
	public SetupShell(Shell parent, final String location,
			List<IPackageFragmentRoot> list) {
		super(parent, SWT.APPLICATION_MODAL | SWT.CLOSE);
		this.setText("参数设置");
		this.setBounds(200, 200, 450, 270);

		// 文件路径
		filePath = location + File.separator + "hc_dev.properties";

		// 生成标签(数据库连接字符串)
		Label labUrl = new Label(this, SWT.RIGHT | SWT.None);
		labUrl.setBounds(0, 30, 100, 30);
		labUrl.setText("数据库URL:");
		// 生成文本框,文字居中，自动换行
		final Text textUrl = new Text(this, SWT.MULTI | SWT.LEFT | SWT.WRAP
				| SWT.BORDER);
		textUrl.setBounds(120, 20, 300, 30);

		// 生成标签（数据库用户名）,文字居中，深陷型
		Label labName = new Label(this, SWT.RIGHT | SWT.BOTTOM | SWT.None);
		labName.setBounds(0, 70, 100, 30);
		labName.setText("数据库用户名:");
		// 生成文本框,文字居中，自动换行
		final Text textName = new Text(this, SWT.MULTI | SWT.LEFT | SWT.WRAP
				| SWT.BORDER);
		textName.setBounds(120, 60, 300, 30);

		// 生成标签（数据库密码）,文字居中，深陷型
		Label labPwd = new Label(this, SWT.RIGHT | SWT.BOTTOM | SWT.None);
		labPwd.setBounds(0, 110, 100, 30);
		labPwd.setText("数据库密码:");
		// 生成文本框,文字居中，自动换行
		final Text textPwd = new Text(this, SWT.MULTI | SWT.LEFT | SWT.WRAP
				| SWT.BORDER);
		textPwd.setBounds(120, 100, 300, 30);
		// 生成标签（源代码包）,文字居中，深陷型
		Label labSrc = new Label(this, SWT.RIGHT | SWT.BOTTOM | SWT.None);
		labSrc.setBounds(0, 140, 100, 30);
		labSrc.setText("源代码目录:");
		// 生成文本框,文字居中，自动换行
		final Combo comSrc = new Combo(this, SWT.NONE);
		comSrc.setBounds(120, 130, 300, 30);

		// 生成标签（数据库密码）,文字居中，深陷型
		Label labPackage = new Label(this, SWT.RIGHT | SWT.BOTTOM | SWT.None);
		labPackage.setBounds(0, 170, 100, 30);
		labPackage.setText("代码生成包:");
		// 生成文本框,文字居中，自动换行
		final Combo comPackage = new Combo(this, SWT.NONE);
		comPackage.setBounds(120, 160, 300, 30);

		// 存放源码根目录
		List<String> listSrc = new ArrayList<String>();
		String[] strSrc = null;

		// 存放包目录
		List<String> listPackage = new ArrayList<String>();
		String[] strPackage = null;

		final Map<String, Object> mapPackage = new HashMap<String, Object>();
		try {
			// 从list<IPackageFragmentRoot> 中取出IPackageFragmentRoot对象
			for (IPackageFragmentRoot a : list) {
				// 从IPackageFragmentRoot取出IJavaElement对象，通过getChildren（）方法获取
				IJavaElement[] b = a.getChildren();
				listSrc.add(a.getElementName());
				strSrc = (String[]) listSrc.toArray(new String[listSrc.size()]);
				// mapSrc.put("src" + i, a.getElementName());
				for (IJavaElement c : b) {
					if (c.getElementName() != "") {
						listPackage.add(c.getElementName());
					}
					strPackage = (String[]) listPackage
							.toArray(new String[listPackage.size()]);
					mapPackage.put(a.getElementName(), strPackage);
				}
				listPackage.clear();
				// comPackage.setItems(strPackage);

			}

			// listSrc.clear();
			// 给源代码包下拉框赋值
			comSrc.setItems(strSrc);
			// 添加事件
			comSrc.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent arg0) {
					String key = comSrc.getText();
					String[] st = (String[]) mapPackage.get(key);
					comPackage.setItems(st);
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});

			// 给代码包添加事件
			comPackage.addMouseListener(new MouseListener() {

				public void mouseUp(MouseEvent arg0) {
					
				}

				public void mouseDown(MouseEvent arg0) {
					if (comSrc.getText() == null || comSrc.getText() == "") {
						MessageDialog.openError(null, "错误提示", "请先选择源代码包!");
					}else{
						String key = comSrc.getText();
						String[] st = (String[]) mapPackage.get(key);
						comPackage.setItems(st);
					}

				}

				public void mouseDoubleClick(MouseEvent arg0) {

				}
			});
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		// 添加确定按钮
		final Button buttonSure = new Button(this, SWT.PUSH);
		buttonSure.setBounds(340, 190, 80, 30);
		buttonSure.setText("确定");

		// 如果文件存在，则读取配置文件
		File file = new File(filePath);
		if (file.exists()) {
			try {
				InputStream in = new FileInputStream(file);
				Properties p = new Properties();
				p.load(in);
				textUrl.setText(p.getProperty("jdbc.url", ""));
				textName.setText(p.getProperty("jdbc.username", ""));
				textPwd.setText(p.getProperty("jdbc.password", ""));
				comSrc.setText(p.getProperty("src", ""));
				comPackage.setText(p.getProperty("package", ""));
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 确定按钮事件
		buttonSure.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {
			}

			public void mouseDown(MouseEvent arg0) {
				// 客户端输入值
				String url = textUrl.getText().trim();
				String username = textName.getText().trim();
				String password = textPwd.getText().trim();
				String driver = "oracle.jdbc.OracleDriver";
				String src = comSrc.getText().trim();
				String packages = comPackage.getText().trim();

				if (url.length() < 1 || username.length() < 1
						|| password.length() < 1 || src.length() < 1
						|| packages.length() < 1) {
					MessageDialog.openError(null, "错误", "参数必须填写完整");
					return;
				}

				if (url.contains("oracle"))
					driver = "oracle.jdbc.OracleDriver";
				if (url.contains("db2"))
					driver = "com.ibm.db2.jcc.DB2Driver";
				if (url.contains("sqlserver"))
					driver = "net.sourceforge.jtds.jdbc.Driver";
				if (url.contains("mysql"))
					driver = "com.mysql.jdbc.Driver";
				if (url.contains("postgresql"))
					driver = "org.postgresql.Driver";

				try {
					saveConfig("jdbc.driver", driver, "jdbc.url", url,
							"jdbc.username", username, "jdbc.password",
							password, "src", src, "package", packages);
					buttonSure.getShell().close();
				} catch (IOException e) {
					MessageDialog.openError(null, "错误",
							"参数保存失败：" + e.getMessage());
				}
			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		this.open();
	}

	/**
	 * 保存配置文件
	 * 
	 * @param path
	 * @param param
	 * @throws IOException
	 */
	public void saveConfig(String... param) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}

		Properties p = new Properties();
		for (int i = 0; i < param.length; i += 2) {
			p.setProperty(param[i], param[i + 1]);
		}

		OutputStream out = new FileOutputStream(file);
		p.store(out, null);
		out.close();
	}
}
