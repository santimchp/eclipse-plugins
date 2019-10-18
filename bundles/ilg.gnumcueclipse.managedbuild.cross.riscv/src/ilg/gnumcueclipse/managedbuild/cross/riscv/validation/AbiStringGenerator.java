package ilg.gnumcueclipse.managedbuild.cross.riscv.validation;

import org.eclipse.cdt.managedbuilder.core.IOption;

public class AbiStringGenerator {

	private String integerAbiValue;
	private String floatingPointAbiValue;

	public AbiStringGenerator(IOption intAbiOption, IOption floatingPointAbiOption) {

		this.integerAbiValue = intAbiOption.getValue().toString();
		this.floatingPointAbiValue= floatingPointAbiOption.getValue().toString();
	}

	public String getString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getLastDotOf(integerAbiValue));
		builder.append(getFloatingPointOptionFromString(floatingPointAbiValue));
		return builder.toString();
	}
	
	private Object getFloatingPointOptionFromString(String floatingPointOption2) {
		
		String selectedOption=getLastDotOf(floatingPointOption2);
		
		switch(selectedOption)
		{
		case "none":
			return "";
		case "single":
			return "f";
		case "double":
			return "d";
		
			default:
				return "";
		}
	}

	private String getArchOptionValueFromString(String archOptionValue2) {
		return getLastDotOf(archOptionValue2);
	}

	private static String getLastDotOf(String string) {
		String bitsOfString[]=string.split("\\.");
		return bitsOfString[bitsOfString.length-1];
	}

}
