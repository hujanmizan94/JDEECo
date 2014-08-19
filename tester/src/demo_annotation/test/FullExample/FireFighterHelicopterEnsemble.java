package demo_annotation.test.FullExample;

import java.util.ArrayList;
import java.util.List;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;


@Ensemble
@PeriodicScheduling(80)
public class FireFighterHelicopterEnsemble {

	// it is better to have the modes from the membership condition
	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			@In("coord.ffLPos") Double ffLPos,
			
			@In("member.hFFPos") Double hFFPos,
			@In("member.hFFSpeed") Double hFFSpeed
 		){
		if(ffPos < 100)
			return true;
		return false;
	}
	
	@KnowledgeExchange
	public static void map(
			@InOut("coord.ffPos") TSParamHolder<Double> ffPos,
			@InOut("coord.ffSpeed") TSParamHolder<Double> ffSpeed,
			@InOut("coord.ffLPos") InaccuracyParamHolder<Double> ffLPos,
			
			@InOut("member.hFFPos") InaccuracyParamHolder<Double> hFFPos,
			@InOut("member.hFFSpeed") InaccuracyParamHolder<Double> hFFSpeed
	) {
		hFFPos.setWithTS(ffPos);
		hFFSpeed.setWithTS(ffSpeed);
	}
}