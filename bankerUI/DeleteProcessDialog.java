package bankerUI;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class DeleteProcessDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 7206793382174134317L;
    public static boolean InputEffiencent = true;
    public static boolean InputError = false;
    private JButton okButton;
    private JButton cancelButton;
    private JComboBox processNames;
    private String processId;
    private boolean returnResult = InputError; // InputEffiencent, InputError.
    private final int lineHeight = 35;
    private final int width = 65;
    public DeleteProcessDialog(JFrame owner, String[] names) {
        super(owner, "删除进程", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        processNames = new JComboBox(names);
        processNames.setSelectedIndex(0); // 默认选择第一个进程.
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("请选择要删除的进程:"));
        panel.add(processNames);
        okButton = new JButton("确定");
        okButton.addActionListener(this);
        panel.add(okButton);
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
        setBounds();
        this.getContentPane().add(panel);
    }
    public void setBounds() {
        this.setBounds(getX(), getY(), width * 4, lineHeight * 3);
        this.setLocationRelativeTo(this.getOwner());
    }
    private void setProcessId() {
        processId = (String) processNames.getSelectedItem();
        returnResult = InputEffiencent;
    }
    public String getProcessId() {
        return processId;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == okButton) {
            this.setProcessId();
            this.setVisible(false);
        }
        if (e.getSource() == cancelButton) {
            returnResult = InputError;
            this.dispose();
        }
    }
    public boolean showDialog() {
        this.setVisible(true);
        return returnResult;
    }
}
