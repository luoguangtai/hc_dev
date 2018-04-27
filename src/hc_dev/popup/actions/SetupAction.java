package hc_dev.popup.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class SetupAction implements IObjectActionDelegate {

	private Shell shell;
	private IJavaProject project = null;

	/**
	 * Constructor for Action1.
	 */
	public SetupAction() {
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
		try {
			IPackageFragmentRoot[] x = project.getAllPackageFragmentRoots();
			// 用于存放源文件目录
			List<IPackageFragmentRoot> listsrc = new ArrayList<IPackageFragmentRoot>();
			for (IPackageFragmentRoot y : x) {
				if (!y.isArchive()) {
					listsrc.add(y);
				}
			}
			if (listsrc.isEmpty()) {
				MessageDialog.openError(null, "错误", "没有源代码目录");
			} else {
				new SetupShell(shell, rootpath, listsrc);
			}
		} catch (JavaModelException e) {
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
