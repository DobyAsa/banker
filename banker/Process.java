package banker;
import java.util.Arrays;
public class Process {
    // 表示系统中资源种类数
    private final int _resourceClassesCount;
    private String _id;
    private int[] _maxNeed;
    private int[] _allocated;
    public Process(String id, int[] maxNeed, int[] allocated,
                   int resourceClassesCount) {
        _resourceClassesCount = resourceClassesCount;
        _id = id;
        _maxNeed = maxNeed;
        _allocated = allocated;
    }
    public String getId() {
        return _id;
    }
    public void setId(String id) {
        _id = id;
    }
    public int[] getMaxNeed() {
        return _maxNeed;
    }
    public void setMaxNeed(int[] maxNeed) {
        _maxNeed = maxNeed;
    }
    public int[] getAllocated() {
        return _allocated;
    }
    public void setAllocated(int[] allocated) {
        _allocated = allocated;
    }
    public int[] getNeed() {
        int[] need = new int[_resourceClassesCount];
        for (int i = 0; i < _resourceClassesCount; ++i)
            need[i] = _maxNeed[i] - _allocated[i];
        return need;
    }
    public Process newProcess() {
        return new Process(this._id, Arrays.copyOf(_maxNeed,
                _resourceClassesCount), Arrays.copyOf(_allocated,
                _resourceClassesCount), _resourceClassesCount);
    }
    public boolean equals(String id) {
        return _id.equals(id);
    }
    public boolean isFinished() {
        for (int i = 0; i < _resourceClassesCount; ++i)
            if (_maxNeed[i] != _allocated[i])
                return false;
        return true;
    }
    public boolean isNeedLowerAvilable(int[] avilable) {
        int[] need = getNeed();
        for (int i = 0; i < avilable.length; ++i) {
            if (need[i] > avilable[i])
                return false;
        }
        return true;
    }
    public boolean isAllocatedLowerMax() {
        for (int i = 0; i < _resourceClassesCount; ++i) {
            if (_allocated[i] > _maxNeed[i])
                return false;
        }
        return true;
    }
    public void allocate(int[] resource) {
        for (int i = 0; i < resource.length; ++i)
            _allocated[i] += resource[i];
    }
    public boolean isMaxNeedLowerTotalResource(int[] totalResource,
                                               StringBuffer executeInfo) {
        for (int i = 0; i < totalResource.length; ++i) {
            if (_maxNeed[i] > totalResource[i]) {
                executeInfo.append("进程" + _id + "的最大资源需求量大于系统资源量.");
                return false;
            }
        }
        return true;
    }
    public boolean isLowerMaxNeed(int[] resource, StringBuffer executeInfo) {
        for (int i = 0; i < resource.length; ++i) {
            if ((_allocated[i] + resource[i]) > _maxNeed[i]) {
                executeInfo.append("进程" + _id + "的资源请求量大于其最大资源需求量.");
                return false;
            }
        }
        return true;
    }
    private String printArray(int[] array) {
        StringBuffer buffer = new StringBuffer(20);
        buffer.append("[");
        for (int i = 0; i < array.length; i++) {
            buffer.append(String.valueOf(array[i]) + ", ");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        return buffer.toString();
    }
    public String toString() {
        StringBuffer print = new StringBuffer(50);
        print.append("{ processName=");
        print.append(_id);
        print.append(", maxNeed=");
        print.append(printArray(_maxNeed));
        print.append(", Allocated=");
        print.append(printArray(_allocated));
        print.append(", Need=");
        print.append(printArray(getNeed()));
        print.append("}");
        return print.toString();
    }
    // 用html格式显示.
    public String makeHtml() {
        StringBuffer tableRow = new StringBuffer(50);
        tableRow.append("<td>" + _id + "</td>");

        StringBuffer maxNeed = new StringBuffer(_maxNeed.length * 3);
        StringBuffer allocated = new StringBuffer(_maxNeed.length * 3);
        StringBuffer need = new StringBuffer(_maxNeed.length * 3);
        int[] needed = getNeed();
        for (int i = 0; i < _maxNeed.length; i++) {
            maxNeed.append("<td>" + String.valueOf(_maxNeed[i]) + "</td>");
            allocated.append("<td>" + String.valueOf(_allocated[i]) + "</td>");
            need.append("<td>" + String.valueOf(needed[i]) + "</td>");
        }
        tableRow.append(maxNeed);
        tableRow.append(allocated);
        tableRow.append(need);

        return tableRow.toString();
    }
}
