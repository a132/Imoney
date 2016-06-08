package guolei.imoney.model;

/**
 * Created by guolei on 2016/5/6.
 */
public enum  ExpenseType {
    shopping("购物"),food("食物"),traffic("交通");
    private String name;
    private ExpenseType(final String name){
        this.name = name ;
    }
    @Override
    public String toString(){
        return name;
    }
}
