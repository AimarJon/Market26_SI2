package gui;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import iterator.*;
import domain.Sale;

public class ExtendedIteratorTest {

    public static void main(String[] args) {

        DataAccess da = new DataAccess();
        BLFacade facade = new BLFacadeImplementation(da);

        // Ajusta el texto de búsqueda y el email a los datos de tu base de datos
        ExtendedIterator<Sale> i = facade.getSalesIterator("a", "seller1@gmail.com");

        // Atrás hacia delante
        i.goLast();
        System.out.println("_____________________");
        System.out.println("FROM LAST TO FIRST");
        while (i.hasPrevious())
            System.out.println(i.previous());

        // Delante hacia atrás
        i.goFirst();
        System.out.println();
        System.out.println("_____________________");
        System.out.println("FROM FIRST TO LAST");
        while (i.hasNext())
            System.out.println(i.next());
    }
}