package ilg.gnumcueclipse.managedbuild.cross.riscv.ui.preferences;

import java.util.regex.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;

/**
 * Class to validate Multilib String combination supported. Using regular
 * expression in String.
 */

public class MultilibStringValidation {
	static boolean result = true;
	static String syntax = "(?:rv(?:32i|32e|32g|64i|64g)(?:m|a|c|ma|ac|mc|mac|)(?:f|fd|fdq|)(?:c|)-(?:ilp32|ilp32e|lp64)(?:f|d|)--( |$))+";

	// returns true if the string matches exactly "true"
	public static boolean isTrue(String combinationsString) {
		result = true;
		
		if (combinationsString.length() != 0) {
			result = combinationsString.matches(syntax);
		}
		return result;
	}
}