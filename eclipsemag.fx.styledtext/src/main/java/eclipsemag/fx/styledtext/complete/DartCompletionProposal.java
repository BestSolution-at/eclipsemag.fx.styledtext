package eclipsemag.fx.styledtext.complete;

import java.util.function.Supplier;

import org.eclipse.fx.code.editor.services.CompletionProposal.BaseCompletetionProposal;

import javafx.scene.Node;

public class DartCompletionProposal extends BaseCompletetionProposal implements Comparable<DartCompletionProposal> {
	private int relevance;
	private final Supplier<Node> graphicSupplier;

	public DartCompletionProposal(int relevance, String replacementString, int replacementOffset, int replacementLength,
			CharSequence label, Supplier<Node> graphicSupplier) {
		super(replacementString, replacementOffset, replacementLength, label);
		this.graphicSupplier = graphicSupplier;
	}

	public Supplier<Node> getGraphicSupplier() {
		return graphicSupplier;
	}

	@Override
	public int compareTo(DartCompletionProposal o) {
		int compare = Integer.compare(relevance, o.relevance);
		if( compare == 0 ) {
			compare = getLabel().toString().compareTo(o.getLabel().toString());
		}
		return compare;
	}
}
