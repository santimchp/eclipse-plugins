package ilg.gnumcueclipse.managedbuild.cross.riscv.validation;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IOption;

public class ArchStringGenerator {

	private String archOptionValue;
	private boolean multiplyOptionValue;
	private boolean atomicOptionValue;
	private boolean compressedOption;
	private String floatingPointOption;

	public ArchStringGenerator(IOption archOption, IOption multiplyOption, IOption atomicOption,
			IOption compressedOption, IOption floatingPointOption) throws BuildException {

		this.archOptionValue = archOption.getValue().toString();
		this.multiplyOptionValue = multiplyOption.getBooleanValue();
		this.atomicOptionValue = atomicOption.getBooleanValue();
		this.compressedOption = compressedOption.getBooleanValue();
		this.floatingPointOption = floatingPointOption.getValue().toString();
	}

	public String getString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(getArchOptionValueFromString(archOptionValue));
		builder.append(multiplyOptionValue?"m":"");
		builder.append(atomicOptionValue?"a":"");
		builder.append(getFloatingPointOptionFromString(floatingPointOption));
		builder.append(compressedOption?"c":"");
		
		return builder.toString();
	}
	
	private Object getFloatingPointOptionFromString(String floatingPointOption2) {
		String selectedOption=getLastDotOf(floatingPointOption2);
		
		switch(selectedOption){
		case "none":
			return "";
		case "single":
			return "f";
		case "double":
			return "fd";
		case "quad":
			return "fdq";
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
