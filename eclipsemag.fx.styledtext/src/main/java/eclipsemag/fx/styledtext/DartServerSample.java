package eclipsemag.fx.styledtext;

import java.util.ServiceLoader;
import java.util.stream.Stream;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.DartServerFactory;
import at.bestsolution.dart.server.api.model.CompletionResultsNotification;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;
import at.bestsolution.dart.server.api.services.ServiceCompletion;

public class DartServerSample {

	public static void main(String[] args) {
		ServiceLoader<DartServerFactory> loader = ServiceLoader.load(DartServerFactory.class);

		DartServer server = loader.iterator().next().getServer("my-server");

		ServiceAnalysis analysisService = server.getService(ServiceAnalysis.class);
		ServiceCompletion completionService = server.getService(ServiceCompletion.class);

		String file = "/Users/tomschindl/Desktop/dart-sample/sample.dart";
		// Configure server
		analysisService.setAnalysisRoots(
				new String[] { "/Users/tomschindl/Desktop/dart-sample/" },
				new String[0],
				null);

		// Subscribe to auto-complete events
		completionService.results(DartServerSample::handleHandleResults);

		// Request auto-complete
		completionService.getSuggestions(file, 352);

		// Request errors/warnings/...
		Stream.of(analysisService.getErrors(file).getErrors())
			.forEach(System.err::println);
	}

	static void handleHandleResults(CompletionResultsNotification notification) {
		Stream.of(notification.getResults())
			.forEach(System.err::println);
	}
}
