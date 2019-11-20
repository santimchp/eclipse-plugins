package ilg.gnumcueclipse.managedbuild.cross.riscv.validation;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.bind.ValidationEventHandler;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IOptionApplicability;
import org.eclipse.cdt.managedbuilder.internal.core.BuildObject;
import org.eclipse.cdt.managedbuilder.internal.core.Option;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.Preferences;
import org.eclipse.osgi.service.datalocation.Location;

public class ValidationImplementor implements IOptionApplicability {

	public static final String VALIDATION_SELECTOR_FIELD = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.checkboxval";
//	public static final String VALIDATION_SELECTOR_FIELD = ".*ilg\\.gnumcueclipse\\.managedbuild\\.cross\\.riscv\\.option\\.target\\.validateselector3\\..*";	

	@Override
	public boolean isOptionUsedInCommandLine(IBuildObject configuration, IHoldsOptions holder, IOption option) {
		return false;
	}

	@Override
	public boolean isOptionVisible(IBuildObject configuration, IHoldsOptions holder, IOption option) {
		return true;
	}

	@Override
	public boolean isOptionEnabled(IBuildObject configuration, IHoldsOptions holder, IOption option) {

		boolean returnValue = true;
			
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getSite().get

//			IOption validateOption = Arrays.asList(holder.getOptions()).stream().filter(candidate->candidate.getId().contains(VALIDATION_FIELD)).findFirst().orElse(null);
		IOption validateSelectorOption = Arrays.asList(holder.getOptions()).stream()
				.filter(candidate -> candidate.getId().contains(VALIDATION_SELECTOR_FIELD)).findFirst().orElse(null);
		
		// path (get the combination entered in Workspace scope - eclipse properties)
		String combinationsString = Platform.getPreferencesService().getRootNode().node(InstanceScope.SCOPE)
				.node("ilg.gnumcueclipse.managedbuild.cross.riscv").get("combinationsSet", "not found");
		System.out.println("Value is: " + combinationsString);
		
		
		//=======================================================Enable tick selection from Combination preferences page=======================================================
//		String EnableValidation = Platform.getPreferencesService().getRootNode().node(InstanceScope.SCOPE)
//				.node("ilg.gnumcueclipse.managedbuild.cross.riscv").get("EnableValidation", "not found");
//
//		System.out.println("EnableValidation Value is:::::::::::::::::::::::::::::::::::::::::::::::::::::: " + EnableValidation.toString());
//		
//		if (EnableValidation.equals("true")){
//			try {
//				validateSelectorOption.setValue(true);
//			} catch (BuildException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		else if (EnableValidation.equals("not found")){
//			try {
//				validateSelectorOption.setValue(false);
//			} catch (BuildException e) {
//				e.printStackTrace();
//			}
//		}
		
		//=====================================================================================================================================================================

		if (validateSelectorOption.getValue().equals(true) ) {
			new CombinationCheck().checkandDisplay(holder, combinationsString);
		}
		else if (validateSelectorOption.getValue().equals(false) ) {
			new CombinationCheck().clearMsg(holder);
		}

		return returnValue;
	}
}










