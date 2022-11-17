package bankerUI;
import banker.Process;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class AddProcessDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = -4063443304983396100L;
    public static boolean InputEffiencent = true;
    public static boolean InputError = false;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField processName;
    private JTextField[] textMaxNeed;
    private JTextField[] textAllocated;
    private int[] maxNeed = null;
    private int[] allocated = null;
    private String name;
    private boolean returnResult = InputError; // InputEffiencent, InputError.
    private final int lineHeight = 35;
    private final int width = 65;
    public AddProcessDialog(JFrame owner, int resourceClassesCount) {
        super(owner, "添加新进程", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        textMaxNeed = new JTextField[resourceClassesCount];
        textAllocated = new JTextField[resourceClassesCount];
        maxNeed = new int[resourceClassesCount];
        allocated = new int[resourceClassesCount];
        processName = new JTextField();
        JPanel panel = new JPanel(new GridLayout(resourceClassesCount + 3, 3));
        panel.add(new JLabel("进程名"));
        panel.add(processName);
        panel.add(new JLabel()); // 站位作用.
        panel.add(new JLabel("资源名")); // 站位作用.
        panel.add(new JLabel("最大需求量"));
        panel.add(new JLabel("已分配"));
        for (int i = 0; i < resourceClassesCount; i++) {
            String resourceName = "资源R" + String.valueOf(i) + ":";
            panel.add(new JLabel(resourceName));
            textMaxNeed[i] = new JTextField();
            panel.add(textMaxNeed[i]);
            textAllocated[i] = new JTextField();
            panel.add(textAllocated[i]);
        }
        panel.add(new JLabel()); // 站位作用.
        okButton = new JButton("确定");
        okButton.addActionListener(this);
        panel.add(okButton);
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
        this.getContentPane().add(panel);
        setBounds();
    }
    public void setBounds() {
        this.setBounds(getX(), getY(), width * 3 + width, lineHeight
                * (textMaxNeed.length + 3));
        this.setLocationRelativeTo(this.getOwner());
    }
    private void setProcess() {
        if (setName() && setMaxNeed() && setAllocated()) {
            for (int i = 0; i < maxNeed.length; i++) {
                if (maxNeed[i] < allocated[i]) {
                    returnResult = InputError;
                    JOptionPane.showMessageDialog(this, "进程已分配资源量大于进程所需资源最大量!");
                    return;
                }
            }
            returnResult = InputEffiencent;
        }
    }
    private boolean setName() {
        returnResult = InputEffiencent;
        name = processName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "进程名不能为空!");
            returnResult = InputError;
            return returnResult;
        }
        processName.setText("");
        return returnResult;
    }
    private boolean setMaxNeed() {
        returnResult = InputEffiencent;
        for (int i = 0; i < textMaxNeed.length; i++) {
            try {
                maxNeed[i] = Integer.parseInt(textMaxNeed[i].getText().trim());
                textMaxNeed[i].setText("");
                if (maxNeed[i] < 0) {
                    JOptionPane.showMessageDialog(this, "请输入大于零的整数: "
                            + maxNeed[i]);
                    returnResult = InputError;
                    return returnResult;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入整数,\""
                        + textMaxNeed[i].getText().trim() + "\"不能转换为整数!");
                returnResult = InputError;
                return returnResult;
            }
        }
        return returnResult;
    }
    private boolean setAllocated() {
        returnResult = InputEffiencent;
        for (int i = 0; i < textAllocated.length; i++) {
            try {
                allocated[i] = Integer.parseInt(textAllocated[i].getText()
                        .trim());
                textAllocated[i].setText("");
                if (allocated[i] < 0) {
                    JOptionPane.showMessageDialog(this, "请输入大于零的整数: "
                            + allocated[i]);
                    returnResult = InputError;
                    return returnResult;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入整数,\""
                        + textAllocated[i].getText().trim() + "\"不能转换为整数!");
                returnResult = InputError;
                return returnResult;
            }
        }
        returnResult = InputEffiencent;
        return returnResult;
    }
    public Process getProcess() {
        return new Process(name, maxNeed, allocated, maxNeed.length);
    }
    public boolean showDialog() {
        this.setVisible(true);
        return returnResult;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == okButton) {
            setProcess();
            this.setVisible(false);
        }
        if (e.getSource() == cancelButton) {
            returnResult = InputError;
            this.dispose();
        }
    }
}
