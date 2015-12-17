package eclipsemag.fx.styledtext;

import org.eclipse.fx.ui.controls.styledtext.StyleRange;
import org.eclipse.fx.ui.controls.styledtext.StyledTextArea;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StyledTextSample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("StyledText API Sample");

		StyledTextArea sta = new StyledTextArea();
		sta.getContent().setText("Hello JavaFX!");
		StyleRange r = new StyleRange("colorful");
		r.start = 6;
		r.length = 6;
		sta.setStyleRanges(r);

		Scene s = new Scene(new BorderPane(sta));
		s.getStylesheets().add(
				getClass().getResource("sample.css").toExternalForm());
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
