package br.com.ipet.ordering.infrastructure.persistence.shoppingcart;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCart;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShoppingCartProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceRepository repository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartPersistenceMapper shoppingCartPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> ofId(ShoppingCartId id) {
        return repository.findById(id.value()).map(shoppingCartMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return repository.findByCustomer_Id(customerId.value()).map(shoppingCartMapper::toDomain);
    }

    @Override
    @Transactional
    public void add(ShoppingCart shoppingCart) {
        repository.findById(shoppingCart.id().value())
                .ifPresentOrElse(shoppingCartPersistence ->
                        update(shoppingCartPersistence, shoppingCart),
                        () -> insert(shoppingCart));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ShoppingCartId id) {
        return repository.existsById(id.value());
    }

    @Override
    @Transactional
    public void remove(ShoppingCart shoppingCart) {
        repository.deleteById(shoppingCart.id().value());
    }

    @Override
    @Transactional
    public void remove(ShoppingCartId id) {
        repository.deleteById(id.value());
    }

    private void insert(ShoppingCart shoppingCart) {
        var shoppingCartPersistence = shoppingCartPersistenceMapper.fromDomain(shoppingCart);
        repository.saveAndFlush(shoppingCartPersistence);
    }

    private void update(ShoppingCartPersistenceEntity shoppingCartPersistence, ShoppingCart shoppingCart) {
        shoppingCartPersistence = shoppingCartPersistenceMapper.merge(shoppingCartPersistence, shoppingCart);
        repository.saveAndFlush(shoppingCartPersistence);
    }
}
