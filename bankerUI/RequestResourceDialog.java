package bankerUI;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class RequestResourceDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = -7564169130445277739L;
    public static boolean InputEffiencent = true;
    public static boolean InputError = false;
    private JButton okButton;
    private JButton cancelButton;
    private JComboBox processNames;
    private JTextField[] textRequest;
    private int[] requestInfo;
    private boolean returnResult = InputError; // InputEffiencent, InputError.
    private final int lineHeight = 35;
    private final int width = 65;
    public RequestResourceDialog(JFrame owner, int resourceClassesCount,
                                 String[] names) {
        super(owner, "请求资源", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(resourceClassesCount + 2, 2));
        processNames = new JComboBox(names);
        processNames.setSelectedIndex(0); // 默认选中第一个进程.
        panel.add(new JLabel("请选择进程名:"));
        panel.add(processNames);
        JLabel[] label = new JLabel[resourceClassesCount];
        textRequest = new JTextField[resourceClassesCount];
        for (int i = 0; i < resourceClassesCount; i++) {
            String resourceName = "资源R" + String.valueOf(i) + ":";
            label[i] = new JLabel(resourceName);
            panel.add(label[i]);
            textRequest[i] = new JTextField();
            panel.add(textRequest[i]);
        }
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
        this.setBounds(getX(), getY(), width * 2, lineHeight
                * (textRequest.length + 2));
        this.setLocationRelativeTo(this.getOwner());
    }
    private void setRequestResource() {
        requestInfo = new int[textRequest.length + 1];
        for (int i = 0; i < textRequest.length; i++) {
            try {
                requestInfo[i] = Integer.parseInt(textRequest[i].getText()
                        .trim());
                textRequest[i].setText("");
                if (requestInfo[i] < 0) {
                    JOptionPane.showMessageDialog(this, "请输入大于零的整数: "
                            + requestInfo[i]);
                    returnResult = InputError;
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入整数,\""
                        + textRequest[i].getText().trim() + "\"不能转换为整数!");
                returnResult = InputError;
                return;
            }
        }
        returnResult = InputEffiencent;
    }
    public boolean showDialog() {
        this.setVisible(true);
        return returnResult;
    }
    // 最后一个数字代表选中的进程的索引,其余的数字代表请求的资源数.
    public int[] getRequestInfo() {
        requestInfo[requestInfo.length - 1] = processNames.getSelectedIndex();
        return requestInfo;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == okButton) {
            this.setRequestResource();
            this.setVisible(false);
        }
        if (e.getSource() == cancelButton) {
            returnResult = InputError;
            this.dispose();
        }
    }
}
