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
import banker.Process;
public class ChangeProcessDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 7620376369161897583L;
    public static boolean InputEffiencent = true;
    public static boolean InputError = false;
    private JButton changeProcessName;
    private JButton okButton;
    private JButton cancelButton;
    private JComboBox processNames;
    private JTextField[] textMaxNeed;
    private JTextField[] textAllocated;
    private int[] maxNeed = null;
    private int[] allocated = null;
    private String newProcessName;
    private boolean returnResult = InputError; // InputEffiencent, InputError.
    private final int lineHeight = 35;
    private final int width = 65;
    public ChangeProcessDialog(JFrame owner, int resourceClassesCount,
                               String[] names) {
        super(owner, "更改进程信息", true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(resourceClassesCount + 3, 3));
        panel.add(new JLabel("请选择进程:"));
        processNames = new JComboBox(names);
        processNames.setSelectedIndex(0);
        processNames.addActionListener(this);
        panel.add(processNames);
        changeProcessName = new JButton("更改进程名称");
        changeProcessName.addActionListener(this);
        panel.add(changeProcessName);
        newProcessName = (String) processNames.getSelectedItem();
        panel.add(new JLabel("资源名")); // 站位作用.
        panel.add(new JLabel("最大需求量"));
        panel.add(new JLabel("已分配"));
        textMaxNeed = new JTextField[resourceClassesCount];
        textAllocated = new JTextField[resourceClassesCount];
        maxNeed = new int[resourceClassesCount];
        allocated = new int[resourceClassesCount];
        Process p = ((BankerMainUI) owner).getBanker().getProcesses().get(0);
        int[] oldMaxNeed = p.getMaxNeed();
        int[] oldAllocated = p.getAllocated();
        for (int i = 0; i < resourceClassesCount; i++) {
            String resourceName = "资源R" + String.valueOf(i) + ":";
            panel.add(new JLabel(resourceName));
            textMaxNeed[i] = new JTextField(String.valueOf(oldMaxNeed[i]));
            panel.add(textMaxNeed[i]);
            textAllocated[i] = new JTextField(String.valueOf(oldAllocated[i]));
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
        if (setMaxNeed() && setAllocated()) {
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
    private void updateProcessInfoBySelectedIndex() {
        newProcessName = (String) processNames.getSelectedItem();
        if (newProcessName == null) {
            processNames.setSelectedIndex(0);
            newProcessName = (String) processNames.getSelectedItem();
        }
        BankerMainUI ui = (BankerMainUI) this.getOwner();
        Process p = ui.getBanker().getProcesses()
                .get(processNames.getSelectedIndex());
        int[] oldMaxNeed = p.getMaxNeed();
        int[] oldAllocated = p.getAllocated();
        for (int i = 0; i < oldMaxNeed.length; i++) {
            textMaxNeed[i].setText(String.valueOf(oldMaxNeed[i]));
            textAllocated[i].setText(String.valueOf(oldAllocated[i]));
        }
    }
    public Process getProcess() {
        return new Process(newProcessName, maxNeed, allocated, maxNeed.length);
    }
    public String getOldProcessId() {
        return (String) processNames.getSelectedItem();
    }
    public boolean showDialog() {
        this.setVisible(true);
        return returnResult;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == changeProcessName) {
            newProcessName = JOptionPane.showInputDialog(this, "请输入进程的新名字");
            if (newProcessName.isEmpty()) {
                newProcessName = JOptionPane.showInputDialog(this, "请输入进程的新名字");
            }
            return;
        }
        if (e.getSource() == processNames) {
            updateProcessInfoBySelectedIndex();
            return;
        }
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
