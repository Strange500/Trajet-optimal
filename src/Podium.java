package src;

import java.util.ArrayList;
import java.util.List;

public class Podium<T> {
    private T first;
    private T second;
    private T third;
    
    public T getFirst() {
        return first;
    }
    public void setFirst(T first) {
        this.first = first;
    }
    public T getSecond() {
        return second;
    }
    public void setSecond(T second) {
        this.second = second;
    }
    public T getThird() {
        return third;
    }
    public void setThird(T third) {
        this.third = third;
    }

    public List<String> getAllString() {
        List<String> list = new ArrayList<>();
        list.add(first.toString());
        list.add(second.toString());
        list.add(third.toString());
        return list;
    }

    
}
