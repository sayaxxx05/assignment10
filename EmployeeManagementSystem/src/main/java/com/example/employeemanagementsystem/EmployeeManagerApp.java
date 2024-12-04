package com.example.employeemanagementsystem;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EmployeeManagerApp extends Application {

    // TableView to display the employee data
    private TableView<Employee> employeeTable;
    private ObservableList<Employee> employeeList;

    // Input fields for adding new employees
    private TextField nameField, hourlyRateField, hoursWorkedField, maxHoursField;
    private ComboBox<String> employeeTypeComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Manager");

        // TableView for displaying employees
        employeeList = FXCollections.observableArrayList();
        employeeTable = new TableView<>(employeeList);

        // Set up columns for the TableView
        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Employee, String> typeColumn = new TableColumn<>("Employee Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployeeType()));

        TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calculateSalary()).asObject());

        employeeTable.getColumns().addAll(nameColumn, typeColumn, salaryColumn);

        // Input form for adding new employees
        nameField = new TextField();
        nameField.setPromptText("Enter Name");

        employeeTypeComboBox = new ComboBox<>();
        employeeTypeComboBox.getItems().addAll("Full-time", "Part-time", "Contractor");
        employeeTypeComboBox.setValue("Full-time");

        hourlyRateField = new TextField();
        hourlyRateField.setPromptText("Hourly Rate");

        hoursWorkedField = new TextField();
        hoursWorkedField.setPromptText("Hours Worked");

        maxHoursField = new TextField();
        maxHoursField.setPromptText("Max Hours");

        Button addButton = new Button("Add Employee");
        addButton.setOnAction(e -> addEmployee());

        Button calculateButton = new Button("Calculate Salaries");
        calculateButton.setOnAction(e -> updateSalaries());

        Button removeButton = new Button("Remove Selected Employee");
        removeButton.setOnAction(e -> removeEmployee());

        // Layout
        HBox formLayout = new HBox(10, nameField, employeeTypeComboBox, hourlyRateField, hoursWorkedField, maxHoursField, addButton);
        VBox layout = new VBox(10, formLayout, employeeTable, calculateButton, removeButton);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Add a new employee based on input form
    private void addEmployee() {
        String name = nameField.getText();
        String type = employeeTypeComboBox.getValue();

        // Input validation
        if (name.isEmpty()) {
            showAlert("Error", "Name cannot be empty!");
            return;
        }

        if (type.equals("Full-time")) {
            try {
                double annualSalary = Double.parseDouble(hourlyRateField.getText());  // This would be the annual salary for full-time
                employeeList.add(new FullTimeEmployee(name, annualSalary));
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid salary input!");
            }
        } else if (type.equals("Part-time")) {
            try {
                double hourlyRate = Double.parseDouble(hourlyRateField.getText());
                double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                if (hoursWorked < 0) {
                    showAlert("Error", "Hours worked cannot be negative!");
                    return;
                }
                employeeList.add(new PartTimeEmployee(name, hourlyRate, hoursWorked));
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input for part-time employee!");
            }
        } else if (type.equals("Contractor")) {
            try {
                double hourlyRate = Double.parseDouble(hourlyRateField.getText());
                double maxHours = Double.parseDouble(maxHoursField.getText());
                if (maxHours < 0) {
                    showAlert("Error", "Max hours cannot be negative!");
                    return;
                }
                employeeList.add(new Contractor(name, hourlyRate, maxHours));
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid input for contractor!");
            }
        }

        // Clear input fields
        nameField.clear();
        hourlyRateField.clear();
        hoursWorkedField.clear();
        maxHoursField.clear();
    }

    // Update the salary for each employee in the list (recalculate and refresh)
    private void updateSalaries() {
        employeeTable.refresh();  // Refresh the table to recalculate and display updated salaries
    }

    // Remove the selected employee from the table
    private void removeEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeList.remove(selectedEmployee);
        } else {
            showAlert("Error", "Please select an employee to remove.");
        }
    }

    // Show an alert box for errors
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
