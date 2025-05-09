package presentation;

import javax.swing.*;

public class UIPanel extends JPanel {
    private Presentation p;

    public UIPanel(Presentation p) {
        super();
        this.p = p;
    }

    public Presentation getPresentation() {
        return p;
    }
}
