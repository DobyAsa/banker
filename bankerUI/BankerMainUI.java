package bankerUI;

import banker.Process;
import banker.BankerAlgorithm;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BankerMainUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = -8004544916653049326L;

    private JPanel panel;

    // north
    private JButton addResource;
    private JTextField textResourceCount;

    // east
    private JButton addProcess;
    private JButton deleteProcess;
    private JButton changeProcess;
    private JButton requestResource;
    private JButton currentSecured;
    private JButton exit;

    // center
    private JEditorPane resourcesInfo;
    private JEditorPane processesInfo; // 用html格式显示进程需要资源个数.
    private JTextArea result;
    private JSplitPane splitCenter;

    // data
    private BankerAlgorithm banker;
    private int resourceClassesCount;// 表示资源的个数

    // Dialogs
    private AddResourceDialog resourceDialog;
    private AddProcessDialog processDialog;
    private ChangeProcessDialog change;
    private DeleteProcessDialog delete;
    private RequestResourceDialog request;

    public BankerMainUI() {
        super("银行家算法");
        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Toolkit tool = this.getToolkit();
        // Image ico =
        // tool.getImage("G:\\workspace\\BankerAlgorithm\\exe\\img\\iconImage.png");
        // setIconImage(ico);
        setBounds(100, 100, 800, 600);
        panel = new JPanel(new BorderLayout());

        // north
        JPanel north = new JPanel();
        FlowLayout flowLayout = (FlowLayout) north.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        JLabel label = new JLabel("请输入系统资源种类个数:");
        north.add(label);
        textResourceCount = new JTextField(20);
        north.add(textResourceCount);
        addResource = new JButton("添加系统资源个数");
        addResource.addActionListener(this);
        north.add(addResource);
        panel.add(north, BorderLayout.NORTH);

        // center
        resourcesInfo = new JEditorPane("text/html", "<html></html>");
        resourcesInfo.setEditable(false);
        processesInfo = new JEditorPane("text/html", "<html></html>"); // 以html格式显示进程信息.
        processesInfo.setEditable(false);
        JSplitPane splitInfo = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitInfo.add(new JScrollPane(resourcesInfo), JSplitPane.TOP);
        splitInfo.add(new JScrollPane(processesInfo), JSplitPane.BOTTOM);
        splitInfo.setBorder(BorderFactory.createTitledBorder("系统信息"));
        splitInfo.setOneTouchExpandable(true);

        result = new JTextArea(5, 30);
        result.setEditable(false);
        result.setWrapStyleWord(true); // 按单词换行,即所有单词都不会打断.
        result.setLineWrap(true); // 换行.
        JScrollPane textScroll = new JScrollPane(result);
        textScroll.setBorder(BorderFactory.createTitledBorder("执行结果"));

        splitCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitCenter.setResizeWeight(1.0);
        splitCenter.add(splitInfo, JSplitPane.TOP);
        splitCenter.add(textScroll, JSplitPane.BOTTOM);
        splitCenter.setOneTouchExpandable(true); // 点击一下就可以扩展分割开来的控件.

        panel.add(splitCenter, BorderLayout.CENTER);

        // east
        JPanel east = new JPanel();

        addProcess = new JButton("添加进程");
        addProcess.addActionListener(this);
        changeProcess = new JButton("更改进程信息");
        changeProcess.addActionListener(this);
        deleteProcess = new JButton("删除进程");
        deleteProcess.addActionListener(this);

        requestResource = new JButton("请求资源");
        // requestResource.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        requestResource.addActionListener(this);
        currentSecured = new JButton("当前系统是否安全");
        currentSecured.addActionListener(this);

        exit = new JButton("退出");
        exit.addActionListener(this);

        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(addProcess);
        east.add(changeProcess);
        east.add(deleteProcess);
        east.add(currentSecured);
        east.add(requestResource);
        east.add(exit);
        panel.add(east, BorderLayout.EAST);

        this.getContentPane().add(new JScrollPane(panel));
        setEastButtonEnabled(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setResourceClassesCount(int count) {
        resourceClassesCount = count;
    }

    public void setEastButtonEnabled(boolean b) {
        // east
        addProcess.setEnabled(b);
        changeProcess.setEnabled(b);
        deleteProcess.setEnabled(b);
        requestResource.setEnabled(b);
        currentSecured.setEnabled(b);
        exit.setEnabled(b);
    }

    public BankerAlgorithm getBanker() {
        return banker;
    }

    // 一个数组小于另一个数组,两个数组大小相等.
    public boolean aLowerB(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > b[i])
                return false;
        }
        return true;
    }

    // 在resourceInfoz中显示系统资源的信息.
    private void updateTotalResourcesInfo() {
        StringBuffer html = new StringBuffer(100);
        html.append("<html><body>");
        html.append("<table width = \"100%\" border = \"1\" bgcolor=\"#C0C0C0\" bordercolor=\"#000000\">\n");
        StringBuffer resourceNames = new StringBuffer("<tr><td>资源名</td>");
        StringBuffer resourceCounts = new StringBuffer("<tr><td>资源个数</td>");
        int[] totalResource = banker.getTotalResource();
        for (int i = 0; i < totalResource.length; i++) {
            resourceNames.append("<td>");
            resourceNames.append("R" + String.valueOf(i));
            resourceNames.append("</td>");

            resourceCounts.append("<td>");
            resourceCounts.append(String.valueOf(totalResource[i]));
            resourceCounts.append("</td>");
        }
        resourceNames.append("</tr>");
        resourceCounts.append("</tr>");
        html.append(resourceNames);
        html.append(resourceCounts);
        html.append("</table>\n</body>\n</html>");

        resourcesInfo.setText(html.toString());
    }

    private void updateProcessInfo() {
        StringBuffer content = new StringBuffer("<html>\n");
        content.append("<body>\n");
        content.append("<table width = \"100%\" border = \"1\" bgcolor=\"#C0C0C0\" bordercolor=\"#000000\">\n");
        content.append("<tr><td>资源情况</td><td align = \"center\" colspan = "
                + resourceClassesCount
                + ">Max</td><td align = \"center\" colspan = "
                + resourceClassesCount
                + ">Allocated</td><td align = \"center\" colspan = "
                + resourceClassesCount
                + ">Need</td><td align = \"center\" colspan = "
                + resourceClassesCount + ">Avilable</td></tr>");

        content.append("<tr>");
        content.append("<td>进程名</td>");
        StringBuffer processNames = new StringBuffer(40);
        for (int i = 0; i < resourceClassesCount; i++) {
            processNames.append("<td>R" + i + "</td>");
        }
        content.append(processNames); // Max
        content.append(processNames); // Allocated
        content.append(processNames); // Need
        content.append(processNames); // Avilable
        content.append("</tr>");

        ArrayList<Process> processes = banker.getProcesses();
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            content.append("<tr>" + p.makeHtml());
            if (i == 0) {
                int[] avilable = banker.getAvilable();
                for (int j = 0; j < avilable.length; j++)
                    content.append("<td>" + avilable[j] + "</td>");
            }
            if (i == 1)
                content.append("<td rowspan ="
                        + String.valueOf(processes.size() - 1)
                        + " colspan = \"3\"></td>");
            content.append("</tr>");
        }
        content.append("</table>\n");
        content.append("</body>\n");
        content.append("</html>");

        processesInfo.setText(content.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == addResource) {
            String resourceCount = textResourceCount.getText().trim();
            if (resourceCount != null && !resourceCount.isEmpty()) {
                try {
                    setResourceClassesCount(Integer.parseInt(resourceCount));
                } catch (NumberFormatException number) {
                    JOptionPane
                            .showMessageDialog(this, "请输入整数,\""
                                    + textResourceCount.getText().trim()
                                    + "\"不能转换为整数!");
                    return;
                }
                textResourceCount.setEditable(false);
                resourceDialog = new AddResourceDialog(this,
                        resourceClassesCount);
                if (resourceDialog.showDialog()) {
                    banker = new BankerAlgorithm(
                            resourceDialog.getTotalResource(),
                            resourceClassesCount, new ArrayList<Process>());
                    updateTotalResourcesInfo(); // 更新系统资源信息.
                    this.setEastButtonEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "请先在文本框输入系统资源种类个数!");
            }
            return;
        }

        if (e.getSource() == addProcess) {
            processDialog = new AddProcessDialog(this, resourceClassesCount);
            if (processDialog.showDialog()) {
                Process p = processDialog.getProcess();
                if (aLowerB(p.getAllocated(), banker.getAvilable())) {
                    if (banker.addProcess(p)) {
                        result.setText(result.getText() + "成功添加进程: "
                                + banker.getExecuteInfo());
                        updateProcessInfo(); // 更新系统进程资源信息.
                    } else {
                        result.setText(result.getText() + "添加进程失败: "
                                + banker.getExecuteInfo());
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "不能添加这个新进程,否则系统中各进程已分配的资源之和就会大于系统中的资源总数!");
                    return;
                }
            }
            return;
        }

        if (e.getSource() == changeProcess) {
            String[] names = banker.getProcessNames();
            if (names.length == 0) {
                JOptionPane.showMessageDialog(this, "当前系统中没有一个进程,请先添加进程.");
                return;
            }

            change = new ChangeProcessDialog(this, resourceClassesCount, names);
            if (change.showDialog()) {
                Process p = change.getProcess();
                if (aLowerB(p.getAllocated(), banker.getAvilable())) {
                    if (banker.changeProcess(change.getOldProcessId(),
                            change.getProcess())) {
                        result.setText(result.getText() + "成功更改进程.\n "
                                + banker.getExecuteInfo());
                        updateProcessInfo(); // 更新系统进程资源信息.
                    } else {
                        result.setText(result.getText() + "更改进程失败: "
                                + banker.getExecuteInfo());
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "不能更改这个进程,否则系统中各进程已分配的资源之和就会大于系统中的资源总数!");
                    return;
                }

            }

            return;
        }

        if (e.getSource() == deleteProcess) {
            String[] names = banker.getProcessNames();
            if (names.length == 0) {
                JOptionPane.showMessageDialog(this, "当前系统中没有一个进程,请先添加进程.");
                return;
            }

            delete = new DeleteProcessDialog(this, names);
            if (delete.showDialog()) {
                banker.removeProcess(delete.getProcessId());
                result.setText(result.getText() + "成功删除进程 "
                        + banker.getExecuteInfo());
                updateProcessInfo(); // 更新系统进程资源信息.
            }

            return;
        }

        if (e.getSource() == currentSecured) {
            String[] names = banker.getProcessNames();
            if (names.length == 0) {
                result.setText(result.getText() + "当前系统中没有进程,系统是安全的.\n");
                return;
            }

            if (banker.isSecured()) {
                result.setText(result.getText() + "当前系统是安全的.\n安全序列: "
                        + banker.getExecuteInfo());
            } else {
                result.setText(result.getText() + "当前系统是不安全的.\n详细信息如下: "
                        + banker.getExecuteInfo());
            }
            return;
        }

        if (e.getSource() == requestResource) {
            String[] names = banker.getProcessNames();
            if (names.length == 0) {
                JOptionPane.showMessageDialog(this, "当前系统中没有一个进程,请先添加进程.");
                return;
            }

            request = new RequestResourceDialog(this, resourceClassesCount,
                    names);
            if (request.showDialog()) {
                // requestInfo中的最后一个数字是进程的索引.
                int[] requestInfo = request.getRequestInfo();
                int[] requestResource = new int[requestInfo.length - 1];
                System.arraycopy(requestInfo, 0, requestResource, 0,
                        requestResource.length);
                if (banker.request(
                        banker.getProcesses()
                                .get(requestInfo[requestInfo.length - 1])
                                .getId(), requestResource)) {
                    result.setText(result.getText() + "分配成功.\n安全序列: "
                            + banker.getExecuteInfo());
                    updateProcessInfo(); // 更新系统进程资源信息.
                } else {
                    String note = banker.getExecuteInfo();
                    result.setText(result.getText() + "分配失败.\n详细信息如下: " + note);
                }
            }
            return;
        }

        if (e.getSource() == exit) {
            if (JOptionPane.showConfirmDialog(this, "退出系统?", "确定退出",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                System.exit(0);
            return;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        // TODO Auto-generated method stub
        BankerMainUI banker = new BankerMainUI();
        banker.setVisible(true);
    }
}