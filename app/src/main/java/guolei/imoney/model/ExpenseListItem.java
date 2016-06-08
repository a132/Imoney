package guolei.imoney.model;

/**
 * Created by guolei on 2016/6/2.
 */
public class ExpenseListItem {
    private Expense expense;
    public boolean isSection = false;
    public String sectionStr;

    public ExpenseListItem(Expense expense){
        this.expense = expense;
    }

    public void setSection(boolean isSection, String sectionStr){
        this.isSection = isSection;
        this.sectionStr = sectionStr;
        if(expense!= null && isSection){
            int month = expense.getMonth();
            this.sectionStr = month + "月";
        }
    }

    @Override
    public String toString() {
        if(isSection)
            return sectionStr;
        if(expense != null ){
            String display = expense.getDescription() + "  " + expense.getAmount();
            return display;
        }
        return "error";
    }
}
