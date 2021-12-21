package org.zutjmx.java.jdbc;

import org.zutjmx.java.jdbc.modelo.Producto;
import org.zutjmx.java.jdbc.repositorio.ProductoRepositorioImpl;
import org.zutjmx.java.jdbc.repositorio.Repositorio;
import org.zutjmx.java.jdbc.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class EjemploJdbcDelete {
    public static void main(String[] args) {

        Repositorio<Producto> repositorio = new ProductoRepositorioImpl();

        System.out.println(":::: Se prueba el método lista() ::::");
        repositorio.listar().forEach(System.out::println);

        System.out.println(":::: Se prueba el método porId() ::::");
        System.out.println(repositorio.porId(2L));

        System.out.println(":::: Se prueba el método eliminar() ::::");
        repositorio.eliminar(6L);

        System.out.println(":::: Producto eliminado ::::");

        System.out.println(":::: Lista de productos ::::");
        repositorio.listar().forEach(System.out::println);


    }
}
