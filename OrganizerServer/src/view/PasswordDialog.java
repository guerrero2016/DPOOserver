package view;

import javax.swing.*;
import java.util.Arrays;

public class PasswordDialog extends JDialog{
    private char[] pass;
    private final char[] CANCEL = {'.', '-', '^'};

    public PasswordDialog(MainServerView mainServerView) {
        JPasswordField password = new JPasswordField();
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Contrasenya de l'usuari root de la BBDD"),
                password
        };
        int result = JOptionPane.showConfirmDialog(mainServerView, inputs, "Accés a la BBDD", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.CANCEL_OPTION) {
            pass = CANCEL;
        } else {
            pass = password.getPassword();
        }
    }

    public String getPass() {
        final StringBuilder sb = new StringBuilder();
        sb.append(pass);
        return sb.toString();
    }

}
