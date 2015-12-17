package eclipsemag.fx.styledtext;

import org.eclipse.fx.text.ui.TextViewer;
import org.eclipse.fx.ui.controls.styledtext.StyleRange;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TextViewerSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TextViewer API Sample");

		TextViewer v = new TextViewer();
		IDocument doc = new Document("Hello JavaFX!");
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
