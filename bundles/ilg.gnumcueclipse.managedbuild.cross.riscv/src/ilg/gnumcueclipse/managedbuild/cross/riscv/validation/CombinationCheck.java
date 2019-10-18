package ilg.gnumcueclipse.managedbuild.cross.riscv.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.internal.core.Option;
import org.eclipse.cdt.ui.PreferenceConstants;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyDialogAction;

import ilg.gnumcueclipse.managedbuild.cross.riscv.ui.preferences.GlobalToolchainsPathsPreferencesPage;
import ilg.gnumcueclipse.managedbuild.cross.riscv.ui.preferences.WorkspaceToolchainsCombinatiosPreferencesPage;
import ilg.gnumcueclipse.managedbuild.cross.riscv.ui.properties.ProjectToolchainsPathPropertiesPage;

public class CombinationCheck {
	//VALIDATION
	public static final String VALIDATION_FIELD = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.validate"; 				
	//ARCHITECTURE
	public static final String ARCH_FIELD = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.isa.base";
	public static final String MULTIPLY_FIELD="ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.isa.multiply";
	public static final String ATOMIC_FIELD="ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.isa.atomic";
	public static final String COMPRESSED_FIELD="ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.isa.compressed";
	public static final String FLOATING_POINT_FIELD="ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.isa.fp";
	
	public static final String ARCH_DEFAULT = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.defaultARCH";
	public static final String ARCH_RV32I = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.rv32i";								
	public static final String ARCH_RV64I = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.rv64i";
	public static final String ARCH_RV32E = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.rv32e";
	public static final String ARCH_RV32G = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.rv32g";
	public static final String ARCH_RV64G = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.arch.rv64g";
	//INTEGER ABI
	public static final String INT_ABI_FIELD = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.abi.integer";
	public static final String INT_DEFAULT = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.integer.default";
	public static final String INT_ILP32 = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.integer.ilp32";
	public static final String INT_ILP32E = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.integer.ilp32e";
	public static final String INT_LP64 = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.integer.lp64";						  
	//FLOATING POINT ABI
	public static final String FP_ABI_FIELD = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.target.abi.fp";
	public static final String FP_ABI_NONE = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.fp.none";							  
	public static final String FP_ABI_SINGLE_PRECISION_F = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.fp.single";
	public static final String FP_ABI_DOUBLE_PRECISION_D = "ilg.gnumcueclipse.managedbuild.cross.riscv.option.abi.fp.double";
	//CURENT VALUES
	public String prev = null;
	public String actual = null;
	
	public void checkandDisplay(IHoldsOptions holder, String combinationsString, String selection)  {
	
		//Architecture options
		IOption archOption = getOptionFor(holder, ARCH_FIELD);
		IOption multiplyOption = getOptionFor(holder, MULTIPLY_FIELD);
		IOption atomicOption = getOptionFor(holder, ATOMIC_FIELD);
		IOption compressedOption = getOptionFor(holder, COMPRESSED_FIELD);
		IOption floatingPointOption = getOptionFor(holder, FLOATING_POINT_FIELD);
		
		//ABI Options
		IOption intAbiOption = getOptionFor(holder, INT_ABI_FIELD);
		IOption floatingPointAbiOption = getOptionFor(holder, FP_ABI_FIELD);
		
		try{
		String archString = new ArchStringGenerator(archOption, multiplyOption, atomicOption, compressedOption, floatingPointOption).getString();
		String abiString = new AbiStringGenerator(intAbiOption, floatingPointAbiOption).getString();
		
		String errorMessage=null;
		if ((errorMessage=checkValidationPairMultiLib(archString, abiString, combinationsString))!=null){
			
	    	displayMsgValidationError(errorMessage, holder, selection );
	
	    }
		else {
			displayMsgValidationCorrect(holder);
		}
		}
		catch(BuildException e){
			displayMsgValidationError(e.getMessage(), holder, selection);
		}
	}

private IOption getOptionFor(IHoldsOptions holder, String field) {

		return Arrays.asList(holder.getOptions()).stream().filter(candidate->candidate.getId().contains(field)).findFirst().orElse(null);
	}
	
	private String checkValidationPairMultiLib(String selectedArchitecture, String selectedAbi, String combinationsString) {
		String returnVal = null;
		
		List<String> lines2 = new ArrayList<String>(Arrays.asList(combinationsString.split(" ")));
		
		// Nothing on first line, so split the 2nd line by spaces
//		String[] validCombinations = lines2.get(1).split(" ");
		System.out.println("USER DIR: " + System.getProperty("user.dir"));

		// Split the valid combinations
		for (String validCombination : lines2) {
			String individualCombinations[] = validCombination.split("-");
			System.out.println("Valid combination elements:: " + Arrays.asList(individualCombinations));

			//Check validation here
			if (selectedArchitecture.equals(individualCombinations[0]) && selectedAbi.equals(individualCombinations[1])) {
				return returnVal;
			}
		}
		return "INVALID COMBINATION: " + selectedArchitecture + " and " + selectedAbi;
	}

	public static void displayMsgValidationError(String errorMessage, IHoldsOptions holder, String selection) {
			// MessageDialog.openError( null, "Validation Error",  errorMessage);
		
			IOption validationMessage=null;
			for (IOption testOption: holder.getOptions()){
				if (testOption.getId().contains(VALIDATION_FIELD)){                               
					validationMessage=testOption;
					break;
				}
			}
			try {
				((validationMessage)).setValue(errorMessage);
			} catch (BuildException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void displayMsgValidationCorrect(IHoldsOptions holder) {

			IOption validationMessage=null;
			
			for (IOption testOption: holder.getOptions()){
				if (testOption.getId().contains(VALIDATION_FIELD)){                               
					validationMessage=testOption;
					break;
				}
			}
			try {
				((validationMessage)).setValue("Supported combination");
			} catch (BuildException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void clearMsg(IHoldsOptions holder) {
		// TODO Auto-generated method stub
		System.out.println("ClearMsg() called");
		
		
		IOption validationMessage=null;
		for (IOption testOption: holder.getOptions()){
			if (testOption.getId().contains(VALIDATION_FIELD)){                               
				validationMessage=testOption;
				break;
			}
		}
		try {
			((validationMessage)).setValue("Validation off");
//			GlobalToolchainsPathsPreferencesPage z = new GlobalToolchainsPathsPreferencesPage();
//			z.setVisible(true);
		} catch (BuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
