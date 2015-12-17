package eclipsemag.fx.styledtext;

import java.nio.file.Paths;

import org.eclipse.fx.code.editor.LocalSourceFileInput;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.code.editor.services.InputDocument;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.fx.core.event.SimpleEventBus;
import org.eclipse.fx.text.ui.TextViewer;
import org.eclipse.fx.ui.controls.styledtext.StyleRange;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TextViewerSample2 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TextViewer API Sample");

		EventBus eventBus = new SimpleEventBus();
		TextViewer v = new TextViewer();
		SourceFileInput f = new LocalSourceFileInput(Paths.get("/tmp/sample.txt"), eventBus);
		InputDocument doc = new InputDocument(f, eventBus);
		v.setDocument(doc);

		StyleRange r = new StyleRange("colorful");
		r.start = 6;
		r.length = 6;
		v.getTextWidget().setStyleRanges(r);

		Scene s = new Scene(new BorderPane(v.getTextWidget()));
		s.getStylesheets().add(
				getClass().getResource("sample.css").toExternalForm());
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
