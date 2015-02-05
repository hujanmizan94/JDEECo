package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Receival additional information for L1 packet incoming from MANET source
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class MANETReceivedInfo extends ReceivedInfo {
	
	public final double rssi; /** Radio Signal Strength Indicator */
	
	public MANETReceivedInfo(String srcAddress, double rssi) {
		super(srcAddress);
		this.rssi = rssi;
	}

}
