package cz.cuni.mff.d3s.jdeeco.ros;

import kobuki_msgs.BumperEvent;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import cz.cuni.mff.d3s.jdeeco.ros.datatypes.BumperValue;

/**
 * Provides methods to check robot's bumper state through ROS. Registration of
 * appropriate ROS topics is handled in the
 * {@link #subscribeDescendant(ConnectedNode)} method.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Bumper extends TopicSubscriber {

	/**
	 * The name of the bumper topic.
	 */
	private static final String BUMPER_TOPIC = "/mobile_base/events/bumper";
	/**
	 * The bumper state.
	 */
	private BumperValue bumper;

	/**
	 * Internal constructor enables the {@link RosServices} to be in the control
	 * of instantiating {@link Bumper}.
	 */
	Bumper() {
		bumper = BumperValue.RELEASED;
	}

	/**
	 * Register and subscribe to required ROS topics of sensor readings.
	 * 
	 * @param connectedNode
	 *            The ROS node on which the DEECo node runs.
	 */
	@Override
	protected void subscribeDescendant(ConnectedNode connectedNode) {
		Subscriber<BumperEvent> bumperTopic = connectedNode.newSubscriber(
				BUMPER_TOPIC, BumperEvent._TYPE);
		bumperTopic.addMessageListener(new MessageListener<BumperEvent>() {
			@Override
			public void onNewMessage(BumperEvent message) {
				byte state = message.getState();
				if (state == BumperEvent.RELEASED) {
					bumper = BumperValue.RELEASED;
				} else {
					bumper = BumperValue.fromByte(message.getBumper());
				}
				// TODO: log
			}
		});
	}

	/**
	 * The bumper state published in the bumper topic.
	 * 
	 * @return the state of the bumper.
	 */
	public BumperValue getBumper() {
		return bumper;
	}

}