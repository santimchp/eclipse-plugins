/*******************************************************************************
 * Copyright (c) 2015 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial implementation.
 *******************************************************************************/

package ilg.gnumcueclipse.managedbuild.cross.riscv.ui.preferences;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ilg.gnumcueclipse.core.EclipseUtils;
import ilg.gnumcueclipse.core.ui.LabelFakeFieldEditor;
import ilg.gnumcueclipse.core.ui.StringVariableFieldEditor;
import ilg.gnumcueclipse.core.ui.XpackDirectoryNotStrictFieldEditor;
import ilg.gnumcueclipse.managedbuild.cross.preferences.DefaultPreferences;
import ilg.gnumcueclipse.managedbuild.cross.preferences.PersistentPreferences;
import ilg.gnumcueclipse.managedbuild.cross.riscv.Activator;
import ilg.gnumcueclipse.managedbuild.cross.riscv.Option;
import ilg.gnumcueclipse.managedbuild.cross.riscv.ui.Messages;
import ilg.gnumcueclipse.managedbuild.cross.riscv.validation.CombinationCheck;
import ilg.gnumcueclipse.managedbuild.cross.riscv.validation.ValidationImplementor;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class GlobalToolchainsPathsPreferencesPage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	// ------------------------------------------------------------------------

	public static final String ID = "ilg.gnumcueclipse.managedbuild.cross.riscv.preferencePage.globalToolchainsPaths";

	// ------------------------------------------------------------------------

	private PersistentPreferences fPersistentPreferences;
	private DefaultPreferences fDefaultPreferences;

	// ------------------------------------------------------------------------

	public GlobalToolchainsPathsPreferencesPage() {
		super(GRID);

		fPersistentPreferences = Activator.getInstance().getPersistentPreferences();
		fDefaultPreferences = new DefaultPreferences(Activator.PLUGIN_ID);

		setPreferenceStore(new ScopedPreferenceStore(ConfigurationScope.INSTANCE, Activator.PLUGIN_ID));

		setDescription(Messages.GlobalToolchainsPathsPreferencesPage_description);
	}

	// ------------------------------------------------------------------------

	// Contributed by IWorkbenchPreferencePage
	@Override
	public void init(IWorkbench workbench) {
		if (Activator.getInstance().isDebugging()) {
			System.out.println("riscv.GlobalToolchainsPathsPreferencesPage.init()");
		}
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI
	 * blocks needed to manipulate various types of preferences. Each field editor
	 * knows how to save and restore itself.
	 */

	@Override
	protected void createFieldEditors() {
		boolean isStrict;

		FieldEditor toolchainNameField = new ToolchainsFieldEditor(PersistentPreferences.TOOLCHAIN_NAME_KEY,
				Messages.ToolchainName_label, getFieldEditorParent());
		addField(toolchainNameField);

		Set<String> toolchainNames = new HashSet<String>();

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; ++i) {
			IConfiguration[] configs = EclipseUtils.getConfigurationsForProject(projects[i]);
			if (configs != null) {
				for (int j = 0; j < configs.length; ++j) {
					IToolChain toolchain = configs[j].getToolChain();
					if (toolchain == null) {
						continue;
					}
					IOption option = toolchain.getOptionBySuperClassId(Option.OPTION_TOOLCHAIN_NAME);
					if (option == null) {
						continue;
					}
					try {
						String name = option.getStringValue();
						if (!name.isEmpty()) {
							toolchainNames.add(name);
						}
					} catch (BuildException e) {
						;
					}
				}
			}
		}

		if (toolchainNames.isEmpty()) {
			toolchainNames.add(fPersistentPreferences.getToolchainName());
		}

		for (String toolchainName : toolchainNames) {

			FieldEditor labelField = new LabelFakeFieldEditor(toolchainName, Messages.ToolsPaths_ToolchainName_label,
					getFieldEditorParent());
			addField(labelField);

			isStrict = fDefaultPreferences.getBoolean(PersistentPreferences.GLOBAL_TOOLCHAIN_PATH_STRICT, true);

			String xpackName = fDefaultPreferences.getToolchainXpackName(toolchainName);

			String key = PersistentPreferences.getToolchainKey(toolchainName);
			FieldEditor toolchainPathField = new XpackDirectoryNotStrictFieldEditor(xpackName, key,
					Messages.ToolchainPaths_label, getFieldEditorParent(), isStrict);

			addField(toolchainPathField);
		}
		
		//---Sanity check multilib combinations------------------------------
		    
		// StringVariableFieldEditor(String name, String variableName, String, variableDescription, String labelText,Composite parent)
		FieldEditor labelField2 = new StringVariableFieldEditor("combinationsSet", "globalMultilibsCombinations", "description", "Supported combinations: ", getFieldEditorParent());
		labelField2.setEnabled(true, getFieldEditorParent());
//		labelField2.fillIntoGrid(getFieldEditorParent(), 1);
		addField(labelField2);
		
		//-------------------------------------------------------------------
	}

	// ------------------------------------------------------------------------
}
