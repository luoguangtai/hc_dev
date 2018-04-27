package hc_dev.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class JavaGeneratorAction implements IObjectActionDelegate {

	private Shell shell;
	private IJavaProject project = null;
	
	/**
	 * Constructor for Action1.
	 */
	public JavaGeneratorAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		String rootpath = project.getResource().getLocation().toString();
		String filePath = rootpath + File.separator + "hc_dev.properties";
		File file = new File(filePath);
		if(!file.exists()){
			MessageDialog.openError(null, "错误", "未找到配置文件，需要先设置参数");
			return;
		}
		
		try {
			InputStream in = new FileInputStream(file);
			Properties p = new Properties();
			p.load(in);
			String url = p.getProperty("jdbc.url", "").trim();
			String username = p.getProperty("jdbc.username", "").trim();
			String password = p.getProperty("jdbc.password", "").trim();
			String driver = p.getProperty("jdbc.driver", "").trim();
			String src = p.getProperty("src", "").trim();
			String pkg = p.getProperty("package", "").trim();
			in.close();
			
			if(url.length()<1 || username.length()<1 || password.length()<1 || driver.length()<1 || src.length()<1 || pkg.length()<1){
				MessageDialog.openError(null, "错误", "配置信息不全");
			}
			else{
				new JavaGeneratorShell(shell, driver, url, username, password, rootpath, src, pkg, project);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection structSelection = (StructuredSelection) selection;
			Object object = structSelection.getFirstElement();
			if (object instanceof IJavaProject) {
				project = (IJavaProject) object;
			}
			else if(object instanceof IProject){
				project  = JavaCore.create((IProject)object);
			}
		}
	}

}
