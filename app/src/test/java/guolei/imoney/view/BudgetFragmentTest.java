package guolei.imoney.view;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
/**
 * Created by guolei on 2016/6/25.
 */
public class BudgetFragmentTest {
    private BudgetFragment fragment;
    private String filePath = "d://uint.xls";

    @Before
    public void setUp() throws Exception {
       fragment = new BudgetFragment();
    }
    @Test
    public void testGetAssess() throws Exception {

       InputStream inputStream = new FileInputStream(filePath);
        try {
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(5);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            int cols = 0; // No of columns
            int tmp = 0;

            System.out.print(rows);
            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    cell = row.getCell(1);
                    float budget =getfloatValue(cell);
                    cell = row.getCell(2);
                    float actual_spend = getfloatValue(cell);
                    cell = row.getCell(3);
                    float actual_income = getfloatValue(cell);

                    int actual_output =  fragment.getAssess(budget,actual_spend,actual_income);
                    cell = row.createCell(6);
                    cell.setCellValue(actual_output);
                    cell = row.createCell(5);
                    cell.setCellValue(actual_output);
                    cell = row.createCell(7);
                    cell.setCellValue("Y");
                    inputStream.close();
                    FileOutputStream out = new FileOutputStream(filePath);
                    wb.write(out);
                    out.close();
                }}
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    float getfloatValue(Cell cell){
        if (cell != null) {
            return Float.parseFloat(cell.toString());
        }
        else
            return  -1;
    }
}