package eclipsemag.fx.styledtext;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.ServiceLoader;

import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.LocalSourceFileInput;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.code.editor.fx.services.internal.DefaultSourceViewerConfiguration;
import org.eclipse.fx.code.editor.services.InputDocument;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.fx.core.event.SimpleEventBus;
import org.eclipse.fx.text.ui.source.SourceViewer;
import org.eclipse.fx.text.ui.source.SourceViewerConfiguration;
import org.eclipse.jface.text.IDocumentExtension3;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.DartServerFactory;
import at.bestsolution.dart.server.api.model.AddContentOverlay;
import at.bestsolution.dart.server.api.model.ChangeContentOverlay;
import at.bestsolution.dart.server.api.model.SourceEdit;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;
import eclipsemag.fx.styledtext.annotation.DartAnnotationModel;
import eclipsemag.fx.styledtext.annotation.DartAnnotationPresenter;
import eclipsemag.fx.styledtext.complete.DartProposal;
import eclipsemag.fx.styledtext.complete.DartProposalComputer;
import eclipsemag.fx.styledtext.generated.DartPartitioner;
import eclipsemag.fx.styledtext.generated.DartPresentationReconciler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SourceViewerSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SourceViewer API Sample");

		EventBus eventBus = new SimpleEventBus();

		LocalSourceFileInput f = new LocalSourceFileInput(Paths.get("/Users/tomschindl/Desktop/dart-sample/sample.dart"), eventBus);
		InputDocument doc = new InputDocument(f, eventBus);

		SourceViewer viewer = new SourceViewer();

		DartPartitioner partitioner = new DartPartitioner();

		DartServer dartServer = initDart(eventBus, f);
		SourceViewerConfiguration configuration = new DefaultSourceViewerConfiguration(f,
				new DartPresentationReconciler(),
				new DartProposalComputer(dartServer),
				new DartAnnotationModel(dartServer, f, new FXThreadSync()),
				new DartAnnotationPresenter(),
				null,
				DartProposal::new);

		doc.setDocumentPartitioner(IDocumentExtension3.DEFAULT_PARTITIONING, partitioner);
		doc.setDocumentPartitioner(partitioner);
		partitioner.connect(doc);

		viewer.configure(configuration);
		viewer.setDocument(doc,configuration.getAnnotationModel());

		Scene s = new Scene(new BorderPane(viewer.getTextWidget()));
		s.getStylesheets().add(getClass().getResource("source.css").toExternalForm());
		primaryStage.setScene(s);
		primaryStage.show();
	}

	private DartServer initDart(EventBus eventBus, LocalSourceFileInput f) {
		ServiceLoader<DartServerFactory> loader = ServiceLoader.load(DartServerFactory.class);

		DartServer server = loader.iterator().next().getServer("my-server");
		ServiceAnalysis analysisService = server.getService(ServiceAnalysis.class);
		analysisService.setAnalysisRoots(
				new String[] { f.getPath().getParent().toAbsolutePath().toString() },
				new String[0],
				null);

		String filePath = f.getPath().toAbsolutePath().toString();

		AddContentOverlay overlay = new AddContentOverlay();
		overlay.setContent(f.getData());
		server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, overlay));

		eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, e -> handleInputModified(server, e.getData(), filePath));

		return server;
	}

	void handleInputModified(DartServer server, SourceFileChange modified, String filePath) {
		ChangeContentOverlay overlay = new ChangeContentOverlay();
		SourceEdit edit = new SourceEdit();
		edit.setOffset(modified.offset);
		edit.setLength(modified.length);
		edit.setReplacement(modified.replacement);
		overlay.setEdits(new SourceEdit[] { edit });
		server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, overlay));
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}