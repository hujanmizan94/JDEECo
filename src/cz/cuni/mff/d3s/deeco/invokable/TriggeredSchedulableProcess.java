package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

public class TriggeredSchedulableProcess implements IKnowledgeChangeListener {

	public SchedulableProcess sp;

	public TriggeredSchedulableProcess(SchedulableProcess sp) {
		this.sp = sp;
	}

	@Override
	public void knowledgeChanged(String triggerer, ETriggerType recMode) {
		sp.invoke(triggerer, recMode);
	}

	public boolean equals(Object o) {
		return o != null && o instanceof TriggeredSchedulableProcess
				&& this.sp.equals(((TriggeredSchedulableProcess) o).sp);
	}

	@Override
	public List<String> getKnowledgePaths() {
		if (isEnsembleTriggered()) {
			return getEvaluatedKnowledgePaths(
					EEnsembleParty.COORDINATOR.toString(),
					EEnsembleParty.MEMBER.toString());
		} else {
			return getEvaluatedKnowledgePaths();
		}
	}

	private List<String> getEvaluatedKnowledgePaths() {
		return getEvaluatedKnowledgePaths(null, null);
	}

	private List<String> getEvaluatedKnowledgePaths(String coord, String member) {
		List<Parameter> triggeredParams = ((ProcessTriggeredSchedule) sp.scheduling).parameters;
		List<String> result = new ArrayList<String>();
		ISession session;
		for (Parameter p : triggeredParams) {
			session = sp.km.createSession();
			session.begin();
			while (session.repeat()) {
				result.add(p.kPath.getEvaluatedPath(sp.km, coord, member,
						session));
				session.end();
			}
		}
		return result;
	}

	private boolean isEnsembleTriggered() {
		return sp instanceof SchedulableEnsembleProcess;
	}
}
