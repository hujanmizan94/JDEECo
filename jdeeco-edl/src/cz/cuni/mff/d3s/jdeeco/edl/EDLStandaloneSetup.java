/*
* generated by Xtext
*/
package cz.cuni.mff.d3s.jdeeco.edl;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class EDLStandaloneSetup extends EDLStandaloneSetupGenerated{

	public static void doSetup() {
		new EDLStandaloneSetup().createInjectorAndDoEMFRegistration();
	}	
}

