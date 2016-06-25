package guolei.imoney.model.db;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import java.util.Date;

import guolei.imoney.application.MvpApplication;

/**
 * Created by guolei on 2016/6/24.
 */
public class DBClassTest {

    private DBClass db;
    private String filePath = "d://uint.xls";
    @Before
    public void setUp() throws Exception {
        db = new DBClass(MvpApplication.getApplication());
    }

    @Test
    public void testGetAverage() throws Exception {
        Date date1 = new Date();
        long time = date1.getTime() - 24 * 60 * 60;
        Date date2 = new Date(time);
        //assertEquals(6f, db.getAverage(date1,date2,20), 0);

        InputStream inputStream = new FileInputStream(filePath);
        try {
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(3);
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
                    Date startDate = getDateFromCell(cell);
                    cell = row.getCell(2);
                    Date endDate = getDateFromCell(cell);
                    cell = row.getCell(3);
                    float amount = getfloatValue(cell);

                    float average = db.getAverage(startDate,endDate,amount);
                    cell = row.createCell(6);
                    cell.setCellValue(average);
                    cell = row.createCell(5);
                    cell.setCellValue(average);
                    cell = row.createCell(7);
                    cell.setCellValue("Y");
                    inputStream.close();
                    FileOutputStream out = new FileOutputStream(filePath);
                    wb.write(out);
                    out.close();
                }
            }

        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    float getfloatValue(Cell cell){
        if(cell != null){
            return Float.parseFloat(cell.toString());
        }
        else
            return -1;
    }
    Date getDateFromCell(Cell cell){
        if(cell != null){
            if(HSSFDateUtil.isCellDateFormatted(cell)){
                return cell.getDateCellValue();
            }
            return null;
        }
        else
            return null;
    }
}