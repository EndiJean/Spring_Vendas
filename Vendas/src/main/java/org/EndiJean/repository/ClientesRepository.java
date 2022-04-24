package org.EndiJean.repository;

import org.EndiJean.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientesRepository extends JpaRepository<Cliente, Integer> {

    @Query(value = " select c from Cliente c where c.nome like :nome ")
    List<Cliente> encontrarPorNome(String nome);

    boolean existsByNome (@Param("nome") String nome);

    @Query(" select c from Cliente c left join fetch c.pedidos where c.id = :id ")
    Cliente findClienteFetchPedidos(@Param("id") Integer id);
}
