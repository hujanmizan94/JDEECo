package cz.cuni.mff.d3s.deeco.model.runtime;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.KnowledgePathExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeModelHelper {

	public static KnowledgePath createKnowledgePath(
			String... knowledgePathNodes) {
		return KnowledgePathExt.createKnowledgePath(knowledgePathNodes);
	}

	public static KnowledgeChangeTrigger createKnowledgeChangeTrigger(
			String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgeChangeTrigger trigger = factory.createKnowledgeChangeTrigger();

		trigger.setKnowledgePath(RuntimeModelHelper
				.createKnowledgePath(knowledgePathNodes));

		return trigger;
	}
	
	public static Parameter createParameter(ParameterKind direction,
			String... knowledgePathNodes) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		Parameter param = factory.createParameter();

		param.setKind(direction);
		param.setKnowledgePath(RuntimeModelHelper
				.createKnowledgePath(knowledgePathNodes));

		return param;
	}

	public static TimeTrigger createPeriodicTrigger(long period) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		TimeTrigger trigger = factory.createTimeTrigger();

		trigger.setPeriod(period);
		trigger.setOffset(0);

		return trigger;
	}
	
	public static PathNodeField createPathNodeField(String name) {
		return KnowledgePathExt.createPathNodeField(name);
	}
}
