package org.EndiJean.service.impl;

import org.EndiJean.entity.Cliente;
import org.EndiJean.entity.ItemPedido;
import org.EndiJean.entity.Pedido;
import org.EndiJean.entity.Produto;
import org.EndiJean.enums.StatusPedido;
import org.EndiJean.exception.PedidoNaoEncontradoException;
import org.EndiJean.exception.RegraNegocioException;
import org.EndiJean.repository.ClientesRepository;
import org.EndiJean.repository.ItemsPedidoRepository;
import org.EndiJean.repository.PedidosRepository;
import org.EndiJean.repository.ProdutoRepository;
import org.EndiJean.rest.dto.ItemPedidoDTO;
import org.EndiJean.rest.dto.PedidoDTO;
import org.EndiJean.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private PedidosRepository pedidosRepository;
    private ClientesRepository clientesRepository;
    private ProdutoRepository produtoRepository;
    private ItemsPedidoRepository itemsPedidoRepository;

    public PedidoServiceImpl(PedidosRepository pedidosRepository, ClientesRepository clientesRepository, ProdutoRepository produtoRepository, ItemsPedidoRepository itemsPedidoRepository) {
        this.pedidosRepository = pedidosRepository;
        this.clientesRepository = clientesRepository;
        this.produtoRepository = produtoRepository;
        this.itemsPedidoRepository = itemsPedidoRepository;
    }

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Codigo de Cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedido = converterItems(pedido, dto.getItems());
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemPedido);
        pedido.setItens(itemPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidosRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidosRepository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Ñ é pssovel realizar um pedido sem items.");
        }

        return items.stream().map( dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto = produtoRepository
                    .findById(idProduto)
                    .orElseThrow(
                            () -> new RegraNegocioException(
                                    "Código de Produto Invalido: "+ idProduto
                            ));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }
}
