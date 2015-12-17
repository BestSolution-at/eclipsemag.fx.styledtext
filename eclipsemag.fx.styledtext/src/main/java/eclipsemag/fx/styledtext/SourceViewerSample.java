package eclipsemag.fx.styledtext;

import java.nio.file.Paths;

import org.eclipse.fx.code.editor.LocalSourceFileInput;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.code.editor.fx.services.internal.DefaultSourceViewerConfiguration;
import org.eclipse.fx.code.editor.services.InputDocument;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.fx.core.event.SimpleEventBus;
import org.eclipse.fx.text.ui.source.SourceViewer;
import org.eclipse.fx.text.ui.source.SourceViewerConfiguration;
import org.eclipse.jface.text.IDocumentExtension3;

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

		SourceFileInput f = new LocalSourceFileInput(Paths.get("/tmp/sample.dart"), eventBus);
		InputDocument doc = new InputDocument(f, eventBus);

		SourceViewer viewer = new SourceViewer();

		DartPartitioner partitioner = new DartPartitioner();
		SourceViewerConfiguration configuration = new DefaultSourceViewerConfiguration(f,
				new DartPresentationReconciler(), null, null, null, null, null);

		doc.setDocumentPartitioner(IDocumentExtension3.DEFAULT_PARTITIONING, partitioner);
		doc.setDocumentPartitioner(partitioner);
		partitioner.connect(doc);

		viewer.configure(configuration);
		viewer.setDocument(doc);

		Scene s = new Scene(new BorderPane(viewer.getTextWidget()));
		s.getStylesheets().add(getClass().getResource("source.css").toExternalForm());
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}