/*******************************************************************************
 * Copyright 2017 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.adaptation.modeswitchprops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.modes.DEECoModeChart;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationUtility;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.Component;
import cz.cuni.mff.d3s.metaadaptation.modeswitchprops.ModeSwitchPropsManager;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeSwitchPropsPlugin implements DEECoPlugin, StartupListener {

	private final Map<Class<?>, AdaptationUtility> utilities;

	private AdaptationPlugin adaptationPlugin = null;
	private DEECoContainer container = null;
	private ModeSwitchPropsManager manager = null;	

	private boolean verbose = false;
	private boolean training = false;
	private String trainProperty = null;
	private double trainValue = 0;
	private String trainProperty2 = null;
	private double trainValue2 = 0;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{AdaptationPlugin.class});;


	public ModeSwitchPropsPlugin(Map<Class<?>, AdaptationUtility> utilities){		
		verbose = false;
		
		this.utilities = utilities;
	}
		
	public ModeSwitchPropsPlugin withVerbosity(boolean verbosity){
		verbose = verbosity;
		return this;
	}
	public ModeSwitchPropsPlugin withTraining(boolean training){
		this.training = training;
		return this;
	}
	public ModeSwitchPropsPlugin withTrainProperty(String trainProperty){
		this.trainProperty = trainProperty;
		return this;
	}
	public ModeSwitchPropsPlugin withTrainValue(double trainValue){
		this.trainValue = trainValue;
		return this;
	}
	public ModeSwitchPropsPlugin withTrainProperty2(String trainProperty2){
		this.trainProperty2 = trainProperty2;
		return this;
	}
	public ModeSwitchPropsPlugin withTrainValue2(double trainValue2){
		this.trainValue2 = trainValue2;
		return this;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(DEECoContainer container) {

		this.container = container;
		
		container.addStartupListener(this);
		
		adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);

		ModeSwitchPropsManager.verbose = verbose;
		ModeSwitchPropsManager.training = training;
		ModeSwitchPropsManager.trainProperty = trainProperty;
		ModeSwitchPropsManager.trainValue = trainValue;
		ModeSwitchPropsManager.trainProperty2 = trainProperty2;
		ModeSwitchPropsManager.trainValue2 = trainValue2;
	}

	@Override
	public void onStartup() throws PluginStartupFailedException {
		if(container == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"DEECo container"));
		}
		if(adaptationPlugin == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"AdaptationPlugin"));
		}
		
		List<Component> components = new ArrayList<>();
		// Components with non-deterministic mode switching aspiration
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			DEECoModeChart modeChart = c.getModeChart();
			if (modeChart != null) {
				// Check the required utility was placed
				AdaptationUtility utility = null;
				for(Class<?> key : utilities.keySet()){
					if(key.getName().equals(c.getName())){
						utility = utilities.get(key);
					}
				}
				
				if(utility == null){
					throw new PluginStartupFailedException(String.format(
							"The %s component has no associated utility function.",
							c.getKnowledgeManager().getId()));
				}
				
				ComponentImpl componentImpl = new ComponentImpl(c);
				components.add(componentImpl);
			}
		}
		
		manager = new ModeSwitchPropsManager(components);
		adaptationPlugin.registerAdaptation(manager);
	}
}