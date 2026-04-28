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

    private final ShoppingCartPersistenceRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartPersistenceMapper shoppingCartPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> ofId(ShoppingCartId id) {
        return shoppingCartRepository.findById(id.value()).map(shoppingCartMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return shoppingCartRepository.findByCustomer_Id(customerId.value()).map(shoppingCartMapper::toDomain);
    }

    @Override
    @Transactional
    public void add(ShoppingCart shoppingCart) {
        shoppingCartRepository.findById(shoppingCart.id().value())
                .ifPresentOrElse(
                        shoppingCartPersistence -> this.update(shoppingCartPersistence, shoppingCart),
                        () -> this.insert(shoppingCart));

        shoppingCart.clearDomainEvents();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return shoppingCartRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ShoppingCartId id) {
        return shoppingCartRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void remove(ShoppingCart shoppingCart) {
        shoppingCartRepository.deleteById(shoppingCart.id().value());
    }

    @Override
    @Transactional
    public void remove(ShoppingCartId id) {
        shoppingCartRepository.deleteById(id.value());
    }

    private void insert(ShoppingCart shoppingCart) {
        var shoppingCartPersistence = shoppingCartPersistenceMapper.fromDomain(shoppingCart);
        shoppingCartRepository.saveAndFlush(shoppingCartPersistence);
    }

    private void update(ShoppingCartPersistenceEntity shoppingCartPersistence, ShoppingCart shoppingCart) {
        shoppingCartPersistence = shoppingCartPersistenceMapper.merge(shoppingCartPersistence, shoppingCart);
        shoppingCartRepository.saveAndFlush(shoppingCartPersistence);
    }
}
