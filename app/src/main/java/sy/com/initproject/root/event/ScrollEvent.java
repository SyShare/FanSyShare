package sy.com.initproject.root.event;

/**
 * @date：2018/7/18
 * @author: SyShare
 */
public class ScrollEvent {
    boolean scrollOut;

    public ScrollEvent(boolean scrollOut) {
        this.scrollOut = scrollOut;
    }

    public boolean isScrollOut() {
        return this.scrollOut;
    }
}
