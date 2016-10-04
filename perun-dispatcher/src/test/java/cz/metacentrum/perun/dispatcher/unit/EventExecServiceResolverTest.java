package cz.metacentrum.perun.dispatcher.unit;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.metacentrum.perun.core.api.Facility;
import cz.metacentrum.perun.core.api.Pair;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.PrivilegeException;
import cz.metacentrum.perun.core.api.exceptions.ServiceNotExistsException;
import cz.metacentrum.perun.dispatcher.AbstractDispatcherTest;
import cz.metacentrum.perun.dispatcher.exceptions.InvalidEventMessageException;
import cz.metacentrum.perun.dispatcher.model.Event;
import cz.metacentrum.perun.dispatcher.processing.EventExecServiceResolver;
import cz.metacentrum.perun.taskslib.model.ExecService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Michal Voců
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class EventExecServiceResolverTest extends AbstractDispatcherTest {

	@Autowired
	private EventExecServiceResolver eventExecServiceResolver;

	@Test
	public void parseEventTest() throws ServiceNotExistsException, InvalidEventMessageException, InternalErrorException, PrivilegeException {
		System.out.println("EventExecServiceResolver.parseEventTest()");

		String message = member1.serializeToString() + " added to " + group1.serializeToString() + ".";

		Event event = new Event();
		event.setTimeStamp(System.currentTimeMillis());
		event.setHeader("portishead");
		event.setData(message);
		Map<Facility, Set<ExecService>> resolvedServices = eventExecServiceResolver.parseEvent(event.toString());

		Assert.assertTrue("We should resolved only one facility-service", resolvedServices.size() == 1);

		Set<ExecService> resolved = resolvedServices.get(facility1);
		Assert.assertTrue("We should have 2 exec services", resolved.size() == 2);
		Assert.assertTrue("Our exec service 1 is missing", resolved.contains(execservice1));
		Assert.assertTrue("Our exec service 2 is missing", resolved.contains(execservice2));
		//Assert.assertEquals("Facility from test is not the same", facility1, resolved.getRight());

	}

}
