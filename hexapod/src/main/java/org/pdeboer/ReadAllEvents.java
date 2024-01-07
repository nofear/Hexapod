package org.pdeboer;

import net.java.games.input.*;

import java.util.*;
import java.util.stream.*;

/**
 * This class shows how to use the event queue system in JInput. It will show
 * how to get the controllers, how to get the event queue for a controller, and
 * how to read and process events from the queue.
 *
 * @author Endolf
 */
public class ReadAllEvents {

	public ReadAllEvents() {

		var dualSense = Stream.of(ControllerEnvironment.getDefaultEnvironment().getControllers())
				.filter(controller -> controller.getName().compareToIgnoreCase(CONTROLLER) == 0)
				.findFirst()
				.orElseThrow(UnsupportedOperationException::new);

		System.out.println(String.format("[controller=%s] ", dualSense.getName()));

		Map<String, Float> controllerValues = new HashMap<>();

		while (true) {

			dualSense.poll();

			/* Get the controllers event queue */
			EventQueue queue = dualSense.getEventQueue();

			/* Create an event object for the underlying plugin to populate */
			Event event = new Event();

			/* For each object in the queue */
			while (queue.getNextEvent(event)) {

				/*
				 * Create a string buffer and put in it, the controller name,
				 * the time stamp of the event, the name of the component
				 * that changed and the new value.
				 *
				 * Note that the timestamp is a relative thing, not
				 * absolute, we can tell what order events happened in
				 * across controllers this way. We can not use it to tell
				 * exactly *when* an event happened just the order.
				 */

				String id = event.getComponent().getIdentifier().getName();
				float oldValue = controllerValues.computeIfAbsent(id, k -> (float) 0.0);

				if (oldValue == event.getValue()) {
					continue;
				}

				String m = String.format(
						"[time_stamp=%d] [component=%s] [analog=%s] [value=%f]",
						event.getNanos(),
						event.getComponent(),
						event.getComponent().isAnalog() ? "true" : "false",
						event.getValue()
				);

				controllerValues.put(id, event.getValue());

				System.out.println(m);
			}

			/*
			 * Sleep for 20 milliseconds, in here only so the example doesn't
			 * thrash the system.
			 */
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ReadAllEvents();
	}

	private static final String CONTROLLER = "DualSense Wireless Controller";
}