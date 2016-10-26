import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

class NumberedEditorKit extends StyledEditorKit
{

    NumberedEditorKit()
    {
    }

    public ViewFactory getViewFactory()
    {
        return new NumberedViewFactory();
    }
}