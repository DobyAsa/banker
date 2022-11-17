package bankerUI;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
public class AddResourceDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 5706645239429800376L;
    public static boolean InputEffiencent = true;
    public static boolean InputError = false;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField[] text;
    private int[] totalResources = null;
    private boolean returnResult = InputError; // InputEffiencent, InputError.
    private final int lineHeight = 35;
    private final int width = 65;
    public AddResourceDialog(JFrame owner, int resourceClassesCount) {
        super(owner, "添加系统资源", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JLabel[] label = new JLabel[resourceClassesCount];
        text = new JTextField[resourceClassesCount];
        JPanel panel = new JPanel(new GridLayout(resourceClassesCount + 2, 2));
        panel.add(new JLabel("资源名"));
        panel.add(new JLabel("资源个数"));
        for (int i = 0; i < resourceClassesCount; i++) {
            String resourceName = "资源R" + String.valueOf(i) + ":";
            label[i] = new JLabel(resourceName);
            panel.add(label[i]);
            text[i] = new JTextField();
            panel.add(text[i]);
        }
        okButton = new JButton("确定");
        okButton.addActionListener(this);
        panel.add(okButton);
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
        this.getContentPane().add(new JScrollPane(panel));
        setBounds();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == okButton) {
            this.setTotalResource();
            this.setVisible(false);
        }
        if (e.getSource() == cancelButton) {
            returnResult = InputError;
            this.dispose();
        }
    }
    public void setBounds() {
        this.setBounds(getX(), getY(), width * 2, lineHeight
                * (text.length + 2));
        this.setLocationRelativeTo(this.getOwner());
    }
    private void setTotalResource() {
        totalResources = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            try {
                totalResources[i] = Integer.parseInt(text[i].getText().trim());
                text[i].setText("");
                if (totalResources[i] < 0) {
                    JOptionPane.showMessageDialog(this, "请输入大于零的整数: "
                            + totalResources[i]);
                    returnResult = InputError;
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入整数,\""
                        + text[i].getText().trim() + "\"不能转换为整数!");
                returnResult = InputError;
                return;
            }
        }
        returnResult = InputEffiencent;
    }
    public int[] getTotalResource() {
        return totalResources;
    }
    public boolean showDialog() {
        this.setVisible(true);
        return returnResult;
    }
}
