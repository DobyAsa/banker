package banker;
import java.util.ArrayList;
public class BankerAlgorithm {
    // 表示系统中资源种类数
    private final int _resourceClassesCount;
    private final int[] _totalResource;
    private ArrayList<Process> _processes = new ArrayList<Process>();
    private StringBuffer _executeInfo = new StringBuffer(50);
    public BankerAlgorithm(int[] totalResource, int resourceClassesCount,
                           ArrayList<Process> processes) {
        _resourceClassesCount = resourceClassesCount;
        _totalResource = totalResource;
        _processes = processes;
    }
    private ArrayList<Process> newProcesses() {
        ArrayList<Process> pList = new ArrayList<Process>();
        for (Process p : _processes) {
            pList.add(p.newProcess());
        }
        return pList;
    }
    public int[] getAvilable() {
        int[] avilable = new int[_resourceClassesCount];
        for (int i = 0; i < _resourceClassesCount; ++i) {
            avilable[i] = _totalResource[i] - getResourceAllocated(i);
        }
        return avilable;
    }
    // index代表某个资源的索引,结果为某个资源的已分配量.
    private int getResourceAllocated(int index) {
        int totalAllocated = 0;
        for (Process p : _processes) {
            int[] allocated = p.getAllocated();
            totalAllocated += allocated[index];
        }
        return totalAllocated;
    }
    public boolean addProcess(Process p) {
        if (isUniqueProcessId(p.getId())) {
            _executeInfo.append(p.getId() + "=" + p.toString());
            return _processes.add(p);
        } else {
            _executeInfo.append(p.getId() + "与已有进程重名.");
            return false;
        }
    }
    public boolean changeProcess(String oldProcessId, Process newProcess) {
        return changeProcess(oldProcessId, newProcess.getId(),
                newProcess.getMaxNeed(), newProcess.getAllocated());
    }
    public boolean changeProcess(String oldProcessId, String newProcessId,
                                 int[] maxNeed, int[] allocated) {
        Process p = getProcessById(_processes, oldProcessId);
        _executeInfo.append("进程" + oldProcessId + "=" + p.toString() + "\n");

        // oldProcessId与newProcessId说明更改的是同一个进程的其它部分,即进程没有改名.
        if (oldProcessId.equals(newProcessId)) {
            p.setAllocated(allocated);
            p.setMaxNeed(maxNeed);
            _executeInfo.append("更改为" + p.getId() + "=" + p.toString());
            return true;
        } else { // 进程改名,需判断是不是一个独一无二的名称.
            if (isUniqueProcessId(newProcessId)) {
                p.setId(newProcessId);
                p.setAllocated(allocated);
                p.setMaxNeed(maxNeed);
                _executeInfo.append("更改为" + p.getId() + "=" + p.toString());
                return true;
            } else {
                _executeInfo.append(newProcessId + "与已有进程重名.");
                return false;
            }
        }

    }
    public void removeProcess(String processId) {
        removeProcess(getProcessById(_processes, processId));
    }
    public void removeProcess(Process p) {
        _processes.remove(p);
        _executeInfo.append(p.getId());
    }
    public int[] getTotalResource() {
        return _totalResource;
    }
    public String getExecuteInfo() {
        // 若存在安全序列,则要删除最后一个"->".
        int startIndex = _executeInfo.lastIndexOf("->");
        if (startIndex != -1) {
            _executeInfo.delete(startIndex, startIndex + 2);
        }
        _executeInfo.append("\n\n");
        String info = _executeInfo.toString();
        _executeInfo.delete(0, _executeInfo.length());
        return info;
    }
    public ArrayList<Process> getProcesses() {
        return _processes;
    }
    public String[] getProcessNames() {
        String[] names = new String[_processes.size()];
        for (int i = 0; i < _processes.size(); i++)
            names[i] = _processes.get(i).getId();
        return names;
    }
    public boolean request(String processId, int[] resource) {
        Process p = getProcessById(_processes, processId);
        if (p != null) {
            return request(p, resource);
        } else
            return false;
    }
    private boolean request(Process p, int[] resource) {
        if (!isAllAllocatedLowerMax() || !isAllAllocatedLowerTotalResource())
            return false;
        int[] avilable = getAvilable();
        if (p.isLowerMaxNeed(resource, _executeInfo)
                && isRequestLowerAvilable(avilable, resource)) {
            ArrayList<Process> pList = newProcesses();
            Process newP = getProcessById(pList, p.getId());
            newP.allocate(resource);
            sub(avilable, resource);
            if (newP.isFinished()) {
                // System.out.println(newP.getId());
                _executeInfo.append(p.getId() + "->");
                add(avilable, newP.getAllocated());
            }
            if (isSecured(pList, avilable)) {
                p.allocate(resource);
                if (p.isFinished()) {
                    // System.out.println(newP.getId());
                    _executeInfo.append("\n进程" + p.getId() + "执行完毕.");
                    _processes.remove(p);
                    // add(avilable, newP.getAllocated());
                }
                return true;
            } else
                return false;
        } else
            return false;
    }
    // 判断当前系统是否安全
    public boolean isSecured() {
        return isSecured(newProcesses(), getAvilable());
    }
    private boolean isSecured(ArrayList<Process> pList, int[] avilable) {
        if (!isAllMaxNeedLowerTotalResource(pList))
            return false;

        while (!isAllFinished(pList)) {
            Process p = searchProcessLowerAvilable(pList, avilable);
            if (p != null) {
                int[] need = p.getNeed();
                p.allocate(need);
                // System.out.println(p.getId());
                _executeInfo.append(p.getId() + "->");
                sub(avilable, need);
                add(avilable, p.getAllocated());
            } else {
                _executeInfo.append("系统中剩余进程的资源需求量均大于系统资源可用量.");
                return false;
            }
        }
        return true;
    }
    private Process getProcessById(ArrayList<Process> pList, String id) {
        for (Process p : pList) {
            if (p.equals(id))
                return p;
        }
        return null;
    }
    private boolean isRequestLowerAvilable(int[] avilable, int[] resource) {
        for (int i = 0; i < avilable.length; ++i) {
            if (resource[i] > avilable[i]) {
                _executeInfo.append("请求的资源数超过系统资源剩余量.\n");
                return false;
            }
        }
        return true;
    }
    private boolean isAllFinished(ArrayList<Process> pList) {
        for (Process p : pList) {
            if (!p.isFinished())
                return false;
        }
        return true;
    }
    public boolean isAllMaxNeedLowerTotalResource(ArrayList<Process> processes) {
        for (Process p : processes) {
            if (!p.isMaxNeedLowerTotalResource(_totalResource, _executeInfo))
                return false;
        }
        return true;
    }
    public boolean isAllAllocatedLowerMax() {
        for (Process p : _processes)
            if (!p.isAllocatedLowerMax()) {
                _executeInfo
                        .append("进程" + p.getId() + "的已分配资源数大于其所需要的最大资源量.\n");
                return false;
            }
        return true;
    }
    public boolean isAllAllocatedLowerTotalResource() {
        for (int i = 0; i < _resourceClassesCount; i++)
            if (_totalResource[i] < getResourceAllocated(i)) {
                _executeInfo.append("资源R" + i + "的已分配总量大于系统中该资源的最大数目.\n");
                return false;
            }
        return true;
    }
    private Process searchProcessLowerAvilable(ArrayList<Process> pList,
                                               int[] avilable) {
        for (Process p : pList) {
            if (p.isNeedLowerAvilable(avilable) && !p.isFinished())
                return p;
        }
        return null;
    }
    public boolean isUniqueProcessId(String processId) {
        String[] names = getProcessNames();
        for (String id : names) {
            if (id.equals(processId)) {
                return false;
            }
        }
        return true;
    }
    // a用来保存a+b之和
    private void add(int[] a, int[] b) {
        for (int i = 0; i < a.length; ++i) {
            a[i] += b[i];
        }
    }
    // a用来保存a-b之和
    private void sub(int[] a, int[] b) {
        for (int i = 0; i < a.length; ++i) {
            a[i] -= b[i];
        }
    }
    public void print() {
        for (Process p : _processes) {
            System.out.println(p.toString());
        }
        System.out.println();
    }
}
