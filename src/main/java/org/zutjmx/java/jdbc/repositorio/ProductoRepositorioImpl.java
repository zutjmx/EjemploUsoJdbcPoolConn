package org.zutjmx.java.jdbc.repositorio;

import org.zutjmx.java.jdbc.modelo.Categoria;
import org.zutjmx.java.jdbc.modelo.Producto;
import org.zutjmx.java.jdbc.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImpl implements Repositorio<Producto> {
    private static String consultaSqlTodos = "SELECT p.*, c.nombre AS nombre_categoria FROM productos AS p INNER JOIN categorias AS c ON (p.categoria_id = c.id)";
    private static String consultaSqlById = consultaSqlTodos.concat(" WHERE p.id = ?");
    private static String insertaSql = "insert into productos (nombre, precio, categoria_id, fecha_registro) values(?,?,?,?)";
    private static String actualizaSql = "update productos set nombre = ?, precio = ?, categoria_id = ? where id = ?";
    private static String borrarSlq = "delete from productos where id = ?";

    private Connection getConexion() throws SQLException {
        return ConexionBD.getConnection();
    }

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        try(Connection connection = getConexion();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(consultaSqlTodos)) {
            while (resultSet.next()) {
                Producto producto = crearProducto(resultSet);
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    @Override
    public Producto porId(Long id) {
        Producto producto = null;
        try(Connection connection = getConexion();
            PreparedStatement preparedStatement = connection.prepareStatement(consultaSqlById)) {
            preparedStatement.setLong(1,id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    producto = crearProducto(resultSet);
                }
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    @Override
    public void guardar(Producto producto) {
        String cadenaSlq;
        if(producto.getId() != null && producto.getId() > 0) {
            cadenaSlq = actualizaSql;
        } else {
            cadenaSlq = insertaSql;
        }
        try(Connection connection = getConexion();
            PreparedStatement preparedStatement = connection.prepareStatement(cadenaSlq)) {
            preparedStatement.setString(1,producto.getNombre());
            preparedStatement.setLong(2,producto.getPrecio());
            preparedStatement.setLong(3,producto.getCategoria().getId());
            if (producto.getId() != null && producto.getId() > 0) {
                preparedStatement.setLong(4, producto.getId());
            } else {
                preparedStatement.setDate(4,new Date(producto.getFechaCreacion().getTime()));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        try(Connection connection = getConexion();
            PreparedStatement statement = connection.prepareStatement(borrarSlq)) {
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Producto crearProducto(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setId(resultSet.getLong("id"));
        producto.setNombre(resultSet.getString("nombre"));
        producto.setPrecio(resultSet.getInt("precio"));
        producto.setFechaCreacion(resultSet.getDate("fecha_registro"));
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("categoria_id"));
        categoria.setNombre(resultSet.getString("nombre_categoria"));
        producto.setCategoria(categoria);
        return producto;
    }

}
