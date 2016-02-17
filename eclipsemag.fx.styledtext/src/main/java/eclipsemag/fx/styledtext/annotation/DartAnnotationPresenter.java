package eclipsemag.fx.styledtext.annotation;

import java.util.Arrays;
import java.util.List;

import org.eclipse.fx.text.ui.source.AnnotationPresenter;
import org.eclipse.jface.text.source.Annotation;

import at.bestsolution.dart.server.api.model.AnalysisErrorType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DartAnnotationPresenter implements AnnotationPresenter {
	private static Image TODO = new Image(DartAnnotationPresenter.class.getResource("icons/showtsk_tsk.png").toExternalForm());
	private static Image ERROR = new Image(DartAnnotationPresenter.class.getResource("icons/message_error.png").toExternalForm());
	private static Image INFO = new Image(DartAnnotationPresenter.class.getResource("icons/message_info.png").toExternalForm());
	private static Image WARNING = new Image(DartAnnotationPresenter.class.getResource("icons/message_warning.png").toExternalForm());

	@Override
	public List<String> getTypes() {
		return Arrays.asList("dart.annotation.WARNING","dart.annotation.ERROR","dart.annotation.INFO");
	}

	@Override
	public Node getPresentation(Annotation annotation) {
		Node n = null;
		if( annotation instanceof DartAnnotation ) {
			DartAnnotation ja = (DartAnnotation) annotation;

			if( ja.getError().getType() == AnalysisErrorType.TODO ) {
				n = new ImageView(TODO);
			} else {
				switch (ja.getError().getSeverity()) {
				case ERROR:
					n = new ImageView(ERROR);
					break;
				case INFO:
					n = new ImageView(INFO);
					break;
				case WARNING:
					n = new ImageView(WARNING);
					break;
				default:
					break;
				}
			}

			if( n != null ) {
				Label l = new Label(null, n);
				l.setTooltip(new Tooltip(ja.getError().getMessage()));
				n = l;
			}
		}
		return n;
	}

}
