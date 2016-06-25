package guolei.imoney.view;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by guolei on 2016/6/25.
 */
public class LoginFragmentTest {

    private LoginFragment fragment;
    private String filePath= "d://uint.xls";

    @Before
    public void setUp() throws Exception {
        fragment = new LoginFragment();
    }

    @Test
    public void testLogin() throws Exception {
        //SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
       // when(mockSharedPreferences.getString("email","null")).thenReturn("guolei0321@foxmail.com");

        InputStream inputStream = new FileInputStream(filePath);
        try {
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(4);
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
                    String email = cell.toString();
                    cell = row.getCell(2);
                    String password = cell.toString();
                    cell = row.getCell(4);
                    boolean except_value = cell.getBooleanCellValue();

                    boolean result = fragment.login(email,password);
                    cell = row.createCell(5);
                    cell.setCellValue(result);
                    cell = row.createCell(6);
                    String ifPass = result ==except_value ? "Y" : "N";
                    cell.setCellValue(ifPass);
                    inputStream.close();
                    FileOutputStream out = new FileOutputStream(filePath);
                    wb.write(out);
                    out.close();
                }}
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
}