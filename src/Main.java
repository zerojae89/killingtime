import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showWindow);
    }

    private static void showWindow() {
        JFrame window = new JFrame("인사 만들기");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField nameField = new JTextField(16);
        JButton greetButton = new JButton("인사하기");
        JLabel result = new JLabel(" ");

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 0));
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setLabelFor(nameField);
        form.add(nameLabel);
        form.add(nameField);
        form.add(greetButton);

        greetButton.addActionListener(event -> {
            result.setText(greeting(nameField.getText()));
            window.pack();
        });

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.add(form, BorderLayout.CENTER);
        content.add(result, BorderLayout.SOUTH);

        window.setContentPane(content);
        window.getRootPane().setDefaultButton(greetButton);
        window.pack();
        window.setMinimumSize(window.getSize());
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        nameField.requestFocusInWindow();
    }

    static String greeting(String input) {
        String name = input.strip();
        return name.isEmpty() ? "이름을 입력해 주세요." : "안녕하세요, " + name + "님!";
    }
}
