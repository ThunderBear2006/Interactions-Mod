package interactions.Util;

public class Tuple <X,Y> {
    public X x;
    public Y y;

    public Tuple(X x, Y y){
        this.x = x;
        this.y = y;
    }

    public X First(){
        return this.x;
    }

    public Y Second(){
        return this.y;
    }

    public void SetFirst(X x){
        this.x = x;
    }

    public void SetSecond(Y y){
        this.y = y;
    }
}
