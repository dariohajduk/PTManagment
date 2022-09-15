package com.example.ptmanagment.utils;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.example.ptmanagment.component.Order;
import com.example.ptmanagment.component.Restaurant;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.fragments.NewRestFragment;
import com.example.ptmanagment.fragments.NewUserFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jplus.hyberbin.excel.service.ExportExcelService;

public class ExcelToObject {


    private ArrayList<User> employeeList;
    private ArrayList<Restaurant> restList;
    private FirebaseDatabase database;
    private DatabaseReference userDB;
    private DatabaseReference restDB;

    public ArrayList<User> getEmployeeList() {
        return employeeList;
    }

    public ArrayList<Restaurant> getRestList() {
        return restList;
    }

    public void UploadeUserWithExcel(File file, NewUserFragment newUserFragment) {
        try {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            database = FirebaseDatabase.getInstance();
            userDB = database.getReference("Users");
            employeeList = new ArrayList<>();
            //I've Header and I'm ignoring header for that I've +1 in loop
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                User e = new User();
                Row ro = sheet.getRow(i);
                for (int j = ro.getFirstCellNum(); j <= ro.getLastCellNum(); j++) {
                    Cell ce = ro.getCell(j);
                    if (j == 0) {
                        e.setFirst(ce.getStringCellValue());
                    }
                    if (j == 1) {
                        e.setLast(ce.getStringCellValue());
                    }
                    if (j == 2) {
                        e.setEmail(ce.getStringCellValue());
                    }
                    if (j == 3) {
                        e.setDepartment(ce.getStringCellValue());
                    }
                    if (j == 4) {
                        e.setShift(ce.getStringCellValue());
                    }
                    if (j == 5) {
                        e.setAdmin(ce.getBooleanCellValue());
                        employeeList.add(e);
                    }
                }
            }
            for (User emp : employeeList) {
                System.out.println("firstName:" + emp.getFirst() + " lastName:" + emp.getLast() + employeeList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getFirst() != null) {
                User user = employeeList.get(i);
                userDB.child(user.getFirst() + " " + user.getLast()).setValue(user);
            }
        }

        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getFirst() != null) {
                EmailAndPassword emailAndPassword = new EmailAndPassword();
                emailAndPassword.createAccount2(employeeList.get(i), newUserFragment);
            }
        }

    }

    public void UploadeRestWithExcel(File file, NewRestFragment newRestFragment) throws IOException, InvalidFormatException {
        try {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            database = FirebaseDatabase.getInstance();
            restDB = database.getReference("Restaurants");
            restList = new ArrayList<>();
            //I've Header and I'm ignoring header for that I've +1 in loop
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Restaurant res = new Restaurant();
                Row ro = sheet.getRow(i);
                for (int j = ro.getFirstCellNum(); j <= ro.getLastCellNum(); j++) {
                    Cell ce = ro.getCell(j);
                    if (j == 0 && ce.getStringCellValue() != null) {
                        //If you have Header in text It'll throw exception because it won't get NumericValue
                        res.setRestName(ce.getStringCellValue());
                    }

                    if (j == 2 && ce.getStringCellValue() != null) {
                        res.setRestPhone(ce.getStringCellValue());
                    }
                    if (j == 3 && ce.getStringCellValue() != null) {
                        res.setRestEmail(ce.getStringCellValue());
                    }
                    res.setRestAddress("");
                    restList.add(res);
                }
            }
            for (Restaurant restaurant : restList) {
                System.out.println("restName:" + restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < restList.size(); i++) {
            if (restList.get(i).getRestName() != null) {
                restDB.child(restList.get(i).getRestName()).child("Details").setValue(restList.get(i));
            }
        }

    }

    public void UploadeMealsWithExcel(File file, NewRestFragment newRestFragment) throws IOException, InvalidFormatException {
        try {
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            database = FirebaseDatabase.getInstance();
            restDB = database.getReference("Restaurants");
            restList = new ArrayList<>();
            //I've Header and I'm ignoring header for that I've +1 in loop
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Restaurant res = new Restaurant();
                Row ro = sheet.getRow(i);
                for (int j = ro.getFirstCellNum(); j <= ro.getLastCellNum(); j++) {
                    Cell ce = ro.getCell(j);
                    if (j == 0 && ce.getStringCellValue() != null) {
                        //If you have Header in text It'll throw exception because it won't get NumericValue
                        res.setRestName(ce.getStringCellValue());
                    }

                    if (j == 2 && ce.getStringCellValue() != null) {
                        res.setRestPhone(ce.getStringCellValue());
                    }
                    if (j == 3 && ce.getStringCellValue() != null) {
                        res.setRestEmail(ce.getStringCellValue());
                    }
                    res.setRestAddress("");
                    restList.add(res);
                }
            }
            for (Restaurant restaurant : restList) {
                System.out.println("restName:" + restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < restList.size(); i++) {
            if (restList.get(i).getRestName() != null) {
                restDB.child(restList.get(i).getRestName()).child("Details").setValue(restList.get(i));
            }
        }

    }

    public void ExportOrdersToExcelFile(ArrayList<Order> orders, ArrayList<String> employee)
    {

        database = FirebaseDatabase.getInstance();
        restDB = database.getReference("Restaurants");
        restDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restList.add(snapshot.getValue(Restaurant.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Create an Excel File
        XSSFWorkbook orderFile = new XSSFWorkbook();
        //Create Sheet With Restaurant Name
        XSSFSheet sheet = orderFile.createSheet("Orders");
        //Create First Row
        XSSFRow row = sheet.createRow(0);
        //region Create relevant Cells
        Cell restCell = row.createCell(0);
        Cell nameCell = row.createCell(1);
        Cell mealCell = row.createCell(2);
        Cell drinkCell = row.createCell(3);

        restCell.setCellValue("Restaurant Name");
        nameCell.setCellValue("Employee Name");
        mealCell.setCellValue("Meal Ordered");
        drinkCell.setCellValue("Drink Ordered");
        //endregion

        for(int i = 0;i<orders.size();i++)
        {
            row=sheet.createRow(i+1);
            for(int j=0;j<4;j++)
            {
                Cell cell = row.createCell(j);

                if(cell.getColumnIndex()==0)
                    cell.setCellValue(orders.get(i).getRest());
                else if (cell.getColumnIndex()==1)
                {
                    cell.setCellValue(employee.get(i));
                }
                else if (cell.getColumnIndex()==2)
                {
                    cell.setCellValue(orders.get(i).getMeal());
                }
                else if (cell.getColumnIndex()==3)
                {
                    cell.setCellValue(orders.get(i).getDrink());
                }

            }
        }
        try{
            String fileName = "Orders.xlsx";
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Documents/"+fileName);
            FileOutputStream out = new FileOutputStream(file);
            //FileOutputStream out = new FileOutputStream(new File("Orders.xlsx"));
            orderFile.write(out);
            out.close();
            System.out.println("Excel File is Created");
        }
        catch (FileNotFoundException exception)
        {
            Logger.getLogger(ExportExcelService.class.getName()).log(Level.SEVERE,null,exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
