package com.example.ptmanagment.utils;

import android.content.res.AssetManager;
import android.os.Environment;

import com.example.ptmanagment.component.Restaurant;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.fragments.NewRestFragment;
import com.example.ptmanagment.fragments.NewUserFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToObject {

    private ArrayList<User> employeeList;
    private ArrayList<Restaurant> restList;
    private FirebaseDatabase database;
    private DatabaseReference userDB;
    private DatabaseReference restDB;



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
                        //If you have Header in text It'll throw exception because it won't get NumericValue
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
                    }
                    employeeList.add(e);
                }
            }
            for (User emp : employeeList) {
                System.out.println("firstName:" + emp.getFirst() + " lastName:" + emp.getLast() + employeeList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < employeeList.size(); i++) {
            if(employeeList.get(i).getFirst()!=null) {
                User user= employeeList.get(i);
                userDB.child(user.getFirst() + " " + user.getLast()).setValue(user);
            }
        }

       for (int i = 0; i < employeeList.size(); i++) {
            if(employeeList.get(i).getFirst()!=null) {
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
                    if (j == 0 && ce.getStringCellValue()!=null) {
                        //If you have Header in text It'll throw exception because it won't get NumericValue
                        res.setRestName(ce.getStringCellValue());
                    }

                    if (j == 2 && ce.getStringCellValue()!=null) {
                        res.setRestPhone(ce.getStringCellValue());
                    }
                    if (j == 3 && ce.getStringCellValue()!=null) {
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
            if(restList.get(i).getRestName()!=null) {
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
                    if (j == 0 && ce.getStringCellValue()!=null) {
                        //If you have Header in text It'll throw exception because it won't get NumericValue
                        res.setRestName(ce.getStringCellValue());
                    }

                    if (j == 2 && ce.getStringCellValue()!=null) {
                        res.setRestPhone(ce.getStringCellValue());
                    }
                    if (j == 3 && ce.getStringCellValue()!=null) {
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
            if(restList.get(i).getRestName()!=null) {
                restDB.child(restList.get(i).getRestName()).child("Details").setValue(restList.get(i));
            }
        }

    }


}
