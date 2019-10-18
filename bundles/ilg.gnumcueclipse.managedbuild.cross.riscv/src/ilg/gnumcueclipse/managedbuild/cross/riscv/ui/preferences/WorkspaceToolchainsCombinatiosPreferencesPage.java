package ilg.gnumcueclipse.managedbuild.cross.riscv.ui.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ilg.gnumcueclipse.core.preferences.ScopedPreferenceStoreWithoutDefaults;
import ilg.gnumcueclipse.core.ui.StringVariableFieldEditor;
import ilg.gnumcueclipse.managedbuild.cross.preferences.DefaultPreferences;
import ilg.gnumcueclipse.managedbuild.cross.preferences.PersistentPreferences;
import ilg.gnumcueclipse.managedbuild.cross.riscv.Activator;
import ilg.gnumcueclipse.managedbuild.cross.riscv.ui.Messages;


public class WorkspaceToolchainsCombinatiosPreferencesPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static final String ID = "ilg.gnumcueclipse.managedbuild.cross.riscv.preferencePage.workspaceToolchainsCombinations";

	private PersistentPreferences fPersistentPreferences;
	private DefaultPreferences fDefaultPreferences;

	public WorkspaceToolchainsCombinatiosPreferencesPage() {
		super(GRID);

		fPersistentPreferences = Activator.getInstance().getPersistentPreferences();
		fDefaultPreferences = new DefaultPreferences(Activator.PLUGIN_ID);

		setPreferenceStore(new ScopedPreferenceStoreWithoutDefaults(InstanceScope.INSTANCE, Activator.PLUGIN_ID));
		setDescription(Messages.WorkspaceToolchainsCombinationsPreferencesPage_description);
		
	}

	@Override
	public void init(IWorkbench workbench) {
		if (Activator.getInstance().isDebugging()) {
			System.out.println("riscv.WorkspaceToolchainsCombinationsPreferencesPage.init()");
		}
	}

	@Override
	protected void createFieldEditors() {
		// StringVariableFieldEditor(String name, String variableName, String, variableDescription, String labelText,Composite parent)
		FieldEditor labelField = new StringVariableFieldEditor("combinationsSet", "variableName", "description", "Supported combinatios: ", getFieldEditorParent());
		labelField.setEnabled(true, getFieldEditorParent());
		addField(labelField);
		
	}
}
