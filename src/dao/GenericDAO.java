package dao;

import java.util.List;

/**
 * Interfaz generica para operaciones CRUD.
 * @param <T> Tipo de entidad
 */
public interface GenericDAO<T> {
    boolean crear(T entity);
    T obtenerPorId(int id);
    List<T> obtenerTodos();
    boolean actualizar(T entity);
    boolean eliminar(int id);
}
