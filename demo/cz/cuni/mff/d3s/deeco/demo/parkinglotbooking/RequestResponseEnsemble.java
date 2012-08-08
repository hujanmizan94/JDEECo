/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.Map;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * base request-response ensemble class.
 * 
 * @author Jaroslav Keznikl
 *
 */
@DEECoEnsemble
@DEECoPeriodicScheduling(2000)
public class RequestResponseEnsemble {

	// must be public, static and extend Knowledge
	public static class RequesterInterface extends Knowledge {
		public Request request;
		public Response response;
	}

	public static class ResponderInterface extends Knowledge {
		public Map<UUID, Request> incomingRequests;
		public Map<UUID, Response> processedResponses;
	}

	@DEECoEnsembleMapper
	public static void map(
			@DEECoIn("member.request") Request request, 
			@DEECoOut("member.response") OutWrapper<Response> response,
			@DEECoOut("coord.incomingRequests[member.request.requestId]") OutWrapper<Request> incomingRequest, 
			@DEECoIn("coord.processedResponses[member.request.requestId]") Response processedResponse) {
		incomingRequest.item = request;
		if (processedResponse != null)
			response.item = processedResponse;
	}

	
}