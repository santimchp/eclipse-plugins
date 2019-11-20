package ilg.gnumcueclipse.managedbuild.cross.riscv.validation;

import java.util.Arrays;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IOptionApplicability;

public class ValidationMsg implements IOptionApplicability {


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
		
		IOption val = Arrays.asList(holder.getOptions()).stream()
				.filter(candidate -> candidate.getId().contains(VALIDATION_SELECTOR_FIELD)).findFirst().orElse(null);
		
		if (val.getValue().equals(false) ) {
			returnValue= false;
		}
		
		return returnValue;
	}
}
