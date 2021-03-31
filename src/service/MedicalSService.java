package service;

import model.medical_services.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class MedicalSService {
    private static ArrayList<Radiography> radiographies = Database.getRadiographies();
    private static ArrayList<Test> tests = Database.getTests();
    private static ArrayList<Consultation> consultations = Database.getConsultations();
    private Scanner scanner = new Scanner(System.in);
    //Utilizat pentru generarea aleatoare a listei de servicii, va fi scos in etapa urmatoare
    private Random rand = new Random();

    //Meniu optiuni servicii medicale
    public void menu() {
        int option = 0;
        int sort = 0;
        System.out.println("Choose an option.");

        while(option != 4){
            System.out.println("1. List all the services");
            System.out.println("2. Update service price");
            System.out.println("3. List services (ascending/descending)");
            System.out.println("4. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    showServices();
                    break;
                case 2:
                    updateServicePrice();
                    break;
                case 3:
                    System.out.println("Ascending or Descending? (0/1)");
                    sort = scanner.nextInt();
                    scanner.nextLine();
                    showSortedServices(sort);
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
        }
    }

    //Metoda ce este folosita pentru a genera lista de servicii oferite de clinica medicala
    //Aceasta metoda va fi folosita doar pentru etapa 1 a proiectului
    //In urmatoarele etape voi incarca lista serviciilor direct din CSV / Baza de date
    //Serviciile de tip consultatie sunt adaugate automat in momentul in care se creeaza o specializare noua
    public void generateServicesList() {
        generateTests();
        generateRadiographies();
    }

    //Metode helper pentru generateServicesList
    private void generateTests() {
        for(TestType t : TestType.values()){
            Test newTest = new Test();
            newTest.setType(t);
            //Generearea unui pret aleator cuprins intre 50 - 150 RON
            newTest.setPrice(rand.nextInt(100) + 50);
            tests.add(newTest);
        }
    }

    private void generateRadiographies() {
        for(RadiographyArea a : RadiographyArea.values()){
            Radiography newRadiography = new Radiography();
            newRadiography.setArea(a);
            //Generearea unui pret aleator cuprins intre 50 - 150 RON
            newRadiography.setPrice(rand.nextInt(100) + 50);
            radiographies.add(newRadiography);
        }

    }

    //Afisare lista servicii
    private void showServices() {
        System.out.println("The clinic offers the following services:");
        System.out.println("Consultations");
        showConsultations();

        System.out.println("Radiographies");
        showRadiographies();

        System.out.println("Tests");
        showTests();
    }

    //Afisare investigatii(teste) oferite
    private void showTests() {
        for (int i = 0; i < tests.size(); i++){
            System.out.println("-------------------");
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(tests.get(i));
        }
    }

    //Afisare radiografii oferite
    private void showRadiographies() {
        for (int i = 0; i < radiographies.size(); i++){
            System.out.println("-------------------");
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(radiographies.get(i));
        }
    }

    //Afisare consultatii oferite
    private void showConsultations() {
        for (int i = 0; i < consultations.size(); i++){
            System.out.println("-------------------");
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(consultations.get(i));
        }
    }

    //Actualizare pret serviciu
    private void updateServicePrice() {
        System.out.println("Select the category:");
        System.out.println("1. Consultations");
        System.out.println("2. Radiographies");
        System.out.println("3. Tests");

        int option = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Which service do you wish to update?");

        switch(option) {
            case 1:
                showConsultations();
                option = scanner.nextInt() - 1;
                scanner.nextLine();
                updatePriceOfService(consultations.get(option));
                break;
            case 2:
                showRadiographies();
                option = scanner.nextInt() - 1;
                scanner.nextLine();
                updatePriceOfService(radiographies.get(option));
                break;
            case 3:
                showTests();
                option = scanner.nextInt() - 1;
                scanner.nextLine();
                updatePriceOfService(tests.get(option));
                break;
        }
    }

    private void showSortedServices(int sort) {
        ArrayList<MedicalS> medicalServices = new ArrayList<>();

        for(Consultation c : consultations){
            medicalServices.add(c);
        }

        for(Test t : tests){
            medicalServices.add(t);
        }

        for(Radiography r : radiographies){
            medicalServices.add(r);
        }
        medicalServices.sort(new PriceSorter());

        if(sort == 1){
            Collections.reverse(medicalServices);
        }

        System.out.print("Medical services sorted by price ");
        if(sort == 1){
            System.out.println("[DESCENDING]");
        }
        else {
            System.out.println("[ASCENDING]");
        }

        for(MedicalS s : medicalServices){
            System.out.println(s);
        }
    }

    //Helper pentru updateServicePrice
    private void updatePriceOfService(MedicalS s){
        System.out.println("Enter the new price: ");
        int price = scanner.nextInt();
        scanner.nextLine();
        s.setPrice(price);
        System.out.println("The price has been updated!");
    }
}
