package eclipsemag.fx.styledtext.complete;

import org.eclipse.fx.code.editor.services.CompletionProposal;
import org.eclipse.fx.text.ui.contentassist.ICompletionProposal;
import org.eclipse.fx.ui.controls.styledtext.TextSelection;
import org.eclipse.jface.text.IDocument;

import javafx.scene.Node;

public class DartProposal implements ICompletionProposal {

		private final DartCompletionProposal proposal;

		public DartProposal(CompletionProposal proposal) {
			this.proposal = (DartCompletionProposal) proposal;
		}

		@Override
		public CharSequence getLabel() {
			return this.proposal.getLabel();
		}

		@Override
		public Node getGraphic() {
			return this.proposal.getGraphicSupplier().get();
		}

		@Override
		public void apply(IDocument document) {
			this.proposal.apply(document);
		}

		@Override
		public TextSelection getSelection(IDocument document) {
			org.eclipse.fx.code.editor.services.CompletionProposal.TextSelection selection = proposal.getSelection(document);
			return selection == null ? TextSelection.EMPTY : new TextSelection(selection.offset, selection.length);
		}

	}